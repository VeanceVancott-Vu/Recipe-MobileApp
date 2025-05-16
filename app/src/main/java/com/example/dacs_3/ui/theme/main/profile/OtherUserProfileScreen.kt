package com.example.dacs_3.ui.theme.main.profile

import BioCard
import ProfileHeader
import SectionCard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.R
import com.example.dacs_3.model.Cooksnap
import com.example.dacs_3.model.Recipe
import com.example.dacs_3.model.User
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.ui.theme.main.CooksnapGrid
import com.example.dacs_3.ui.theme.main.DishCard
import com.example.dacs_3.ui.theme.main.SectionTitle
import com.example.dacs_3.ui.theme.main.firestore
import com.example.dacs_3.utils.TopBar
import com.example.dacs_3.viewmodel.OtherUserProfileViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun OtherUserProfileScreen(
    navController: NavController,
    userId: String,
    viewModel: OtherUserProfileViewModel = viewModel()
) {

    val userState by viewModel.user.collectAsState()

    // Load user khi userId thay đổi
    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }


    var itemsToShow by remember { mutableIntStateOf(3) }

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

    Scaffold(
        topBar = {
            TopBar("My profile", showRightIcon = false,
                onBackIconClick = {navController.popBackStack()} )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = innerPadding.calculateTopPadding()-35.dp)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                userState?.let { user ->
                    ProfileHeader(user)
                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }



                BioCard()

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OliverGreen)
                ) {
                    Text(
                        text = "Follow",
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Dishes",
                        fontSize = 24.sp,
                        color = OliverGreen,
                        fontWeight = FontWeight.Bold
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Hiển thị danh sách các món ăn giới hạn theo itemsToShow
                        dishes.take(itemsToShow).forEach { dish ->
                            DishCard(
                                dish,
                                Modifier.padding(vertical = dimensionResource(R.dimen.spacing_m))
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Nút "See more" chỉ hiện khi còn món chưa hiển thị
                        if (itemsToShow < dishes.size) {
                            Button(
                                onClick = {
                                    itemsToShow = (itemsToShow + 3).coerceAtMost(dishes.size)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Text(
                                    "See more",
                                    color = OliverGreen,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = TextDecoration.Underline
                                )
                            }
                        }
                    }


                    val cooksnaps = listOf(
                        Cooksnap(
                            cooksnapId = "cs1",
                            recipeId = "r1",
                            userId = "u1",
                            description = "Thật tuyệt vời, món ăn rất ngon và dễ làm!",
                            imageResult = "https://example.com/image1.jpg",
                            hearts = 10,
                            slaps = 2,
                            smiles = 5
                        ),
                        Cooksnap(
                            cooksnapId = "cs2",
                            recipeId = "r2",
                            userId = "u2",
                            description = "Món này mình làm chưa thành công lắm, cần thử lại.",
                            imageResult = "https://example.com/image2.jpg",
                            hearts = 7,
                            slaps = 3,
                            smiles = 4
                        )
                    )

                    val usersMap = mapOf(
                        "u1" to User(
                            userId = "u1",
                            email = "user1@example.com",
                            username = "UserOne",
//                            profileImageUrl = "https://example.com/avatar1.jpg",
                            dayJoin = System.currentTimeMillis() - 86400000L * 30, // gia nhập 30 ngày trước
                            location = "Hanoi"
                        ),
                        "u2" to User(
                            userId = "u2",
                            email = "user2@example.com",
                            username = "UserTwo",
//                            profileImageUrl = "https://example.com/avatar2.jpg",
                            dayJoin = System.currentTimeMillis() - 86400000L * 60, // gia nhập 60 ngày trước
                            location = "Saigon"
                        )
                    )
                    Text(
                        text = "Cooksnap",
                        fontSize = 24.sp,
                        color = OliverGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .height(300.dp)

                    ) {

                        CooksnapGrid(
                            cooksnapList = cooksnaps,
                            usersMap = usersMap
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun OtherUserProfileScreenPreview() {
//    OtherUserProfileScreen(
//        navController = rememberNavController()
//    )
}
