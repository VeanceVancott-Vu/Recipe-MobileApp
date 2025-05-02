package com.example.dacs_3.ui.theme.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dacs_3.R
import com.example.dacs_3.model.Comment
import com.example.dacs_3.model.Instruction
import com.example.dacs_3.model.Link
import com.example.dacs_3.ui.theme.MistGreen66
import com.example.dacs_3.ui.theme.OliverGreen
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Clock
import compose.icons.fontawesomeicons.solid.Bookmark
import compose.icons.fontawesomeicons.solid.Clipboard
import compose.icons.fontawesomeicons.solid.EllipsisH
import compose.icons.fontawesomeicons.solid.EllipsisV
import compose.icons.fontawesomeicons.solid.Paperclip
import compose.icons.fontawesomeicons.solid.Star
import compose.icons.fontawesomeicons.solid.Thumbtack

@Composable
fun RecipeDetailScreen(
    imageUrl: String = "",
    paddingValues: PaddingValues = PaddingValues(0.dp),
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xl))
    ) {
        val commentList = listOf(
            Comment(
                commentId = "cmt001",
                recipeId = "recipe123",
                userId = "user01",
                username = "An Nguyen",
                text = "Món này ngon thật sự! Cảm ơn bạn đã chia sẻ 🥰",
                timestamp = 1683289200000, // 05/04/2023 12:00 GMT
                isReported = false
            ),
            Comment(
                commentId = "cmt002",
                recipeId = "recipe123",
                userId = "user02",
                username = "Linh Pham",
                text = "Mình thử làm theo công thức mà bị khét 😅 Có mẹo gì không bạn?",
                timestamp = 1683375600000, // 06/04/2023 12:00 GMT
                isReported = false
            ),
            Comment(
                commentId = "cmt003",
                recipeId = "recipe123",
                userId = "user03",
                username = "Minh Tran",
                text = "Gợi ý tuyệt vời! Mình thêm tí phô mai vào và ngon hơn nhiều!",
                timestamp = 1683462000000, // 07/04/2023 12:00 GMT
                isReported = false
            ),
            Comment(
                commentId = "cmt004",
                recipeId = "recipe123",
                userId = "user04",
                username = "Thảo Lê",
                text = "Bài viết rất chi tiết, cảm ơn bạn nhiều nha! ❤️",
                timestamp = 1683548400000, // 08/04/2023 12:00 GMT
                isReported = false
            )
        )

        var commentText by remember { mutableStateOf("") }

        DishImage(
            onBackClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        FeatureIcon()

        DishContent(
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.spacing_l), end = dimensionResource(R.dimen.spacing_l))
        )


        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_xl)))


        RecipeAuthorInfo(
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.spacing_l), end = dimensionResource(R.dimen.spacing_l))
        )


         RecipeRatingCard()

        CommentListSection(
            commentList = commentList,
            onValueChange = { commentText = it },
            commentText = commentText
        )

        SameAuthor()
    }

}

@Composable
private fun DishImage(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        // Ảnh món ăn
        Image(
            painter = painterResource(R.drawable.mockrecipeimage),
            contentDescription = "Dish Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )

        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
    }
}

@Composable
private fun FeatureIcon(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
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
                    imageVector = FontAwesomeIcons.Solid.Thumbtack,
                    contentDescription = "Pin dish today",
                    tint = Color(0xFF3F764E),
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_small))
                )
            }
        }

        Spacer(Modifier.width(dimensionResource(R.dimen.spacing_xl)))

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
                    contentDescription = "Pin dish today",
                    tint = Color(0xFF3F764E),
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_small))
                )
            }
        }

        Spacer(Modifier.width(dimensionResource(R.dimen.spacing_xl)))

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
                    contentDescription = "Pin dish today",
                    tint = Color(0xFF3F764E),
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_small))
                )
            }
        }
    }
}

