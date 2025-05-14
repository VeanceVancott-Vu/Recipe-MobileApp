package com.example.dacs_3.model

data class Cooksnap(
    val cooksnapId: String = "",      // ID riêng cho mỗi Cooksnap
    val recipeId: String = "",        // ID món ăn liên quan
    val userId: String = "",          // ID người đăng (liên kết User)
    val description: String = "",     // Mô tả trải nghiệm
    val imageResult: String = "",
    var hearts: Int = 0,              // Số lượt icon heart
    var slaps: Int = 0,               // Số lượt icon slap
    var smiles: Int = 0               // Số lượt icon mặt cười
)
