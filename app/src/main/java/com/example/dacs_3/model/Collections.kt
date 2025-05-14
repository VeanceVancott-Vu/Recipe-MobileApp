package com.example.dacs_3.model

data class Collections
    (
    val id: String = "", // Firestore document ID
    val userId: String = "", // Owner of the collection
    val name: String = "", // Collection name (e.g. "Desserts")
    val recipeIds: List<String> = emptyList() // IDs of recipes in this collection
)
