package com.example.dacs_3.ui.theme.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.dacs_3.R
import com.example.dacs_3.model.Recipe
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.utils.BottomNavBar
import com.example.dacs_3.viewmodel.AuthViewModel
import com.example.dacs_3.viewmodel.RecipeViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Bookmark
import compose.icons.fontawesomeicons.regular.Clock
import compose.icons.fontawesomeicons.solid.Bookmark
import compose.icons.fontawesomeicons.solid.Clock
import compose.icons.fontawesomeicons.solid.SlidersH
import compose.icons.fontawesomeicons.solid.User

@Composable
fun SearchScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onSearch: (String) -> Unit = {},
    onFilter: () -> Unit = {},
    recipeViewModel: RecipeViewModel,
    authViewModel: AuthViewModel
) {
    // Lịch sử tìm kiếm giả
    val fakeHistory = remember { mutableStateListOf(
            "Spaghetti Bolognese",
            "Classic Pancakes",
            "Grilled Chicken Salad",
            "Beef Tacos",
            "Strawberry Cheesecake") }

    var text by remember { mutableStateOf("") }

    var showHistory by remember { mutableStateOf(true) } //  Toggle visibility

    val results by recipeViewModel.searchResults.collectAsState()
    val isSearching by recipeViewModel.isSearching.collectAsState()



    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var query by remember { mutableStateOf("") }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.height_top_bar)),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(R.dimen.spacing_m), end = dimensionResource(R.dimen.spacing_m)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))

            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable {
                            navController.popBackStack() // Quay lại màn hình trước (SearchScreen)
                        },
                    tint = OliverGreen
                )

                Card(
                    modifier = Modifier
                        .weight(1f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)), // Bo góc cho Card
                ) {
                    // TextField dạng search
                    val containerColor = Color(0xFFDBE6DE)  // Nền xanh nhạt
                    TextField(
                        value = query,
                        onValueChange = {
                            query = it
                            onSearch(it)
                        },
                        placeholder = {
                            Text(
                                text = "Enter ingredient names …",
                            )
                        },
                        singleLine = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = Modifier
                                    .size(dimensionResource(R.dimen.icon_size_medium))
                                    .clickable {
                                        showHistory = false
                                            recipeViewModel.searchRecipesByName(query)
                                               },

                               tint = OliverGreen
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = containerColor,
                            unfocusedContainerColor = containerColor,
                            disabledContainerColor = containerColor,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
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
                        .clickable {
                            navController.navigate("filter") // Điều hướng đến FilterScreen
                        }
                        .size(dimensionResource(R.dimen.icon_size_medium)),
                    tint = OliverGreen
                )

            }
        }

        Spacer(Modifier.height(12.dp))

        // --- 2. List lịch sử giả ---
        if(showHistory) {
            SearchHistoryList(
                history = fakeHistory,
                onItemClick = { text = it },
                onItemDelete = { fakeHistory.remove(it) }
            )
        }
        else {

            val demoRecipes = listOf(
                Recipe(
                    recipeId = "1",
                    title = "Creamy Chicken Alfredo",
                    story = "A rich and creamy pasta dish that's perfect for weeknight dinners.",
                    servingSize = "2 servings",
                    cookingTime = "30 min",
                    resultImages = "https://images.unsplash.com/photo-1603133872872-684928dc4a07",
                    ingredients = listOf(
                        "Chicken breast",
                        "Fettuccine",
                        "Heavy cream",
                        "Parmesan",
                        "Garlic"
                    ),
                    instructions = listOf(),
                    likes = 120,
                    smiles = 85,
                    claps = 45,
                    userId = "user123",
                    ratings = listOf(),
                    averageRating = 4.5f
                ),
                Recipe(
                    recipeId = "2",
                    title = "Vegan Buddha Bowl",
                    story = "A colorful and nourishing vegan bowl packed with flavor.",
                    servingSize = "1 bowl",
                    cookingTime = "20 min",
                    resultImages = "https://images.unsplash.com/photo-1605478571948-e7cf6deff18b",
                    ingredients = listOf(
                        "Quinoa",
                        "Chickpeas",
                        "Avocado",
                        "Sweet potato",
                        "Tahini"
                    ),
                    instructions = listOf(),
                    likes = 75,
                    smiles = 90,
                    claps = 30,
                    userId = "user456",
                    ratings = listOf(),
                    averageRating = 4.8f
                ),
                Recipe(
                    recipeId = "3",
                    title = "Blueberry Pancakes",
                    story = "Fluffy pancakes bursting with blueberries and a hint of vanilla.",
                    servingSize = "3 pancakes",
                    cookingTime = "25 min",
                    resultImages = "https://images.unsplash.com/photo-1587731253653-0711df56f156",
                    ingredients = listOf("Flour", "Milk", "Eggs", "Blueberries", "Maple syrup"),
                    instructions = listOf(),
                    likes = 150,
                    smiles = 110,
                    claps = 70,
                    userId = "user789",
                    ratings = listOf(),
                    averageRating = 4.7f
                )
            )

            LazyColumn {
                items(results) { recipe ->
                    RecipeCard(recipe = recipe)
                }
            }

        }

    }
}
@Composable
fun SearchHistoryList(
    history: List<String>,
    onItemClick: (String) -> Unit,
    onItemDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(history) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item) }
                    .padding(
                        vertical = dimensionResource(R.dimen.spacing_s),
                        horizontal = dimensionResource(R.dimen.spacing_m)
                    )
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Regular.Clock,
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_average)),
                    tint = OliverGreen
                )

                Spacer(Modifier.width(dimensionResource(R.dimen.spacing_xl)))

                Text(
                    text = item,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_medium))
                        .clickable { onItemDelete(item) },
                    tint = OliverGreen
                )
            }
        }
    }
}



@Composable
fun RecipeCard(
    recipe: Recipe,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Column {
            // Image Section
            AsyncImage(
                model = recipe.resultImages,
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            // Text Content
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = recipe.story,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    InfoChip("🍽 ${recipe.servingSize}")
                    InfoChip("⏱ ${recipe.cookingTime}")
                    InfoChip("⭐ ${String.format("%.1f", recipe.averageRating)}")
                }
            }
        }
    }
}

@Composable
fun InfoChip(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xFFE9EFEA),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = Color(0xFF2F5D47),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}




@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    val navController = rememberNavController()
  //  SearchScreen(navController = navController)


}
