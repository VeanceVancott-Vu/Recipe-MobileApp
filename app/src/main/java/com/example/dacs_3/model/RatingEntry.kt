package com.example.dacs_3.model

data class RatingEntry(
    val userId: String = "",
    val stars: Float = 0f  // Cho phép số lẻ (ví dụ 3.5)
)

