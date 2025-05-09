package com.example.dacs_3.ui.theme.main

import DACS_3Theme
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.R
import com.example.dacs_3.utils.BottomNavBar
import com.example.dacs_3.utils.askForLocationPermission
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.dacs_3.model.Recipe
import com.example.dacs_3.repository.RecipeRepository
import com.example.dacs_3.viewModel.RecipeViewModel

@Composable
fun HomePageScreen(
    navController: NavController,
    userId: String?,
    recipeViewModel: RecipeViewModel = viewModel())
{
    val context = LocalContext.current


    // Registering permission request launcher
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Nếu quyền được cấp, xử lý lấy vị trí
                askForLocationPermission(context) { locationString ->
                    if (locationString != null) {
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        if (userId != null) {
                            val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)
                            userRef.update("location", locationString)
                                .addOnSuccessListener { Log.d("LocationUpdate", "Location saved: $locationString") }
                                .addOnFailureListener { Log.e("LocationUpdate", "Failed to update location", it) }
                        }
                    }
                }
            } else {
                // Nếu quyền bị từ chối, thông báo cho người dùng
                Toast.makeText(context, "Vị trí cần quyền để sử dụng chức năng này", Toast.LENGTH_SHORT).show()
            }
        }

    // Yêu cầu quyền vị trí ngay lập tức khi vào màn hình HomePageScreen
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Nếu chưa có quyền, yêu cầu quyền
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Nếu đã có quyền, gọi ngay hàm lấy vị trí
            askForLocationPermission(context) { locationString ->
                if (locationString != null) {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)
                        userRef.update("location", locationString)
                            .addOnSuccessListener { Log.d("LocationUpdate", "Location saved: $locationString") }
                            .addOnFailureListener { Log.e("LocationUpdate", "Failed to update location", it) }
                    }
                }
            }
        }
    }


    val recipes by recipeViewModel.recipes.collectAsState()
    val isLoading by recipeViewModel.isLoading.collectAsState()
    val errorMessage by recipeViewModel.errorMessage.collectAsState()

    // Trigger data load only once
    LaunchedEffect(Unit) {
        recipeViewModel.fetchRecipes()
    }
    Log.d("HomePageScreen", "Fetch recipes: $recipes")
    Log.d("HomePageScreen", "Recipes size: "+recipes.size  )


    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        },
        containerColor = Color(0xFFF7F7F7)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Important: respect Scaffold's inner padding
                .background(Color(0xFFF7F7F7)),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            item {
                SearchBox()
            }

            item {
                SectionTitle(title = "Featured Recipes")
            }

            item {
                RecipeRow(recipes,navController)
            }

            item {
                SectionTitle(title = "Trending Recipes")
            }

            item {
                RecipeRow(recipes,navController)
            }
        }
    }
}


@Composable
fun SearchBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFD7ECE2))
            .clickable { /* handle click */ },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.search), // Replace with your search icon
                contentDescription = "Search Icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Search for recipes...",
                color = Color(0xFF9AB0A3),
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF333333),
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun RecipeRow(recipes: List<Recipe>,navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        recipes.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (recipe in rowItems) {
                    RecipeCard(recipe = recipe, modifier = Modifier.weight(1f), navController)
                }
                // Fill in empty space if row has only 1 item
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, modifier: Modifier = Modifier,navController: NavController) {
    val painter = if (recipe.resultImages.isNotBlank()) {
        rememberAsyncImagePainter(recipe.resultImages) // Cloudinary URL
    } else {
        painterResource(id = R.drawable.mockrecipeimage) // Fallback
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            .clickable {
                Log.d("HomePageScreen", "Recipe clicked: ${recipe.recipeId}")

                navController.navigate("recipe_detail/${recipe.recipeId}") }
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.Black.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        ) {
            Text(
                text = recipe.title.ifBlank { "Untitled Recipe" },
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePageScreenPreview() {
    DACS_3Theme {
        val navController = rememberNavController() // Mock NavController
        val fakeUserId = "1234567890" // Fake user ID để test preview
        HomePageScreen(navController, fakeUserId)
    }
}
