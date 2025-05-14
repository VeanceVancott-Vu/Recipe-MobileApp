package com.example.dacs_3.repository

import com.example.dacs_3.model.SearchHistory
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class SearchHistoryRepository {

    private val db = FirebaseFirestore.getInstance()
    private val historyRef = db.collection("search_history")


    suspend fun addSearchHistory(userId: String, query: String) {
        val newEntry = SearchHistory(userId = userId, query = query)
        historyRef.add(newEntry).await()
    }

    suspend fun getSearchHistory(userId: String): List<SearchHistory> {
        return try {
            historyRef
                .whereEqualTo("userId", userId)
              // .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(SearchHistory::class.java)?.copy(id = it.id) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deleteSearchHistoryItem(documentId: String) {
        historyRef.document(documentId).delete().await()
    }

    suspend fun clearAllHistory(userId: String) {
        val snapshot = historyRef.whereEqualTo("userId", userId).get().await()
        snapshot.documents.forEach { it.reference.delete().await() }
    }
}
