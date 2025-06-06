package com.example.dacs_3.repository

import com.example.dacs_3.model.Comment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.coroutines.resume

class CommentRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val commentsCollection = firestore.collection("comments")

//    fun addComment(comment: Comment, onResult: (Boolean) -> Unit) {
//        val commentId = UUID.randomUUID().toString()
//        val newComment = comment.copy(commentId = commentId)
//        commentsCollection.document(commentId)
//            .set(newComment)
//            .addOnSuccessListener { onResult(true) }
//            .addOnFailureListener { onResult(false) }
//    }

    // CommentRepository.kt
    suspend fun fetchCommentById(commentId: String): Comment? {
        return try {
            val snapshot = FirebaseFirestore.getInstance()
                .collection("comments")
                .document(commentId)
                .get()
                .await()

            snapshot.toObject(Comment::class.java)?.copy(commentId = snapshot.id)
        } catch (e: Exception) {
            null
        }
    }


    suspend fun addCommentSuspend(comment: Comment): Boolean = suspendCancellableCoroutine { cont ->
        val commentId = java.util.UUID.randomUUID().toString()
        val newComment = comment.copy(commentId = commentId)
        commentsCollection.document(commentId)
            .set(newComment)
            .addOnSuccessListener {
                cont.resume(true)
            }
            .addOnFailureListener { e ->
                cont.resume(false)
            }
    }

    suspend fun deleteComment(commentId: String) {
        try {
            FirebaseFirestore.getInstance()
                .collection("comments")
                .document(commentId)
                .delete()
                .await()
        } catch (e: Exception) {
            // Log or handle the error
            throw e
        }
    }

    suspend fun updateComment(comment: Comment): Result<Unit> {
        return try {
            firestore.collection("comments")
                .document(comment.commentId)
                .set(comment) // Overwrites the whole document with updated data
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    fun getCommentsForRecipe(recipeId: String, onSuccess: (List<Comment>) -> Unit, onFailure: (Exception) -> Unit) {
        commentsCollection.whereEqualTo("recipeId", recipeId)
            .get()
            .addOnSuccessListener { result ->
                val comments = result.documents.mapNotNull { it.toObject(Comment::class.java) }
                onSuccess(comments)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun reportComment(commentId: String, onResult: (Boolean) -> Unit) {
        commentsCollection.document(commentId)
            .update("isReported", true)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
}