@Composable
private fun DishContent(
    modifier: Modifier = Modifier
) {
    val ingredientsList = listOf(
        "2 eggs",
        "Salt & pepper",
        "Butter or oil",
        "Cheese, ham, or other fillings (optional)"
    )

    val stepList = listOf(
    Instruction(
            stepNumber = 1,
    description = "Prepare the ingredients: Chop the vegetables and measure the spices.",
    imageUrl = listOf("https://example.com/images/step1_1.jpg", "https://example.com/images/step1_2.jpg")
    ),
    Instruction(
        stepNumber = 2,
        description = "Heat the pan and add oil. Once hot, add onions and garlic and sauté until golden brown.",
        imageUrl = listOf("https://example.com/images/step2_1.jpg")
    ),
    Instruction(
        stepNumber = 3,
        description = "Add chopped vegetables to the pan and cook until tender.",
        imageUrl = listOf("https://example.com/images/step3_1.jpg", "https://example.com/images/step3_2.jpg")
    ),
    Instruction(
        stepNumber = 4,
        description = "Add the spices and stir to combine, cooking for an additional 2 minutes.",
        imageUrl = listOf("https://example.com/images/step4_1.jpg")
    ),
    Instruction(
        stepNumber = 5,
        description = "Serve hot and garnish with fresh herbs.",
        imageUrl = listOf("https://example.com/images/step5_1.jpg")
    )
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xl))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f)
            )

            SectionTitle(
                "Name of the dish",
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.spacing_s))
            )

            HorizontalDivider(
                modifier = Modifier.weight(1f)
            )
        }

        CreatorInfo()

        DishProperties()

        DishIngredients(
            ingredientsList = ingredientsList
        )

        CookingSteps(
            stepList = stepList
        )
    }
}

@Composable
private fun RecipeAuthorInfo(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.mockrecipeimage),
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(dimensionResource(R.dimen.icon_size_xl))
                .clip(CircleShape)
        )


        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_m)))

        Text(
            text = "Recipe by",
            fontSize = with(LocalDensity.current) {
                dimensionResource(R.dimen.text_size_small).toSp()
            },
            color = OliverGreen
        )

        Text(
            text = "Léonie Diane Quiterrie",
            color = OliverGreen,
            fontWeight = FontWeight.Bold
        )


        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))

        Card(
            modifier = Modifier
                .height(45.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OliverGreen // hoặc backgroundColor nếu dùng Compose cũ
                )
            ) {
                Text("Follow me")
            }
        }

    }
}

@Composable
private fun RecipeRatingCard(
    rating: Int = 4,
    onRatingChanged: (Int) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Card nền tím
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Rate this Recipe",
                    color = Color(0xFF305C43),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.spacing_s))
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_m)))

                Row {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star ${index + 1}",
                            tint = if (index < rating) Color.Yellow else Color.Gray,
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.icon_size_xl))
                                .padding(4.dp)
                                .clickable { onRatingChanged(index + 1) }
                        )
                    }
                }
            }
        }

        // Ngôi sao lớn đè lên nền tím
        Image(
            painter = painterResource(R.drawable.start),
            contentDescription = "Decorative stars",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(150.dp)
        )
    }
}

@Composable
private fun CommentListSection(
    commentText: String,
    onValueChange: (String) -> Unit,
    commentList: List<Comment>,
    modifier: Modifier = Modifier
        .padding(dimensionResource(R.dimen.spacing_m))
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
    ) {
        SectionTitle("Comment")

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.mockrecipeimage),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_lg_medium))
                    .clip(CircleShape)
            )

            Spacer(Modifier.width(dimensionResource(R.dimen.spacing_m)))


            TextField(
                value = commentText,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = "Add a comment",
                        fontSize = 14.sp    // Chỉnh size nhỏ lại
                    )
                },
                textStyle = TextStyle(
                    fontSize = 14.sp       // Size chữ nhập vào nhỏ đồng bộ
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp) // Chiều cao hợp lý cho gõ 1 dòng
            )
        }

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
        ) {
            commentList.forEach { comment ->
                CommentItem(comment = comment)
            }
        }
    }

}

@Composable
fun SameAuthor() {
    Column() {
        Text(
            text = "From the same author"
        )


    }
}

@Composable
fun CommentItem(
    comment: Comment,
    modifier: Modifier = Modifier
) {
    Row(

    ) {
        Image(
            painter = painterResource(R.drawable.mockrecipeimage),
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(dimensionResource(R.dimen.icon_size_lg_medium))
                .clip(CircleShape)
        )

        Spacer(Modifier.width(dimensionResource(R.dimen.spacing_m)))

        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
        ) {
            Row() {
                Text(
                    text = comment.username
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = comment.timestamp.toString()
                )

                Spacer(Modifier.width(dimensionResource(R.dimen.spacing_m)))

                Icon(
                    imageVector = FontAwesomeIcons.Solid.EllipsisH,
                    contentDescription = "",
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small))
                )
            }

            Text(
                text = comment.text
            )
        }
    }
}

