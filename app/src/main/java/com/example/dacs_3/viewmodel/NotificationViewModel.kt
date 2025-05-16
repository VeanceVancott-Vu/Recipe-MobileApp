package com.example.dacs_3.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.Notification
import com.example.dacs_3.model.NotificationWithActor
import com.example.dacs_3.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NotificationViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Cache user theo userId để tránh query nhiều lần
    private val userCache = mutableMapOf<String, User>()

    // MutableStateFlow giữ danh sách Notification + ActorUser
    private val _notificationsWithActors = MutableStateFlow<List<NotificationWithActor>>(emptyList())
    val notificationsWithActors: StateFlow<List<NotificationWithActor>> = _notificationsWithActors

    private var listenerRegistration: ListenerRegistration? = null

    init {
        val currentUserId = auth.currentUser?.uid ?: ""
        if (currentUserId.isNotEmpty()) {
            listenNotificationsRealtime(currentUserId)
        }
    }

    private fun listenNotificationsRealtime(recipientId: String) {
        listenerRegistration = firestore.collection("notifications")
            .whereEqualTo("recipientId", recipientId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("NotificationViewModel", "Listen failed.", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val notifications = snapshot.toObjects(Notification::class.java)

                    viewModelScope.launch {
                        // Lấy danh sách Notification + ActorUser
                        val listWithUsers = notifications.mapNotNull { notification ->
                            val actorId = notification.actorId

                            // Lấy User actor từ cache hoặc Firestore
                            val user = getUserByIdCached(actorId)
                            user?.let { NotificationWithActor(notification, it) }
                        }

                        _notificationsWithActors.value = listWithUsers
                    }
                } else {
                    _notificationsWithActors.value = emptyList()
                }
            }
    }

    // Lấy user với cache
    private suspend fun getUserByIdCached(userId: String): User? {
        // Nếu có cache thì trả về ngay
        userCache[userId]?.let { return it }

        return try {
            val doc = firestore.collection("users").document(userId).get().await()
            val user = if (doc.exists()) doc.toObject(User::class.java) else null
            if (user != null) {
                userCache[userId] = user
            }
            user
        } catch (e: Exception) {
            Log.e("NotificationViewModel", "Failed to get user $userId", e)
            null
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
