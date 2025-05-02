package com.example.dacs_3.model

//data class Recipe (
//    val recipe_id: String ="",
//    val user_id: String ="", // map to the user who make the recipe
//    val title: String ="",
//    val description: String ="",
//    val duration: String ="",
//    val cuisine: String = "",
//    val servings: Int = 0,
//    val image_urls: List<String> = listOf(), // List of image URLs
//    val created_at: Long = System.currentTimeMillis() // get time when created
//)

// Q:
data class Recipe(
    val recipeId: String = "",           // Thêm ID để quản lý công thức
    val title: String = "",
    val story: String = "",
    val servingSize: String = "",
    val cookingTime: String = "",
    val resultImages: String = "",
    val ingredients: List<String> = emptyList(),
    val instructions: List<Instruction> = emptyList(),
    val likes: Int = 0,                // Số lượt thả tim
    val smiles: Int = 0,               // Số lượt mặt cười
    val claps: Int = 0,
    val userId: String = ""              // Thêm userId để liên kết với tác giả
)

