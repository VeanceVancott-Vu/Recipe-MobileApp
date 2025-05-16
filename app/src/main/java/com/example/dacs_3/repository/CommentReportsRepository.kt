package com.example.dacs_3.repository

import com.example.dacs_3.model.CommentReport
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CommentReportsRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val reportCollection = firestore.collection("comment_reports")

    suspend fun submitCommentReport(report: CommentReport): Result<Unit> {
        return try {
            val updatedReport = report.copy(date = System.currentTimeMillis())
            reportCollection.add(updatedReport).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReportsByUser(userId: String): Result<List<CommentReport>> {
        return try {
            val snapshot = reportCollection
                .whereEqualTo("reportingUserId", userId)
                .get()
                .await()

            val reports = snapshot.toObjects(CommentReport::class.java)
            Result.success(reports)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllReports(): Result<List<CommentReport>> {
        return try {
            val snapshot = reportCollection.get().await()
            val reports = snapshot.toObjects(CommentReport::class.java)
            Result.success(reports)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateReportStatus(reportId: String, newStatus: String): Result<Unit> {
        return try {
            reportCollection.document(reportId)
                .update("status", newStatus)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}