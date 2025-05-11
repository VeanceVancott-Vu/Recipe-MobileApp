package com.example.dacs_3.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.Comment
import com.example.dacs_3.repository.CommentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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


    fun postComment(comment: Comment) {
        viewModelScope.launch {
            repository.addComment(comment) { success ->
                if (success) {
                    loadComments(comment.recipeId)
                } else {
                    _errorMessage.value = "Failed to post comment."
                }
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
                loadComments(recipeId) // Refresh the list after deletion
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete comment: ${e.message}"
            }
        }
    }

    fun updateComment(comment: Comment) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.updateComment(comment)
            _isLoading.value = false
            if (result.isSuccess) {
                loadComments(comment.recipeId) // Refresh the list
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

}