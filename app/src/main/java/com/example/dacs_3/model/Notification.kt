    package com.example.dacs_3.model

    data class Notification(
        val id: String = "", // ID duy nhất của thông báo (tạo bởi Firestore hoặc UUID).
        val recipientId: String = "", // ID của user nhận thông báo (liên kết với User.userId).
        val actorId: String = "", // ID của user thực hiện hành động (liên kết với User.userId).
        val type: String = "", // Loại thông báo (vote, comment_new, recipe_deleted, comment_deleted, cooksnap_deleted).
        val message: String = "", // Nội dung thông báo (ví dụ: “Người A đã vote công thức của bạn”).
        val targetId: String = "", // ID của đối tượng liên quan (công thức, bình luận, hoặc cooksnap).
        val targetType: String = "", // Loại đối tượng (recipe, comment, cooksnap) để biết targetId thuộc về gì.
        val timestamp: Long = System.currentTimeMillis(), // Thời gian tạo thông báo (dùng Long để lưu millisecond).
        val isRead: Boolean = false, // Trạng thái đã đọc (true/false, mặc định false).
        val reason: String = "" // Lý do xóa (nếu có, dùng cho các thông báo liên quan đến xóa, mặc định rỗng).
    )

    enum class NotificationType {
        VOTE, COMMENT_NEW, RECIPE_DELETED, COMMENT_DELETED, COOKSNAP_DELETED
    }

    enum class TargetType {
        RECIPE, COMMENT, COOKSNAP
    }
