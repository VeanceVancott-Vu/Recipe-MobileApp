package com.example.dacs_3.ui.theme.main

import DACS_3Theme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.R
import com.example.dacs_3.utils.BottomNavBar

import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar

@Composable
fun HomePageScreen(navController: NavController, userId: String?) {
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

            items(2) {
                RecipeRow()
            }

            item {
                SectionTitle(title = "Trending Recipes")
            }

            items(1) {
                RecipeRow()
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
fun RecipeRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RecipeCard(modifier = Modifier.weight(1f))
        RecipeCard(modifier = Modifier.weight(1f))
    }
}

@Composable
fun RecipeCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            .clickable { /* Navigate to recipe detail */ }
    ) {
        Image(
            painter = painterResource(id = R.drawable.mockrecipeimage),
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
                text = "Delicious Dish",
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
