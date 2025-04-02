package com.example.dacs_3.ui.theme.main

import DACS_3Theme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
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

@Composable
fun HomePageScreen(navController: NavController, userId: String?) {
    LazyColumn( // Replace Column with LazyColumn
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Q:
        // Hiển thị UID
        item {
            Text(
                text = "User ID: ${userId ?: "Not logged in"}",
                fontSize = 16.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Search Box
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(73.dp)
                    .background(Color(0xa8dbe6de), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Search for recipes...",
                    color = Color(0xff9ab0a3),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Featured Recipes Section
        item {
            SectionTitle("Featured Recipes")
        }

        items(2) {  // ✅ Using 'items' instead of manually repeating
            RecipeRow()
        }

        // Trending Recipes Section
        item {
            SectionTitle("Trending Recipes")
        }

        items(2) {  // ✅ Using 'items' instead of manually repeating
            RecipeRow()
        }

        // Bottom Navigation
        item {
            Spacer(modifier = Modifier.height(50.dp))
            BottomNavBar()
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xff3b684d),
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun RecipeRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        RecipeCard(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(8.dp))
        RecipeCard(modifier = Modifier.weight(1f))
    }
}

@Composable
fun RecipeCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(160.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxSize()
        )
    }
}

@Composable
fun BottomNavBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xa8dbe6de)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) {
            BottomNavItem(R.drawable.img)
        }
    }
}

@Composable
fun BottomNavItem(iconId: Int) {
    Image(
        painter = painterResource(id = iconId),
        contentDescription = null,
        modifier = Modifier
            .size(40.dp)
            .clipToBounds()
    )
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

