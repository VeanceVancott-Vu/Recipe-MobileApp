package com.example.dacs_3.ui.theme.main

import com.example.dacs_3.model.Notification
import com.example.dacs_3.model.NotificationType
import com.example.dacs_3.model.Recipe
import com.example.dacs_3.model.TargetType

import java.util.UUID
import kotlin.random.Random

fun generateFakeNotifications(count: Int = 10): List<Notification> {
    val notificationTypes = NotificationType.entries
    val targetTypes = TargetType.entries
    val fakeUsers = listOf("user1", "user2", "user3", "user4", "user5")
    val fakeMessages = mapOf(
        NotificationType.VOTE to listOf(
            "đã thích công thức của bạn",
            "đã vote cho món ăn của bạn",
            "thích bài đăng của bạn"
        ),
        NotificationType.COMMENT_NEW to listOf(
            "đã bình luận về công thức của bạn",
            "đã để lại bình luận mới",
            "bình luận về món ăn của bạn"
        ),
        NotificationType.RECIPE_DELETED to listOf(
            "Công thức của bạn đã bị xóa vì vi phạm quy định",
            "Bài đăng công thức đã bị xóa"
        ),
        NotificationType.COMMENT_DELETED to listOf(
            "Bình luận của bạn đã bị xóa vì nội dung không phù hợp",
            "Bình luận đã bị gỡ bỏ"
        ),
        NotificationType.COOKSNAP_DELETED to listOf(
            "Cooksnap của bạn đã bị xóa do vi phạm chính sách",
            "Hình ảnh cooksnap đã bị gỡ"
        )
    )

    return List(count) {
        val type = notificationTypes.random()
        val targetType = targetTypes.random()
        val actor = fakeUsers.random()
        val recipient = fakeUsers.random()
        val message = fakeMessages[type]?.random() ?: "Thông báo mới"
        val reason = if (type in listOf(NotificationType.RECIPE_DELETED, NotificationType.COMMENT_DELETED, NotificationType.COOKSNAP_DELETED)) {
            "Vi phạm chính sách cộng đồng"
        } else {
            ""
        }

        Notification(
            id = UUID.randomUUID().toString(),
            recipientId = recipient,
            actorId = actor,
            type = type.name.lowercase(),
            message = "$actor $message",
            targetId = UUID.randomUUID().toString(),
            targetType = targetType.name.lowercase(),
            timestamp = System.currentTimeMillis() - Random.nextLong(0, 7 * 24 * 60 * 60 * 1000), // Trong 7 ngày qua
            isRead = Random.nextBoolean(),
            reason = reason
        )
    }
}

fun generateFakeKitchen(): List<Recipe> {
    return listOf(
        Recipe(
            title = "Cơm chiên Dương Châu",
            resultImages = "https://www.example.com/image1.jpg", // Thay thế bằng đường dẫn hình ảnh thực tế
            likes = 150,
            smiles = 75,
            claps = 20
        ),
        Recipe(
            title = "Bánh mì kẹp thịt nướng",
            resultImages = "https://www.example.com/image2.jpg", // Thay thế bằng đường dẫn hình ảnh thực tế
            likes = 200,
            smiles = 50,
            claps = 30
        ),
        Recipe(
            title = "Sushi cuộn trứng",
            resultImages = "https://www.example.com/image3.jpg", // Thay thế bằng đường dẫn hình ảnh thực tế
            likes = 250,
            smiles = 100,
            claps = 40
        ),
        Recipe(
            title = "Mì Ý sốt bò bằm",
            resultImages = "https://www.example.com/image4.jpg", // Thay thế bằng đường dẫn hình ảnh thực tế
            likes = 180,
            smiles = 80,
            claps = 25
        ),
        Recipe(
            title = "Gà rán giòn",
            resultImages = "https://www.example.com/image5.jpg", // Thay thế bằng đường dẫn hình ảnh thực tế
            likes = 300,
            smiles = 120,
            claps = 60
        )
    )
}

