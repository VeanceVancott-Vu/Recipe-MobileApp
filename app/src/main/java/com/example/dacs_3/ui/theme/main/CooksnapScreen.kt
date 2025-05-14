package com.example.dacs_3.ui.theme.main

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dacs_3.R
import com.example.dacs_3.model.Cooksnap
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.viewmodel.AuthViewModel
import com.example.dacs_3.viewmodel.RecipeViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Camera
import compose.icons.fontawesomeicons.solid.EllipsisV

@Composable
fun CooksnapScreen(
    navController: NavController,
    id: String,
    recipeViewModel: RecipeViewModel = viewModel(),
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {

    // Dữ liệu Fake
    val sampleCooksnaps = generateFakeCooksnaps()

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
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Camera,
                    contentDescription = "Camera",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            navController.navigate("share_cooksnap/$id")
                        },
                    tint = OliverGreen
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Share your Cooksnap",
                    fontSize = 16.sp
                )
            }
        }

        // Gọi hàm hiển thị Grid
        CooksnapGrid(cooksnapList = sampleCooksnaps)

    }
}


@Composable
fun CooksnapGrid(
    cooksnapList: List<Cooksnap>
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
            CooksnapItemCard(cooksnap)
        }
    }
}

@Composable
fun CooksnapItemCard(cooksnap: Cooksnap) {
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
            Image(
                painter = painterResource(id = cooksnap.imageResId),
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
                Image(
                    painter = painterResource(id = cooksnap.userAvatarResId),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_medium))
                        .clip(CircleShape) //
                )

                Text(
                    text = cooksnap.userName,
                    fontSize = 14.sp,
                    color = Color(0xFF6A6363)
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

            Row(
                 horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonCard(
                    text = cooksnap.likes.toString(),
                    iconResId = R.drawable.heart
                )

                ButtonCard(
                    text = cooksnap.claps.toString(),
                    iconResId = R.drawable.slap
                )

                ButtonCard(
                    text = cooksnap.smiles.toString(),
                    iconResId = R.drawable.smile
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = FontAwesomeIcons.Solid.EllipsisV,
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_small)),
                    tint = OliverGreen
                )

            }
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
