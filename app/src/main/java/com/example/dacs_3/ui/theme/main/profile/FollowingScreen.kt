package com.example.dacs_3.ui.theme.main.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dacs_3.R
import com.example.dacs_3.model.User
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.ui.theme.main.SectionTitle

@Composable
fun FollowingScreen(
    userId: String,
    viewModel: ProfileViewModel = viewModel()
) {
    val followingUsers by viewModel.oneWayFollowing.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val tnn = userId

    LaunchedEffect(Unit) {
        viewModel.loadFollowingAndBuddies(tnn) // user đang đăng nhập
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.height_top_bar)),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(R.dimen.spacing_m), end = dimensionResource(
                            R.dimen.spacing_m
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))

            ) {
                // ← Back icon
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { }, // Thêm sự kiện click
                    tint = OliverGreen
                )

                SectionTitle(
                    text = "Following"
                )
            }
        }

        when {
            isLoading -> {
                Text(
                    text = "Loading data...",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }

            error != null -> {
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            followingUsers.isEmpty() -> {
                Text(
                    text = "You haven't added any friends yet.",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }

            else -> {
                LazyColumn {
                    items(followingUsers, key = { it.userId }) { user ->
                        UserCard(user = user, currentUserId = tnn) { unfollowedUser ->
                            viewModel.unfollowUser(tnn, unfollowedUser.userId)
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(
    user: User,
    currentUserId: String,
    onUnfollow: (User) -> Unit
) {
    val isUnfollowed = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.spacing_m), vertical = dimensionResource(R.dimen.spacing_s)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.spacing_m)),
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.spacing_s))
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.spacing_m)),

        ) {
            // Avatar
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.profileImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.loading),
                error = painterResource(R.drawable.uploadfailed),
                fallback = painterResource(R.drawable.loading),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_lg_medium))
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Text info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF2F5946)
                )

                Text(
                    text = "@${user.userId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.dining),
                        contentDescription = "Dishes",
                        tint = Color(0xFF2F5946),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "25 Dishes",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // Button
            Button(
                onClick = {
                    isUnfollowed.value = true
                    onUnfollow(user)
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43704F)),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (isUnfollowed.value) "Follow" else "Unfollow",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}


