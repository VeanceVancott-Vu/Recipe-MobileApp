package com.example.dacs_3.ui.theme.main.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.dacs_3.R
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.ui.theme.main.SectionTitle

@Composable
fun FollowerListScreen(
    viewModel: FriendListViewModel,
    currentUserId: String,
    targetUserId: String
) {
    val followingList by viewModel.followingList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Load data when the targetUserId changes
    LaunchedEffect(targetUserId) {
        viewModel.loadFriendAndFollowingLists(targetUserId, currentUserId)
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

            followingList.isEmpty() -> {
                Text(
                    text = "This user isn't following anyone yet.",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }

            else -> {
                LazyColumn {
                    items(followingList) { userWithStatus ->
                        UserCard(
                            userWithStatus = userWithStatus,
                            onFollowUnfollow = { status ->
                                viewModel.onFollowUnfollow(userWithStatus.user.userId, currentUserId, status)
                            }
                        )
                    }
                }

            }
        }
    }
}
