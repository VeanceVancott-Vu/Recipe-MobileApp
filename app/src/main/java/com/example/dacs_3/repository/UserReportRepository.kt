package com.example.dacs_3.repository

import com.example.dacs_3.model.UserReport
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserReportRepository {

    private val collection = FirebaseFirestore.getInstance().collection("user_reports")

    suspend fun submitUserReport(report: UserReport): Result<Unit> {
        return try {
            val updatedReport = report.copy(date = System.currentTimeMillis())
            collection.add(updatedReport).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchUserReportById(reportId: String): UserReport? {
        return try {
            val snapshot = collection.document(reportId).get().await()
            snapshot.toObject(UserReport::class.java)?.copy(id = snapshot.id)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteUserReport(reportId: String): Boolean {
        return try {
            collection.document(reportId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateUserReportStatus(reportId: String, newStatus: String): Boolean {
        return try {
            collection.document(reportId).update("status", newStatus).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun fetchAllUserReports(): List<UserReport> {
        return try {
            val snapshot = collection.get().await()
            snapshot.documents.mapNotNull { it.toObject(UserReport::class.java)?.copy(id = it.id) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