@Composable
private fun CreatorInfo(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
        ) {
            Card(
                elevation = CardDefaults.cardElevation(16.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(Color(0xFFDBE6DE)),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_xl))
            ) {
                Image(
                    painter = painterResource(R.drawable.mockrecipeimage),
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s))
            ) {
                Text(
                    text = "Leonie Diane",
                    color = OliverGreen
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.location_dot),
                        contentDescription = "Icon map",
                        tint = Color(0xFF3F764E ),
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_small))
                            .aspectRatio(2 / 3f)
                    )

                    Text(
                        text =  "US",
                        color = OliverGreen
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Story behind this dish ...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.spacing_s) * 15)
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_m)))
                    .background(colorResource(R.color.text_field_background)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    selectionColors = LocalTextSelectionColors.current,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                )
            )
        }
    }
}

@Composable
private fun DishProperties(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f)
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_m))
        ) {
            IconAndText(
                imageVector = FontAwesomeIcons.Regular.Clock,
                color = Color((0xFF3F764E)),
                contentDescription = "Cook Time",
                text = "30'",
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_medium))
            )

            Spacer(Modifier.width(dimensionResource(R.dimen.spacing_xl)))

            IconAndText(
                imageVector = FontAwesomeIcons.Solid.Star,
                color = Color(0xFFF1FD4D),
                contentDescription = "Rate",
                text = "3.5",
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_medium))
            )
        }

        Text(
            text = "Medium",
            fontWeight = FontWeight.Bold,
            color = OliverGreen
        )
    }
}

@Composable
private fun DishIngredients(
    ingredientsList: List<String>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
    ) {
        SectionTitle("Ingredients")

        ingredientsList.forEach { item ->
            Text(
                text = "- $item"
            )
        }
    }


}

@Composable
private fun CookingSteps(
    stepList: List<Instruction>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xl))
    ) {
        SectionTitle("Cooking Steps")

        stepList.forEach { step ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
            ) {
                Card(
                    modifier = Modifier,
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_xl))
                            .clip(shape = CircleShape)
                            .background(MistGreen66)
                    ) {
                        SectionTitle(step.stepNumber.toString())
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s))
                ){
                    Text(
                        text = step.description,
                        color = Color.Gray
                    )

//                    LazyRow {
//                        items(step.imageUrl) { image ->
//                            Image(
//                                painter = painterResource(image),
//                                contentDescription = "Cooking step image",
//                                contentScale = ContentScale.Crop, // 👈 quan trọng nè!
//                                modifier = Modifier
//                                    .width(dimensionResource(R.dimen.image_size_large))
//                                    .height(dimensionResource(R.dimen.image_size_medium))
//                                    .padding(end = dimensionResource(R.dimen.spacing_xs))
//                                    .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_s))) // nếu muốn bo góc nữa
//                            )
//
//                        }
//                    }
//
//                    ShowLinks(step.links)
                }
            }
        }
    }
}

@Composable
fun ShowLinks(links: List<Link>) {

        links.forEachIndexed { index, link ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs)),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Paperclip,
                    contentDescription = "File attach",
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_small)),
                    tint = OliverGreen
                )

                Text(
                    text = link.title, // Hiển thị tên của link
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = dimensionResource(R.dimen.spacing_s)),
                    color = Color.Gray
                )

                IconButton(
                    onClick = {
                    // Xử lý xóa link khỏi danh sách nếu cần
                    },
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_small))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove link",
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_small))
                    )
                }
            }
        }
}



@Composable
private fun IconAndText(
    imageVector: ImageVector,
    color: Color,
    contentDescription: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s))
    ) {
        Icon(
            imageVector = imageVector,
            tint = color,
            contentDescription = contentDescription,
            modifier = modifier
        )

        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = OliverGreen
        )
    }
}

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = with(LocalDensity.current) {
            dimensionResource(R.dimen.text_size_large).toSp()
        },
        color = OliverGreen,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
private fun RecipeDetailScreenPreview() {
    RecipeDetailScreen(
        imageUrl = "https://www.bestherbalhealth.com/wp-content/uploads/2014/07/Melon.jpg",
        onBackClick = {}
    )
}