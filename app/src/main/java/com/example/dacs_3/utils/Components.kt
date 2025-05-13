package com.example.dacs_3.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dacs_3.R


@Composable
    fun TopBar(title: String,
               showRightIcon: Boolean = true,
               rightIconRes: Int = R.drawable.email,
               onRightIconClick: () -> Unit = {},
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /* Back */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrowback),
                        contentDescription = "Back",
                        tint = Color(0xff3b684d)
                    )
                }
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff3b684d),
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
            }
            if (showRightIcon) {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = rightIconRes),
                        contentDescription = "Right Icon",
                        tint =Color(0xff3b684d)
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp)) // keeps alignment clean when icon is hidden
            }
        }



    }

@Composable
fun BottomNavBar(navController: NavController) {
    val navItems = listOf(
        BottomNavItemData(R.drawable.home, "passwordScreen"),
        BottomNavItemData(R.drawable.grroup, "emailScreen"),
        BottomNavItemData(R.drawable.add_24dp, "addRecipe"),
        BottomNavItemData(R.drawable.search_24dp, "googleScreen"),
        BottomNavItemData(R.drawable.person, "my_profile")
    )

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xFFDDEFEA)), // soft background
        horizontalArrangement = Arrangement.SpaceAround, // slightly closer than SpaceEvenly
        verticalAlignment = Alignment.CenterVertically
    ) {
        navItems.forEach { item ->
            BottomNavItem(
                iconId = item.iconId,
                navController = navController,
                destination = item.destination,
                isSelected = currentDestination == item.destination
            )
        }
    }
}


@Composable
fun BottomNavItem(
    iconId: Int,
    navController: NavController,
    destination: String,
    isSelected: Boolean
) {
    Icon(
        painter = painterResource(id = iconId),
        contentDescription = null,
        modifier = Modifier
            .size(32.dp) // Increased icon size
            .clickable {
                navController.navigate(destination) {
                    launchSingleTop = true
                }
            },
        tint = Color(0xFF3F764E) // Your desired green color
    )
}


// Data class to make items cleaner
data class BottomNavItemData(val iconId: Int, val destination: String)
