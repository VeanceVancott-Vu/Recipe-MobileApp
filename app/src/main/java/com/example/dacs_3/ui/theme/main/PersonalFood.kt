package com.example.dacs_3.ui.theme.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.dacs_3.R
import com.example.dacs_3.model.Collections
import com.example.dacs_3.model.Recipe
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.viewmodel.CollectionsViewModel
import com.example.dacs_3.viewmodel.RecipeViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Bell
import compose.icons.fontawesomeicons.solid.Bookmark
import compose.icons.fontawesomeicons.solid.EllipsisV
import compose.icons.fontawesomeicons.solid.Folder

@Composable
fun PersonalFood(
    modifier: Modifier = Modifier,
    collectionsViewModel: CollectionsViewModel,
    navController: NavController,
    userId:String,
    recipeViewModel: RecipeViewModel
) {
    // Dữ liệu Fake
    val recipes = listOf(
        Recipe(
            title = "Spaghetti Bolognese",
            resultImages = "https://i.pinimg.com/736x/c7/26/5c/c7265c102d65fc6b2d78ca307e6c5968.jpg"
        ),
        Recipe(
            title = "Classic Pancakes",
            resultImages = "https://example.com/images/classic_pancakes.jpg"
        ),
        Recipe(
            title = "Grilled Chicken Salad",
            resultImages = "https://example.com/images/grilled_chicken_salad.jpg"
        ),
        Recipe(
            title = "Beef Tacos",
            resultImages = "https://example.com/images/beef_tacos.jpg"
        ),
        Recipe(
            title = "Strawberry Cheesecake",
            resultImages = "https://example.com/images/strawberry_cheesecake.jpg"
        )
    )


    // Lấy giá trị dimensionResource bên trong Composition
    val horizontalPadding = dimensionResource(R.dimen.spacing_m)
    val verticalSpacing   = dimensionResource(R.dimen.spacing_m)

    val collections by collectionsViewModel.collections.collectAsState()

    LaunchedEffect(Unit) {
        collectionsViewModel.loadCollections(userId)
        Log.d("PersonalFood", "Collections: $collections")

    }




    Column(
        modifier = modifier
            .fillMaxSize()                                // nếu cần
            .padding(horizontal = horizontalPadding),     // padding trái–phải
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        PersonalFoodHeader(
            modifier = modifier
        )

        SearchBar(
            modifier = modifier
        )

        TagSelector(
            modifier = modifier,
            collectionsViewModel = collectionsViewModel,
            userId = userId,
            collections,
            recipeViewModel
        )



    }
}

@Composable
fun PersonalFoodHeader(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m)),
        modifier = Modifier
            .fillMaxWidth()
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
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_xl))
                    .clip(CircleShape)
            )
        }

        SectionTitle("Your Personal Food Vault")

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = FontAwesomeIcons.Solid.Bell,
                contentDescription = "Notifications",
                tint = OliverGreen,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_medium))
            )
        }
    }
}

@Composable
fun SearchBar(
    // query: String,
    // onQueryChange: (String) -> Unit,
    // onSearch: () -> Unit
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        TextField(
            value = "", // query,
            onValueChange = {
                // onQueyChange
            },
            placeholder = {
                Text(
                    text = "Search ...",
                    color = OliverGreen
                )
            },
            singleLine = true,
            leadingIcon = null,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = OliverGreen,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_large))
                )
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                disabledTextColor = Color.Gray,
                errorTextColor = Color.Red,

                focusedContainerColor = Color(0xFFEAF0EB),
                unfocusedContainerColor = Color(0xFFEAF0EB),
                disabledContainerColor = Color(0xFFEAF0EB),
                errorContainerColor = Color(0xFFEAF0EB),

                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,

                cursorColor = Color.Black,

                focusedPlaceholderColor = Color(0xFFB0BFB6),
                unfocusedPlaceholderColor = Color(0xFFB0BFB6),

                focusedSupportingTextColor = Color.Transparent,
                unfocusedSupportingTextColor = Color.Transparent,
                disabledSupportingTextColor = Color.Transparent,
                errorSupportingTextColor = Color.Transparent
            ),
            shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun TagSelector(
    modifier: Modifier = Modifier,
    collectionsViewModel: CollectionsViewModel,
    userId: String,
    collections: List<Collections>,
    recipeViewModel: RecipeViewModel

    ) {
    var showDialog by remember { mutableStateOf(false) }
    var newTag by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
//    var selectedTag by remember { mutableStateOf<String?>(collections.first().name) }
    var selectedTag by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(collections) {
        // Only set the selectedTag when collections are available and selectedTag is null
        if (collections.isNotEmpty() && selectedTag == null) {
            selectedTag = collections.first().name
            // Load recipes for the initial tag
            recipeViewModel.loadRecipesByCollection(collections.first().recipeIds)
        }
    }

    LaunchedEffect(selectedTag) {
        // When selectedTag changes, load recipes based on that tag
        selectedTag?.let { tag ->
            val selectedCollection = collections.find { it.name == tag }
            selectedCollection?.recipeIds?.let { ids ->
                recipeViewModel.loadRecipesByCollection(ids)
            }
        }
    }



    val recipesByCollection by recipeViewModel.recipesByCollection.collectAsState()

    Row(
        modifier = Modifier
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = FontAwesomeIcons.Solid.Folder,
            contentDescription = "Add tag",
            tint = Color(0xFF2F5D47),
            modifier = Modifier
                .size(dimensionResource(R.dimen.icon_size_medium))
                .clickable { showDialog = true }
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_m)))

        collections.forEachIndexed { _, collection  ->
            val isSelected = selectedTag == collection.name

            Surface(
                shape = RoundedCornerShape(50),
                color = if (isSelected) Color(0xFF93A89D) else Color(0xFFE9EFEA),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clickable {
                        selectedTag = if (isSelected) null else collection.name
                        // Optionally call some action based on selection
                    }
            ) {
                Text(
                    text = collection.name,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color(0xFF2F5D47),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add new tag") },
            text = {
                TextField(
                    value = newTag,
                    onValueChange = { newTag = it },
                    label = { Text("Tag name") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newTag.isNotBlank()) {
                        collectionsViewModel.addCollection(newTag, userId = userId)
                        newTag = ""
                    }
                    showDialog = false
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_m)))

    SectionTitle("Your $selectedTag ")

    FoodCardGrid(
        recipeList = recipesByCollection,
        modifier = modifier
    )

}

@Composable
fun FoodCardGrid(
    recipeList: List<Recipe>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s))
    ) {
        items(recipeList) { recipe ->
            FoodCardItem(recipe = recipe)
        }
    }
}

@Composable
fun FoodCardItem(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.spacing_s)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Chỉnh màu nền của Card
        ),
        modifier = Modifier
            .size(
                width = dimensionResource(R.dimen.image_size_medium),
                height = dimensionResource(R.dimen.image_size_large)
            )

    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s))
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = recipe.resultImages,
                    placeholder = painterResource(R.drawable.loading),
                    error = painterResource(R.drawable.uploadfailed)
                ),
                contentDescription = "Food Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.image_size_medium))
            )

            Text(
                text = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.spacing_xs)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Bookmark,
                        contentDescription = "Save to folder",
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_small)),
                        tint = OliverGreen
                    )
                }

                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.EllipsisV,
                        contentDescription = "More options",
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_small)),
                        tint = OliverGreen
                    )
                }
            }
        }

    }

}


@Preview(showBackground = true)
@Composable
private fun PersonalFoodPreview() {
   // PersonalFood()
}