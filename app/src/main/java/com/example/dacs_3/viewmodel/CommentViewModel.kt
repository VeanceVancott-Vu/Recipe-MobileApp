package com.example.dacs_3.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.Comment
import com.example.dacs_3.model.Notification
import com.example.dacs_3.model.NotificationType
import com.example.dacs_3.model.TargetType
import com.example.dacs_3.repository.CommentRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CommentViewModel : ViewModel() {
    private val repository = CommentRepository()

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadComments(recipeId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.getCommentsForRecipe(
                recipeId = recipeId,
                onSuccess = { list ->
                    _comments.value = list
                    _isLoading.value = false
                    Log.d("CommentViewModel", "Fetched comments: ${comments.value} from: $recipeId")
                },
                onFailure = { error ->
                    _errorMessage.value = error.message
                    _isLoading.value = false
                }
            )
        }
    }

    // Lấy userId chủ công thức bằng suspend function, dùng await() để chờ kết quả
    suspend fun getRecipeOwnerId(recipeId: String): String {
        return try {
            val doc = FirebaseFirestore.getInstance()
                .collection("recipes")
                .document(recipeId)
                .get()
                .await()
            if (doc.exists()) {
                doc.getString("userId") ?: ""
            } else {
                ""
            }
        } catch (e: Exception) {
            Log.e("CommentViewModel", "Failed to get recipe ownerId", e)
            ""
        }
    }

    // Hàm postComment chỉnh sửa để chạy đồng bộ logic và tạo notification
    fun postComment(comment: Comment) {
        viewModelScope.launch {
            val success = repository.addCommentSuspend(comment)
            if (success) {
                loadComments(comment.recipeId)

                val recipeOwnerId = getRecipeOwnerId(comment.recipeId)
                val actorId = comment.userId

                if (recipeOwnerId.isNotEmpty() && recipeOwnerId != actorId) {
                    val notification = Notification(
                        recipientId = recipeOwnerId,
                        actorId = actorId,
                        type = NotificationType.COMMENT_NEW.name,
                        message = "Người ${actorId} đã bình luận tại công thức ${comment.recipeId} của bạn.",
                        targetId = comment.recipeId,
                        targetType = TargetType.RECIPE.name,
                        timestamp = System.currentTimeMillis(),
                        isRead = false
                    )
                    createNotification(notification)
                }
            } else {
                _errorMessage.value = "Failed to post comment."
            }
        }
    }

    fun reportComment(commentId: String) {
        viewModelScope.launch {
            repository.reportComment(commentId) { success ->
                if (!success) {
                    _errorMessage.value = "Failed to report comment."
                }
            }
        }
    }

    fun deleteComment(commentId: String, recipeId: String) {
        viewModelScope.launch {
            try {
                repository.deleteComment(commentId)
                loadComments(recipeId)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete comment: ${e.message}"
            }
        }
    }

    fun deleteCommentByAdmin(commentId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                repository.deleteComment(commentId)
                onResult(true) // Success
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete comment: ${e.message}"
                onResult(false) // Failure
            }
        }
    }


    fun updateComment(comment: Comment) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.updateComment(comment)
            _isLoading.value = false
            if (result.isSuccess) {
                loadComments(comment.recipeId)
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun createNotification(notification: Notification, onComplete: () -> Unit = {}) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("notifications")
            .add(notification)
            .addOnSuccessListener {
                Log.d("CommentViewModel", "Notification created successfully")
                onComplete()
            }
            .addOnFailureListener { e ->
                Log.e("CommentViewModel", "Failed to create notification", e)
                onComplete()
            }
    }

    // CommentViewModel.kt
    private val _selectedComment = MutableStateFlow<Comment?>(null)
    val selectedComment: StateFlow<Comment?> = _selectedComment

    fun fetchCommentById(commentId: String) {
        viewModelScope.launch {
            val comment = repository.fetchCommentById(commentId)
            _selectedComment.value = comment
            if (comment == null) {
                _errorMessage.value = "Failed to fetch comment"
            }
        }
    }

}
