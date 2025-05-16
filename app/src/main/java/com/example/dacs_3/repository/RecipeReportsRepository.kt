package com.example.dacs_3.repository

import com.example.dacs_3.model.RecipeReport
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RecipeReportsRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val reportCollection = firestore.collection("recipeReports")

    suspend fun submitReport(report: RecipeReport): Boolean {
        return try {
            reportCollection.add(report).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getReportsByUser(userId: String): List<RecipeReport> {
        return try {
            val snapshot = reportCollection
                .whereEqualTo("reportingUserId", userId)
                .get()
                .await()
            snapshot.toObjects(RecipeReport::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAllReports(): List<RecipeReport> {
        return try {
            val snapshot = reportCollection
                .orderBy("date")
                .get()
                .await()
            snapshot.toObjects(RecipeReport::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateReportStatus(reportId: String, newStatus: String): Boolean {
        return try {
            reportCollection.document(reportId)
                .update("status", newStatus)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}