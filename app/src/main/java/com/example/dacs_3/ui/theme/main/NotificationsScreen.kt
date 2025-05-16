package com.example.dacs_3.ui.theme.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.dacs_3.R
import com.example.dacs_3.model.Notification
import com.example.dacs_3.model.Recipe
import com.example.dacs_3.ui.theme.OliverGreen
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Bookmark
import compose.icons.fontawesomeicons.solid.EllipsisV
import compose.icons.fontawesomeicons.solid.PaperPlane
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationsAndKitchenBuddies(
    navController: NavController
) {
    // Khởi tạo pager state với số lượng tab
    val pagerState = rememberPagerState(pageCount = { 2 })
    val tabs = listOf("Notifications", "Kitchen Buddies")
    val coroutineScope = rememberCoroutineScope()

    // Tạo danh sách dữ liệu giả
    val notifications = generateFakeNotifications()
    val kitchenbuddies = generateFakeKitchen()

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
                    .padding(start = dimensionResource(R.dimen.spacing_m), end = dimensionResource(
                        R.dimen.spacing_m)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))

            ) {
                // ← Back icon
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable {
                            navController.popBackStack() // Quay lại màn hình trước (SearchScreen)
                        },
                    tint = OliverGreen
                )

                SectionTitle(
                    text = "Activity"
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.spacing_m),
                    end = dimensionResource(R.dimen.spacing_m)
                )
        ) {

            // Tab bar
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                            .height(4.dp)
                            .background(OliverGreen)
                    )
                }
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            // Chuyển tab mượt mà bằng coroutine
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = tab,
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    color = if (pagerState.currentPage == index) OliverGreen else Color.Gray
                                )
                            )
                        }
                    )
                }
            }


            // Pager content
            HorizontalPager(
                state = pagerState, // Trạng thái của Pager
                modifier = Modifier.fillMaxSize(), // Điều chỉnh chiều rộng và chiều cao của Pager
                flingBehavior = PagerDefaults.flingBehavior(state = pagerState) // Điều chỉnh hành vi cuộn khi người dùng vuốt
            ) { page ->
                when (page) {
                    0 -> NotificationContent(notifications = notifications) // Truyền danh sách vào
                    1 -> KitchenBuddiesContent(kitchenbuddies = kitchenbuddies)
                }
            }
        }
    }
}

@Composable
fun NotificationContent(notifications: List<Notification>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.spacing_xl)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_l))
    ) {
        items(notifications) { notification ->
            NotificationCard(notification = notification)
        }
    }
}


@Composable
fun NotificationCard(
    notification: Notification
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.spacing_s)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFf6f9f7) // Thay đổi màu nền của Card ở đây
        )
    ) {
        Row(modifier = Modifier.padding(dimensionResource(R.dimen.spacing_m))) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = "",
                    placeholder = painterResource(R.drawable.loading),
                    error = painterResource(R.drawable.uploadfailed)
                ),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape) // Cắt hình ảnh thành hình tròn
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_m)))

            Column {
                Text(
                    text = notification.type,
                    color = OliverGreen
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_s)))

                Text(
                    text = notification.message,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_m)))

                Text(
                    text = notification.timestamp.toString(),
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun KitchenBuddiesContent(
    kitchenbuddies: List<Recipe>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.spacing_xl)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xl))
    ) {
        items(kitchenbuddies) { kitchenbuddy ->
            KitchenBuddiesCard(kitchenbuddy = kitchenbuddy)
        }
    }
}

@Composable
fun KitchenBuddiesCard(
    kitchenbuddy: Recipe
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF) // màu trắng
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
        ) {
            Image(
                painter = painterResource(R.drawable.mockrecipeimage),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(R.dimen.spacing_m), end = dimensionResource(
                        R.dimen.spacing_m))
                ,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_large))
                        .clip(CircleShape) //
                )

                Text(
                    text = "Leonie",
                    fontSize = 16.sp,
                    color = Color(0xFF6A6363)
                )

                Spacer(modifier = Modifier.weight(1f))

                Card(
                    modifier = Modifier,
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_large)),
                        colors = IconButtonDefaults
                            .iconButtonColors(containerColor = Color(0xFFDBE6DE))

                    ) {
                        Icon(
                            imageVector = FontAwesomeIcons.Solid.Bookmark,
                            contentDescription = "",
                            tint = Color(0xFF3F764E),
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.icon_size_small))
                        )
                    }
                }



                Card(
                    modifier = Modifier,
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_large)),
                        colors = IconButtonDefaults
                            .iconButtonColors(containerColor = Color(0xFFDBE6DE))

                    ) {
                        Icon(
                            imageVector = FontAwesomeIcons.Solid.EllipsisV,
                            contentDescription = "",
                            tint = Color(0xFF3F764E),
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.icon_size_small))
                        )
                    }
                }

            }

            Column(
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.spacing_m), end = dimensionResource(
                        R.dimen.spacing_m)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))

            ) {
                Text(
                    text = kitchenbuddy.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LikeSlapSmile(
                        text = kitchenbuddy.likes.toString(),
                        iconResId = R.drawable.heart
                    )

                    LikeSlapSmile(
                        text = kitchenbuddy.claps.toString(),
                        iconResId = R.drawable.slap
                    )

                    LikeSlapSmile(
                        text = kitchenbuddy.smiles.toString(),
                        iconResId = R.drawable.smile
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_s)))

                HorizontalDivider(
                    modifier = Modifier.weight(1f)
                )

                CommentInput(onSend = { })

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_s)))

            }
        }
    }
}


@Composable
fun LikeSlapSmile(
    text: String,
    @DrawableRes iconResId: Int
) {
    Card(
        shape = RoundedCornerShape(50),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFAFA)
        ),
        modifier = Modifier.wrapContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = "Icon",
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small))
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D2D2D)
            )
        }
    }
}

@Composable
fun CommentInput(
    modifier: Modifier = Modifier,
    onSend: (String) -> Unit
) {
    var commentText by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(50))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            if (commentText.isEmpty()) {
                Text(
                    text = "Add comment …",
                    color = Color.DarkGray
                )
            }
            BasicTextField(
                value = commentText,
                onValueChange = { commentText = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                cursorBrush = SolidColor(Color.Black)
            )
        }

        IconButton(
            onClick = {
                if (commentText.isNotBlank()) {
                    onSend(commentText)
                    commentText = ""
                }
            }
        ) {
            Icon(
                imageVector = FontAwesomeIcons.Solid.PaperPlane,
                contentDescription = "Send",
                tint = OliverGreen,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_medium))
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
private fun NotificationsAndKitchenBuddiesPreview() {
//    NotificationsAndKitchenBuddies()
}