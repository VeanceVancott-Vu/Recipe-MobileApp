package com.example.dacs_3.ui.theme.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OtherUserProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        UserProfileHeader()

        UserBio()

        UserStats()

        FollowButton()

        ActionIcons()
    }
}

@Composable
fun UserProfileHeader() {
    // Hiển thị avatar, tên người dùng và thông tin vị trí
}

@Composable
fun UserBio() {
    // Hiển thị mô tả của người dùng
}

@Composable
fun UserStats() {
    // Hiển thị số lượng bạn bè và người theo dõi
}

@Composable
fun FollowButton() {
    // Hiển thị và xử lý hành động nút Follow
}

@Composable
fun ActionIcons() {
    // Hiển thị các biểu tượng hành động như chia sẻ hoặc quay lại
}

@Preview(showBackground = true)
@Composable
fun OtherUserProfileScreenPreview() {
    OtherUserProfileScreen()
}