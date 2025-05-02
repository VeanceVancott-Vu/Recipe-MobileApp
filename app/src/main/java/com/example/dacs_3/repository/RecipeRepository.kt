package com.example.dacs_3.repository

import com.example.dacs_3.model.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class RecipeRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val recipeCollection = firestore.collection("recipes")

    fun addRecipe(recipe: Recipe, onResult: (Boolean) -> Unit) {
        recipeCollection.document(recipe.recipeId)
            .set(recipe)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getRecipes(
        onSuccess: (List<Recipe>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        recipeCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
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
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val recipes = snapshot.toObjects(Recipe::class.java)
                onChange(recipes)
            }
    }
}