package com.example.dacs_3.model

data class Notification(
    val title: String,
    val content: String,
    val date: String,
    val avatar: String // Đây là URL của ảnh avatar hoặc tên tài nguyên hình ảnh
)
