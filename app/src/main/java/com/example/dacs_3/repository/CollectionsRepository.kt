package com.example.dacs_3.repository

import com.example.dacs_3.model.Collections
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CollectionsRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val collectionRef = firestore.collection("collection")

    suspend fun addCollection(collection: Collections): Result<Unit> {
        return try {
            val docRef = collectionRef.document()
            val newCollection = collection.copy(id = docRef.id)
            docRef.set(newCollection).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCollectionsByUser(userId: String): Result<List<Collections>> {
        return try {
            val snapshot = collectionRef
                .whereEqualTo("userId", userId)
                .get()
                .await()
            val collections = snapshot.toObjects(Collections::class.java)
            Result.success(collections)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCollection(collectionId: String): Result<Unit> {
        return try {
            collectionRef.document(collectionId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCollectionRecipes(collectionId: String, recipeIds: List<String>): Result<Unit> {
        return try {
            collectionRef.document(collectionId).update("recipeIds", recipeIds).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
