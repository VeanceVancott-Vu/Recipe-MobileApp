package com.example.dacs_3.repository

import com.example.dacs_3.model.Comment
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class CommentRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val commentsCollection = firestore.collection("comments")

    fun addComment(comment: Comment, onResult: (Boolean) -> Unit) {
        val commentId = UUID.randomUUID().toString()
        val newComment = comment.copy(commentId = commentId)
        commentsCollection.document(commentId)
            .set(newComment)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getCommentsForRecipe(recipeId: String, onSuccess: (List<Comment>) -> Unit, onFailure: (Exception) -> Unit) {
        commentsCollection.whereEqualTo("recipeId", recipeId)
            .orderBy("timestamp")
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
