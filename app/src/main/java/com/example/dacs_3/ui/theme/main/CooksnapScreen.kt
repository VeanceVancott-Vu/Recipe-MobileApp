package com.example.dacs_3.ui.theme.main

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dacs_3.R
import com.example.dacs_3.cloudinary.imageupload.CloudinaryUploader
import com.example.dacs_3.model.Cooksnap
import com.example.dacs_3.model.User
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.viewmodel.CooksnapViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Camera
import compose.icons.fontawesomeicons.solid.EllipsisV
import kotlinx.coroutines.launch

@Composable
fun CooksnapScreen(
    navController: NavController,
    id: String,
    viewModel: CooksnapViewModel = viewModel(),
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()  // Coroutine scope để chạy trên Main thread

    var uploading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }

    // Launcher chọn ảnh từ gallery
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            uploading = true
            errorMessage = null

            CloudinaryUploader.uploadImageFromUri(
                context = context,
                uri = it,
                uploadPreset = "koylin_unsigned",
                onSuccess = { imageUrl ->
                    scope.launch {
                        uploading = false
                        uploadedImageUrl = imageUrl
                        val encodedImageUrl = Uri.encode(imageUrl)  // Mã hóa URL để dùng navigation
                        navController.navigate("share_cooksnap/$id/$encodedImageUrl")
                    }
                },
                onError = { e ->
                    scope.launch {
                        uploading = false
                        errorMessage = "Upload ảnh thất bại: ${e.message}"
                    }
                }
            )
        }
    }

    LaunchedEffect(id) {
        viewModel.loadCooksnaps(id)
    }

    // Quan sát state flow danh sách cooksnap
    val cooksnaps by viewModel.cooksnaps.collectAsState()

    // Quan sát state flow map userId -> User
    val usersMap by viewModel.usersMap.collectAsState()


    // Dữ liệu Fake
//    val sampleCooksnaps = generateFakeCooksnaps()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))

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
                    text = "Cooksnap"
                )

            }
        }

        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensionResource(R.dimen.spacing_m), end = dimensionResource(
                    R.dimen.spacing_m))

        ) {
            Row(
                modifier = Modifier
                    .background(
                        Color(0xFFEFEFEF),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Phần UI giữ nguyên, sửa lại icon camera:
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Camera,
                    contentDescription = "Camera",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            pickImageLauncher.launch("image/*")
                        },
                    tint = OliverGreen
                )

                // Hiển thị lỗi upload (nếu có)
                errorMessage?.let {
                    Text(text = it, color = Color.Red)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Share your Cooksnap",
                    fontSize = 16.sp
                )
            }
        }

        // Gọi hàm hiển thị Grid
        CooksnapGrid(
            cooksnapList = cooksnaps,
            usersMap = usersMap,
            navController = navController
        )

        // Hiển thị lỗi nếu có
        if (!errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }

    }
}


@Composable
fun CooksnapGrid(
    cooksnapList: List<Cooksnap>,
    usersMap: Map<String, User>,
    navController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_m)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m)),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        items(cooksnapList) { cooksnap ->
            val user = usersMap[cooksnap.userId]
            CooksnapItemCard(
                cooksnap = cooksnap,
                user = user,
                navController = navController)
        }
    }
}

@Composable
fun CooksnapItemCard(
    cooksnap: Cooksnap,
    user: User?, // có thể null nếu chưa load kịp
    navController: NavController
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF6F6F6)
        ),
        border = BorderStroke(1.dp, Color(0xFFBDB4B4)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.spacing_s)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3f / 5f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_s)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s))

        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cooksnap.imageResult)   // URL ảnh
                    .crossfade(true)
                    .build(),
                contentDescription = "Cooksnap Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)))
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (user?.profileImageUrl.isNullOrBlank()) {
                    // Hiển thị avatar mặc định hoặc placeholder nếu user hoặc avatar chưa có
                    Image(
                        painter = painterResource(id = R.drawable.loading), // bạn thay bằng icon phù hợp
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_medium))
                            .clip(CircleShape)
                    )
                } else {
                    AsyncImage(
                        model = user?.profileImageUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_medium))
                            .clip(CircleShape)
                    )
                }

                Text(
                    text = user?.username ?: "Unknown",
                    fontSize = 14.sp,
                    color = Color(0xFF6A6363)
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = FontAwesomeIcons.Solid.EllipsisV,
                    contentDescription = "Edit Cooksnap",
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_small))
                        .clickable {
                            val encodedImageUrl = Uri.encode(cooksnap.imageResult)
                            val encodedDescription = Uri.encode(cooksnap.description)
                            navController.navigate("edit_cooksnap/${cooksnap.cooksnapId}/$encodedImageUrl/$encodedDescription")
                        },
                    tint = OliverGreen
                )

            }

            Text(
                text = cooksnap.description,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF6A6363)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_s)))


        }
    }
}

@Composable
fun ButtonCard(
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
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = "Icon",
                modifier = Modifier.size(10.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = text,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D2D2D)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CooksnapScreenPreview() {
//    CooksnapScreen()
}
