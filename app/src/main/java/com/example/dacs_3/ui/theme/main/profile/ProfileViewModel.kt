package com.example.dacs_3.ui.theme.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val followsRef = db.collection("Follows")
    private val usersRef = db.collection("Users")

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
            try {
                val followingTask = followsRef.whereEqualTo("followerId", currentUserId).get()
                val followerTask = followsRef.whereEqualTo("followedId", currentUserId).get()

                Tasks.whenAllSuccess<QuerySnapshot>(followingTask, followerTask)
                    .addOnSuccessListener { results ->
                        val followingIds = results[0].documents.mapNotNull { it.getString("followedId") }
                        val followerIds = results[1].documents.mapNotNull { it.getString("followerId") }

                        val mutualIds = followingIds.intersect(followerIds.toSet()).toList()
                        val oneWayFollowingIds = followingIds.filterNot { mutualIds.contains(it) }

                        val allUserIds = (mutualIds + oneWayFollowingIds).distinct()
                        fetchUsersInBatches(allUserIds) { users ->
                            _mutualFollows.value = users.filter { mutualIds.contains(it.userId) }
                            _oneWayFollowing.value = users.filter { oneWayFollowingIds.contains(it.userId) }
                            _isLoading.value = false
                        }
                    }
                    .addOnFailureListener { exception ->
                        _mutualFollows.value = emptyList()
                        _oneWayFollowing.value = emptyList()
                        _error.value = "Failed to load follows: ${exception.message}"
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _mutualFollows.value = emptyList()
                _oneWayFollowing.value = emptyList()
                _error.value = "Unexpected error: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun unfollowUser(currentUserId: String, targetUserId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                followsRef
                    .whereEqualTo("followerId", currentUserId)
                    .whereEqualTo("followedId", targetUserId)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val deleteTasks = snapshot.documents.map { followsRef.document(it.id).delete() }
                        Tasks.whenAll(deleteTasks)
                            .addOnSuccessListener {
                                _mutualFollows.value = _mutualFollows.value.filterNot { it.userId == targetUserId }
                                _oneWayFollowing.value = _oneWayFollowing.value.filterNot { it.userId == targetUserId }
                                _isLoading.value = false
                            }
                            .addOnFailureListener { exception ->
                                _error.value = "Failed to unfollow user: ${exception.message}"
                                _isLoading.value = false
                            }
                    }
                    .addOnFailureListener { exception ->
                        _error.value = "Failed to fetch follow data: ${exception.message}"
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _error.value = "Unexpected error during unfollow: ${e.message}"
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