package com.example.dacs_3.model

data class Recipe(
    val recipeId: String = "",           // Thêm ID để quản lý công thức
    var title: String = "",
    var story: String = "",
    var servingSize: String = "",
    var cookingTime: String = "",
    var resultImages: String = "",
    var ingredients: List<String> = emptyList(),
    var instructions: List<Instruction> = emptyList(),
    var likes: Int = 0,                // Số lượt thả tim
    var smiles: Int = 0,               // Số lượt mặt cười
    var claps: Int = 0,
    var userId: String = "",              // Thêm userId để liên kết với tác giả
    var ratings: List<RatingEntry> = emptyList(),
    val averageRating: Float = 0f,
    var cooksnaps: List<Cooksnap> = emptyList()
)

