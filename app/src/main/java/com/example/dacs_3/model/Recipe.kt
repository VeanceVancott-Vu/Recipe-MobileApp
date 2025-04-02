package com.example.dacs_3.model

data class Recipe (
    val recipe_id: String ="",
    val user_id: String ="", // map to the user who make the recipe
    val title: String ="",
    val description: String ="",
    val duration: String ="",
    val cuisine: String = "",
    val servings: Int = 0,
    val image_urls: List<String> = listOf(), // List of image URLs
    val created_at: Long = System.currentTimeMillis() // get time when created
)

