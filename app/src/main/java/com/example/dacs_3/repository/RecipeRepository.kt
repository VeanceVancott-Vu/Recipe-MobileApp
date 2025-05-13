package com.example.dacs_3.repository

import android.util.Log
import com.example.dacs_3.model.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class RecipeRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val recipeCollection = firestore.collection("recipes")
     private val currentUser = FirebaseAuth.getInstance().currentUser

    fun addRecipe(recipe: Recipe, onResult: (Boolean) -> Unit) {
        val userId = currentUser?.uid ?: ""
        recipe.userId = userId

        recipeCollection
            .add(recipe)
            .addOnSuccessListener { documentRef ->
                val recipeId = documentRef.id
                // Update the same document with its generated ID
                documentRef.update("recipeId", recipeId)
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { onResult(false) }
            }
            .addOnFailureListener { onResult(false) }
    }



    fun getRecipes(
        onSuccess: (List<Recipe>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        recipeCollection
         //   .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val recipes = snapshot.toObjects(Recipe::class.java)
                onSuccess(recipes)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun observeRecipes(onChange: (List<Recipe>) -> Unit) {
        recipeCollection
      //      .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val recipes = snapshot.toObjects(Recipe::class.java)
                onChange(recipes)
            }
    }

    fun getRecipeById(
        recipeId: String,
        onSuccess: (Recipe?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        recipeCollection
            .document(recipeId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val recipe = document.toObject(Recipe::class.java)
                    onSuccess(recipe)
                } else {
                    onSuccess(null) // No such document
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }

    }

    fun updateRecipe(recipe: Recipe, onResult: (Boolean) -> Unit) {
        // Kiểm tra xem recipeId có rỗng không, nếu có thì không thể update được
        if (recipe.recipeId.isBlank()) {
            Log.e("RecipeRepository", "Không thể cập nhật: thiếu recipeId")
            onResult(false)
            return
        }

        // Cập nhật toàn bộ document với đối tượng mới
        recipeCollection.document(recipe.recipeId)
            .set(recipe) // Có thể dùng .update(...) nếu chỉ muốn cập nhật một số trường
            .addOnSuccessListener {
                Log.d("RecipeRepository", "Cập nhật công thức thành công")
                onResult(true)
            }
            .addOnFailureListener { e ->
                Log.e("RecipeRepository", "Lỗi cập nhật công thức: ${e.message}", e)
                onResult(false)
            }
    }

}