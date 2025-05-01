package com.example.dacs_3.model

data class Cooksnap(
    val id: String,                // ID của Cooksnap (duy nhất)
    val recipeId: String,          // ID công thức mà Cooksnap này liên kết đến
    val userName: String,          // Tên người dùng gửi Cooksnap
    val userAvatarResId: Int,      // Avatar người dùng
    val imageResId: Int,           // Hình ảnh thành phẩm
    val description: String,       // Ghi chú thành phẩm
    val likes: Int,                // Số lượt thả tim
    val smiles: Int,               // Số lượt mặt cười
    val claps: Int,
    val createdAt: Long            // Thời gian gửi
)