package com.example.dacs_3.ui.theme

import android.util.Log
import com.example.dacs_3.model.Instruction
import com.example.dacs_3.model.Recipe
import com.google.firebase.firestore.FirebaseFirestore


fun uploadSampleRecipeToFirestore() {
    val firestore = FirebaseFirestore.getInstance()

    val sampleRecipe = Recipe(
        recipeId = "ov6Mbw2ONvGYpJXuGn8R",
        title = "Green Onion Fried Eggs",
        story = "A simple yet flavorful dish that reminds me of childhood mornings.",
        servingSize = "2 servings",
        cookingTime = "10 minutes",
        resultImages = "https://i.pinimg.com/736x/e0/96/93/e09693529e71a5aa3f9d62854afa0a82.jpg",
        ingredients = listOf(
            "2 eggs",
            "1 stalk of green onion",
            "1 tsp fish sauce",
            "a pinch of black pepper"
        ),
        instructions = listOf(
            Instruction(
                stepNumber = 1,
                description = "Crack the eggs into a bowl, add fish sauce and pepper, and beat well.",
                imageUrl = listOf("https://i.pinimg.com/736x/e0/96/93/e09693529e71a5aa3f9d62854afa0a82.jpg")
            ),
            Instruction(
                stepNumber = 2,
                description = "Finely chop the green onion and mix it into the beaten eggs.",
                imageUrl = listOf()
            ),
            Instruction(
                stepNumber = 3,
                description = "Heat a pan, pour in the mixture, and fry until golden on both sides.",
                imageUrl = listOf("https://i.pinimg.com/736x/e0/96/93/e09693529e71a5aa3f9d62854afa0a82.jpg")
            )
        ),
        likes = 10,
        smiles = 5,
        claps = 3,
        userId = "user_test_001"
    )

    firestore.collection("recipes")
        .document(sampleRecipe.recipeId)
        .set(sampleRecipe)
        .addOnSuccessListener {
            Log.d("Firestore", "Sample recipe uploaded successfully.")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error uploading sample recipe", e)
        }
}