package com.example.dacs_3.model

data class Follow(
    val followerId: String = "",
    val followedId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
