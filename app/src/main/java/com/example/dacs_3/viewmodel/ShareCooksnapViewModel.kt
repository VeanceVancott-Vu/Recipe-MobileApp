package com.example.dacs_3.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.cloudinary.imageupload.CloudinaryUploader
import com.example.dacs_3.model.Cooksnap
import com.example.dacs_3.model.Notification
import com.example.dacs_3.model.NotificationType
import com.example.dacs_3.model.TargetType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ShareCooksnapViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _sending = MutableStateFlow(false)
    val sending: StateFlow<Boolean> = _sending.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success.asStateFlow()

    // Hàm suspend lấy userId chủ công thức từ Firestore
    suspend fun getRecipeOwnerId(recipeId: String): String {
        return try {
            val doc = firestore.collection("recipes").document(recipeId).get().await()
            if (doc.exists()) {
                doc.getString("userId") ?: ""
            } else {
                ""
            }
        } catch (e: Exception) {
            Log.e("ShareCooksnapViewModel", "Failed to get recipe ownerId", e)
            ""
        }
    }

    // Hàm tạo thông báo lên Firestore
    fun createNotification(notification: Notification, onComplete: () -> Unit = {}) {
        firestore.collection("notifications")
            .add(notification)
            .addOnSuccessListener {
                Log.d("ShareCooksnapViewModel", "Notification created successfully")
                onComplete()
            }
            .addOnFailureListener { e ->
                Log.e("ShareCooksnapViewModel", "Failed to create notification", e)
                onComplete()
            }
    }

    fun submitCooksnap(recipeId: String, imageUrl: String, description: String) {
        val userId = auth.currentUser?.uid
        if (userId.isNullOrBlank()) {
            _error.value = "User chưa đăng nhập"
            return
        }
        if (imageUrl.isBlank()) {
            _error.value = "Ảnh không hợp lệ"
            return
        }
        if (description.isBlank()) {
            _error.value = "Vui lòng nhập mô tả"
            return
        }

        _sending.value = true
        _error.value = null

        val newDoc = firestore.collection("cooksnaps").document()
        val cooksnap = Cooksnap(
            cooksnapId = newDoc.id,
            recipeId = recipeId,
            userId = userId,
            description = description,
            imageResult = imageUrl,
            hearts = 0,
            slaps = 0,
            smiles = 0
        )

        newDoc.set(cooksnap)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _sending.value = false
                    _success.value = true

                    // Lấy userId chủ công thức rồi tạo thông báo
                    val recipeOwnerId = getRecipeOwnerId(recipeId)
                    if (recipeOwnerId.isNotEmpty() && recipeOwnerId != userId) {
                        val notification = Notification(
                            recipientId = recipeOwnerId,
                            actorId = userId,
                            type = NotificationType.COOKSNAP_DELETED.name, // hoặc tạo enum mới COOKSNAP_NEW nếu cần
                            message = "Người ${userId} đã gửi một Cooksnap cho công thức của bạn.",
                            targetId = recipeId,
                            targetType = TargetType.RECIPE.name,
                            timestamp = System.currentTimeMillis(),
                            isRead = false
                        )
                        createNotification(notification)
                    }
                }
            }
            .addOnFailureListener { e ->
                _sending.value = false
                _error.value = "Lỗi gửi dữ liệu: ${e.message}"
            }
    }

    fun uploadImageFromUri(
        context: Context,
        uri: Uri,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        _sending.value = true
        _error.value = null

        CloudinaryUploader.uploadImageFromUri(
            context = context,
            uri = uri,
            uploadPreset = "koylin_unsigned",
            onSuccess = { imageUrl ->
                _sending.value = false
                onSuccess(imageUrl)
            },
            onError = { e ->
                _sending.value = false
                _error.value = "Upload ảnh thất bại: ${e.message}"
                onError(e)
            }
        )
    }


    fun reset() {
        _sending.value = false
        _error.value = null
        _success.value = false
    }
}
