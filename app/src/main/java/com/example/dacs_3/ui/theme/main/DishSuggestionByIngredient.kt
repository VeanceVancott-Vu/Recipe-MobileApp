package com.example.dacs_3.ui.theme.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.dacs_3.R
import com.example.dacs_3.model.Recipe
import com.example.dacs_3.ui.theme.OliverGreen
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Bookmark
import compose.icons.fontawesomeicons.solid.SlidersH

@Composable
fun DishSuggestionByIngredient(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onSearch: (String) -> Unit = {},
    onFilter: () -> Unit = {}
) {
    var query by remember { mutableStateOf("") }

    val dishes: List<Recipe> = listOf(
        Recipe(
            title = "The Secret to Perfectly Crispy Fries",
            resultImages = "https://example.com/image1.jpg",
            ingredients = listOf("Potatoes", "Oil", "Salt", "Paprika", "Garlic Powder")
        ),
        Recipe(
            title = "Creamy Mushroom Soup",
            resultImages = "https://example.com/image2.jpg",
            ingredients = listOf("Mushrooms", "Cream", "Garlic", "Onion", "Thyme", "Butter")
        ),
        Recipe(
            title = "Classic Spaghetti Bolognese",
            resultImages = "https://example.com/image3.jpg",
            ingredients = listOf("Spaghetti", "Ground Beef", "Tomato Sauce", "Garlic", "Basil", "Onion")
        ),
        Recipe(
            title = "Vegetable Stir-Fry",
            resultImages = "https://example.com/image4.jpg",
            ingredients = listOf("Bell Pepper", "Carrots", "Broccoli", "Soy Sauce", "Garlic", "Ginger")
        ),
        Recipe(
            title = "Lemon Garlic Shrimp Pasta",
            resultImages = "https://example.com/image5.jpg",
            ingredients = listOf("Shrimp", "Spaghetti", "Lemon", "Garlic", "Olive Oil", "Parsley")
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                WindowInsets.systemBars.asPaddingValues()  // Bao gồm status bar + navigation bar
            ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
    ) {
        // Thanh tìm kiếm và các biểu tượng
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.height_top_bar)),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.spacing_m)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .clickable { onBack() },
                        tint = OliverGreen
                    )

                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium))
                    ) {
                        val containerColor = Color(0xFFDBE6DE)
                        TextField(
                            value = query,
                            onValueChange = {
                                query = it
                                onSearch(it)
                            },
                            placeholder = {
                                Text(text = "Enter ingredient names …")
                            },
                            singleLine = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium)),
                                    tint = OliverGreen
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = containerColor,
                                unfocusedContainerColor = containerColor,
                                disabledContainerColor = containerColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)),
                            modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth()
                        )
                    }

                    Icon(
                        imageVector = FontAwesomeIcons.Solid.SlidersH,
                        contentDescription = "Filter",
                        modifier = Modifier
                            .clickable(onClick = onFilter)
                            .size(dimensionResource(R.dimen.icon_size_medium)),
                        tint = OliverGreen
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // Tiêu đề
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.spacing_m))
            ) {
                SectionTitle("Newest Dishes")
            }


        }

        // Danh sách món ăn
        items(dishes) { dish ->
            DishCard(dish = dish)
        }

        // Carousel món ăn tương tự
        item {
            SimilarDishesCarousel()
        }
    }
}


@Composable
fun DishCard(
    dish: Recipe
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.spacing_m))
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)),
            modifier = Modifier
                .height(170.dp) // Chiều cao cố định cho Card
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                // Cột bên trái: Tên món ăn và nguyên liệu
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.spacing_s))
                        .weight(1f), // Column chiếm không gian còn lại trong Row
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
                ) {
                    Text(
                        text = dish.title,
                        fontSize = 16.sp,
                        color = Color(0xFF0B3D1F)
                    )

                    val ingredientsText = dish.ingredients.joinToString(separator = ", ")
                    Text(
                        text = ingredientsText,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Người đăng
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_background),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.icon_size_medium))
                                .clip(CircleShape)
                        )
                        Text(
                            text = "Jamie Lark",
                            fontSize = 14.sp,
                            color = Color(0xFF6A6363)
                        )
                    }
                }

                // Cột bên phải: Hình ảnh món ăn và icon Bookmark
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(150.dp) // Hạn chế kích thước của hình ảnh
                        .padding(dimensionResource(R.dimen.spacing_s))
                ) {
                    // Hình ảnh món ăn
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = dish.resultImages,
                            placeholder = painterResource(R.drawable.loading),
                            error = painterResource(R.drawable.uploadfailed)
                        ),
                        contentDescription = "Food Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(170.dp)
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium))) // Bo góc ảnh
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd) // Căn chỉnh ở góc phải dưới
                            .padding(dimensionResource(R.dimen.spacing_s)) // Tạo padding để tách Box ra khỏi Image
                            .size(dimensionResource(R.dimen.icon_size_medium)) // Kích thước nền tròn
                            .background(Color.White, shape = CircleShape), // Nền tròn màu trắng

                    ) {
                        Icon(
                            imageVector = FontAwesomeIcons.Solid.Bookmark,
                            contentDescription = "Bookmark",
                            tint = OliverGreen,
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.icon_size_small)) // Kích thước Icon
                                .align(Alignment.Center)
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun SimilarDishesCarousel() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.spacing_m))
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))
        SectionTitle("Still not sure what to cook?")

        Text(
            text = "Explore Similar Dishes"
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
        ) {
            // Các item trong LazyRow
            items(listOf(
                "Spaghetti", "Potato", "Beef", "Spaghetti",
                "Spaghetti", "Potato", "Beef", "Spaghetti"
            )) { item ->
                Card(
                    modifier = Modifier
                        .height(150.dp)
                        .width(250.dp),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_small))
                ) {
                    // Placeholder cho ảnh
                    Box(
                        modifier = Modifier
                            .fillMaxSize()


                    ) {
                        // Ảnh chiếm toàn bộ Card
                        Image(
                            painter = painterResource(id = R.drawable.mockrecipeimage),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(), // Chiếm toàn bộ không gian Card
                            contentScale = ContentScale.Crop // Làm cho ảnh vừa khung mà không bị biến dạng
                        )


                        Box(
                            modifier = Modifier
                                .height(142.dp)
                                .align(Alignment.TopCenter)

                        ) {
                            Box(
                                modifier = Modifier

                                    .align(Alignment.BottomCenter) // Định vị vị trí của Box chứa văn bản
                                    .background(
                                        color = Color.White, // Màu nền của vùng bao quanh
                                        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)) // Định dạng góc của vùng bao quanh
                                    )
                                    .padding(
                                        vertical = dimensionResource(R.dimen.spacing_s), // Khoảng cách trên dưới
                                        horizontal = dimensionResource(R.dimen.spacing_xl) // Khoảng cách trái phải
                                    )

                            ) {
                                Text(
                                    text = item,
                                    modifier = Modifier,
                                    color = OliverGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
private fun DishSuggestionByIngredientPreview() {
    DishSuggestionByIngredient()
}