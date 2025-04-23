package com.example.dacs_3.model

data class Comment(
    val commentId: String = "",          // ID duy nhất cho mỗi bình luận
    val recipeId: String = "",           // ID công thức mà bình luận thuộc về
    val userId: String = "",             // ID người dùng (để liên kết với profile hoặc kiểm tra quyền)
    val username: String = "",           // Tên hiển thị của người dùng
    val text: String = "",               // Nội dung bình luận
    val timestamp: Long = System.currentTimeMillis(), // Thời điểm đăng (dạng millis để dễ sắp xếp)
    val isReported: Boolean = false      // Trạng thái có bị báo cáo hay không
)
