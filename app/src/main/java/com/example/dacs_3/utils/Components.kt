package com.example.dacs_3.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dacs_3.R
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Box
import compose.icons.fontawesomeicons.solid.Check
import compose.icons.fontawesomeicons.solid.Home
import compose.icons.fontawesomeicons.solid.Hourglass
import compose.icons.fontawesomeicons.solid.Plus
import compose.icons.fontawesomeicons.solid.Search
import compose.icons.fontawesomeicons.solid.ShieldAlt
import compose.icons.fontawesomeicons.solid.User
import compose.icons.fontawesomeicons.solid.Users


@Composable
    fun TopBar(title: String,
               showRightIcon: Boolean = true,
               rightIconRes: Int = R.drawable.email,
               onRightIconClick: () -> Unit = {},
               onBackIconClick: () -> Unit = {},
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick =   onBackIconClick ) {
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
        BottomNavItemData(icon = FontAwesomeIcons.Solid.Home, "homepage"),
        BottomNavItemData(icon = FontAwesomeIcons.Solid.Search, "search"),
        BottomNavItemData(icon = FontAwesomeIcons.Solid.Plus, "addRecipe"),
        BottomNavItemData(icon = FontAwesomeIcons.Solid.User, "my_profile"),
        BottomNavItemData(icon = FontAwesomeIcons.Solid.ShieldAlt, "")

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
                icon = item.icon,
                navController = navController,
                destination = item.destination,
                isSelected = currentDestination == item.destination
            )
        }
    }
}


@Composable
fun BottomNavItem(icon: ImageVector, navController: NavController, destination: String, isSelected: Boolean) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier
            .size(dimensionResource(R.dimen.icon_size_medium))
            .clipToBounds()
            .clickable {
                navController.navigate(destination) {
                    launchSingleTop = true
                }
            },
        tint = Color(0xFF3F764E) // Your desired green color
    )
}


@Composable
fun ReportSummary() {
    Row(
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.spacing_m))
    ) {
        ReportSummaryCard(
            icon = FontAwesomeIcons.Solid.Hourglass,
            titleLine1 = "Pending",
            titleLine2 = "Reports",
            count = "28"
        )

        Spacer(modifier = Modifier.weight(1f))

        ReportSummaryCard(
            icon = FontAwesomeIcons.Solid.Check,
            titleLine1 = "Resolved",
            titleLine2 = "Reports",
            count = "126"
        )


    }

}

@Composable
fun ReportSummaryCard(
    icon: ImageVector,
    titleLine1: String,
    titleLine2: String,
    count: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F0E9)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = Color(0xFF14462E)
                )
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text(
                        text = titleLine1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF567F67)
                    )
                    Text(
                        text = titleLine2,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF567F67)
                    )
                }
            }
            Text(
                text = count,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xFF567F67),
                modifier = Modifier.padding(start = 48.dp)
            )
        }
    }
}


data class BottomNavItemData( val icon: ImageVector, val destination: String)

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavBar() {
    val navController = rememberNavController()
    BottomNavBar(navController = navController)
}