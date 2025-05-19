package com.example.dacs_3.ui.theme.main.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val followsRef = db.collection("follows")
    private val usersRef = db.collection("users")

    private val _mutualFollows = MutableStateFlow<List<User>>(emptyList()) // Follow lẫn nhau
    private val _oneWayFollowing = MutableStateFlow<List<User>>(emptyList()) // Mình follow họ, họ không follow lại
    private val _error = MutableStateFlow<String?>(null) // Lưu thông báo lỗi nếu có
    private val _isLoading = MutableStateFlow(false) // Trạng thái tải

    val mutualFollows: StateFlow<List<User>> get() = _mutualFollows
    val oneWayFollowing: StateFlow<List<User>> get() = _oneWayFollowing
    val error: StateFlow<String?> get() = _error
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun loadFollowingAndBuddies(currentUserId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d("ProfileViewModel", "Start loadFollowingAndBuddies for userId=$currentUserId")
            try {
                val followingSnapshot = followsRef.whereEqualTo("followerId", currentUserId).get().await()
                Log.d("ProfileViewModel", "Following snapshot size: ${followingSnapshot.size()}")

                val followerSnapshot = followsRef.whereEqualTo("followedId", currentUserId).get().await()
                Log.d("ProfileViewModel", "Follower snapshot size: ${followerSnapshot.size()}")

                val followingIds = followingSnapshot.documents.mapNotNull { it.getString("followedId") }
                Log.d("ProfileViewModel", "Following IDs: $followingIds")

                val followerIds = followerSnapshot.documents.mapNotNull { it.getString("followerId") }
                Log.d("ProfileViewModel", "Follower IDs: $followerIds")

                val mutualIds = followingIds.intersect(followerIds.toSet()).toList()
                Log.d("ProfileViewModel", "Mutual IDs: $mutualIds")

                val oneWayFollowingIds = followingIds.filterNot { mutualIds.contains(it) }
                Log.d("ProfileViewModel", "One way following IDs: $oneWayFollowingIds")

                val allUserIds = (mutualIds + oneWayFollowingIds).distinct()
                Log.d("ProfileViewModel", "All user IDs to fetch: $allUserIds")

                val users = fetchUsers(allUserIds)
                Log.d("ProfileViewModel", "Fetched users count: ${users.size}")

                _mutualFollows.value = users.filter { mutualIds.contains(it.userId) }
                _oneWayFollowing.value = users.filter { oneWayFollowingIds.contains(it.userId) }

                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Failed to load follows", e)
                _mutualFollows.value = emptyList()
                _oneWayFollowing.value = emptyList()
                _error.value = "Failed to load follows: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    private suspend fun fetchUsers(userIds: List<String>): List<User> {
        if (userIds.isEmpty()) {
            Log.d("ProfileViewModel", "No userIds to fetch")
            return emptyList()
        }

        val batchSize = 10
        val batches = userIds.chunked(batchSize)
        val users = mutableListOf<User>()

        for (batch in batches) {
            Log.d("ProfileViewModel", "Fetching users batch: $batch")
            val snapshot = usersRef.whereIn("userId", batch).get().await()
            val batchUsers = snapshot.toObjects(User::class.java)
            Log.d("ProfileViewModel", "Fetched ${batchUsers.size} users in this batch")
            users.addAll(batchUsers)
        }

        Log.d("ProfileViewModel", "Total users fetched: ${users.size}")
        return users
    }
    fun unfollowUser(currentUserId: String, targetUserId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = followsRef
                    .whereEqualTo("followerId", currentUserId)
                    .whereEqualTo("followedId", targetUserId)
                    .get()
                    .await()

                val deleteTasks = snapshot.documents.map { followsRef.document(it.id).delete().await() }

                // Sau khi xóa xong update lại danh sách theo logic bạn muốn
                _mutualFollows.value = _mutualFollows.value.filterNot { it.userId == targetUserId }
                _oneWayFollowing.value = _oneWayFollowing.value.filterNot { it.userId == targetUserId }
                _isLoading.value = false

            } catch (e: Exception) {
                _error.value = "Failed to unfollow user: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    private fun fetchUsersInBatches(userIds: List<String>, onSuccess: (List<User>) -> Unit) {
        if (userIds.isEmpty()) {
            onSuccess(emptyList())
            return
        }

        val batchSize = 10 // Firestore whereIn limit
        val batches = userIds.chunked(batchSize)
        val users = mutableListOf<User>()

        viewModelScope.launch {
            try {
                batches.forEach { batch ->
                    usersRef.whereIn("userId", batch).get()
                        .addOnSuccessListener { snapshot ->
                            users.addAll(snapshot.toObjects(User::class.java))
                            if (users.size == userIds.size) {
                                onSuccess(users)
                            }
                        }
                        .addOnFailureListener { exception ->
                            _error.value = "Failed to fetch users: ${exception.message}"
                            onSuccess(emptyList())
                        }
                }
            } catch (e: Exception) {
                _error.value = "Unexpected error fetching users: ${e.message}"
                onSuccess(emptyList())
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}