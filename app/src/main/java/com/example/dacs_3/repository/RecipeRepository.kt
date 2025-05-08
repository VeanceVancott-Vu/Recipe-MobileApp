package com.example.dacs_3.repository

import com.example.dacs_3.model.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

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
}