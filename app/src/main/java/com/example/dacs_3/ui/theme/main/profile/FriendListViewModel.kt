package com.example.dacs_3.ui.theme.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.Follow
import com.example.dacs_3.model.User
import com.example.dacs_3.model.UserWithStatus
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FriendListViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val followsRef = db.collection("Follows")
    private val usersRef = db.collection("Users")

    private val _friendsList = MutableStateFlow<List<UserWithStatus>>(emptyList())
    private val _followingList = MutableStateFlow<List<UserWithStatus>>(emptyList())
    private val _error = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)

    val friendsList: StateFlow<List<UserWithStatus>> get() = _friendsList
    val followingList: StateFlow<List<UserWithStatus>> get() = _followingList
    val error: StateFlow<String?> get() = _error
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun loadFriendAndFollowingLists(uid: String, currentUserId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val followingTask = followsRef.whereEqualTo("followerId", uid).get()
                val followerTask = followsRef.whereEqualTo("followedId", uid).get()

                Tasks.whenAllSuccess<QuerySnapshot>(followingTask, followerTask)
                    .addOnSuccessListener { results ->
                        val followingIds = results[0].documents.mapNotNull { it.getString("followedId") }
                        val followerIds = results[1].documents.mapNotNull { it.getString("followerId") }

                        val mutualIds = followingIds.intersect(followerIds.toSet()).toList()
                        val oneWayFollowingIds = followingIds.filterNot { mutualIds.contains(it) }

                        val allUserIds = (mutualIds + oneWayFollowingIds).distinct()
                        fetchUsersAndRelationship(allUserIds, currentUserId) { usersWithStatus ->
                            _friendsList.value = usersWithStatus.filter { mutualIds.contains(it.user.userId) }
                            _followingList.value = usersWithStatus.filter { oneWayFollowingIds.contains(it.user.userId) }
                            _isLoading.value = false
                        }
                    }
                    .addOnFailureListener { exception ->
                        _error.value = "Failed to load follows: ${exception.message}"
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _error.value = "Unexpected error: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    private fun fetchUsersAndRelationship(userIds: List<String>, currentUserId: String, onSuccess: (List<UserWithStatus>) -> Unit) {
        if (userIds.isEmpty()) {
            onSuccess(emptyList())
            return
        }

        val batchSize = 10
        val batches = userIds.chunked(batchSize)
        val usersWithStatus = mutableListOf<UserWithStatus>()

        viewModelScope.launch {
            try {
                val currentUserFollowingTask = followsRef.whereEqualTo("followerId", currentUserId).get()
                val currentUserFollowersTask = followsRef.whereEqualTo("followedId", currentUserId).get()

                Tasks.whenAllSuccess<QuerySnapshot>(currentUserFollowingTask, currentUserFollowersTask)
                    .addOnSuccessListener { results ->
                        val currentUserFollowing = results[0].documents.mapNotNull { it.getString("followedId") }.toSet()
                        val currentUserFollowers = results[1].documents.mapNotNull { it.getString("followerId") }.toSet()

                        batches.forEach { batch ->
                            usersRef.whereIn("userId", batch).get()
                                .addOnSuccessListener { snapshot ->
                                    val fetchedUsers = snapshot.toObjects(User::class.java)

                                    val userStatusList = fetchedUsers.map { user ->
                                        val isFollowing = currentUserFollowing.contains(user.userId)
                                        val isFollower = currentUserFollowers.contains(user.userId)

                                        val status = when {
                                            isFollowing && isFollower -> "Unfollow"
                                            isFollowing && !isFollower -> "Unfollow"
                                            !isFollowing && isFollower -> "Follow"
                                            else -> "Follow"
                                        }

                                        UserWithStatus(user = user, status = status)
                                    }

                                    usersWithStatus.addAll(userStatusList)

                                    if (usersWithStatus.size >= userIds.size) {
                                        onSuccess(usersWithStatus)
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    _error.value = "Failed to fetch users: ${exception.message}"
                                    onSuccess(emptyList())
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        _error.value = "Failed to load current user follow data: ${exception.message}"
                        onSuccess(emptyList())
                    }
            } catch (e: Exception) {
                _error.value = "Unexpected error fetching users: ${e.message}"
                onSuccess(emptyList())
            }
        }
    }

    fun onFollowUnfollow(userId: String, currentUserId: String, relationshipStatus: String) {
        viewModelScope.launch {
            if (relationshipStatus == "Follow") {
                // Nếu trạng thái là "Follow", xóa bản ghi khỏi Firestore (nghĩa là hủy theo dõi)
                try {
                    followsRef.whereEqualTo("followerId", currentUserId)
                        .whereEqualTo("followedId", userId)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            // Nếu tìm thấy bản ghi, xóa nó
                            querySnapshot.documents.forEach { document ->
                                followsRef.document(document.id).delete()
                                    .addOnSuccessListener {
                                        // Cập nhật trạng thái thành "Follow" sau khi xóa
                                        updateUserStatus(currentUserId, "Follow")
                                    }
                                    .addOnFailureListener { e ->
                                        _error.value = "Error unfollowing user: ${e.message}"
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            _error.value = "Error fetching follow data: ${e.message}"
                        }
                } catch (e: Exception) {
                    _error.value = "Error: ${e.message}"
                }
            } else if (relationshipStatus == "Unfollow") {
                // Nếu trạng thái là "Unfollow", thêm bản ghi vào Firestore (nghĩa là theo dõi)
                try {
                    val followData = Follow(followerId = currentUserId, followedId = userId)
                    val docId = "${followData.followerId}_${followData.followedId}" // Đảm bảo mỗi follow có ID duy nhất

                    followsRef.document(docId).set(followData)  // Sử dụng document ID duy nhất
                        .addOnSuccessListener {
                            // Cập nhật trạng thái thành "Unfollow"
                            updateUserStatus(currentUserId, "Unfollow")
                        }
                        .addOnFailureListener { e ->
                            _error.value = "Error following user: ${e.message}"
                        }
                } catch (e: Exception) {
                    _error.value = "Error: ${e.message}"
                }
            }
        }
    }

    private fun updateUserStatus(userId: String, status: String) {
        // Cập nhật trạng thái của người dùng trong danh sách
        _friendsList.value = _friendsList.value.map { userWithStatus ->
            if (userWithStatus.user.userId == userId) {
                userWithStatus.copy(status = status)
            } else {
                userWithStatus
            }
        }
        _followingList.value = _followingList.value.map { userWithStatus ->
            if (userWithStatus.user.userId == userId) {
                userWithStatus.copy(status = status)
            } else {
                userWithStatus
            }
        }
    }



    fun clearError() {
        _error.value = null
    }
}
