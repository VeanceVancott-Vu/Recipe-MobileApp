package com.example.dacs_3.ui.theme.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.dacs_3.R
import com.example.dacs_3.model.Collections
import com.example.dacs_3.model.Comment
import com.example.dacs_3.model.CommentReport
import com.example.dacs_3.model.Instruction
import com.example.dacs_3.model.Link
import com.example.dacs_3.model.Recipe
import com.example.dacs_3.model.RecipeReport
import com.example.dacs_3.model.User
import com.example.dacs_3.ui.theme.MistGreen66
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.viewmodel.AuthViewModel
import com.example.dacs_3.viewmodel.CollectionsViewModel
import com.example.dacs_3.viewmodel.CommentReportsViewModel
import com.example.dacs_3.viewmodel.CommentViewModel
import com.example.dacs_3.viewmodel.RecipeReportsViewModel
import com.example.dacs_3.viewmodel.RecipeViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Clock
import compose.icons.fontawesomeicons.regular.Hourglass
import compose.icons.fontawesomeicons.solid.ArrowDown
import compose.icons.fontawesomeicons.solid.Bookmark
import compose.icons.fontawesomeicons.solid.CameraRetro
import compose.icons.fontawesomeicons.solid.EllipsisV
import compose.icons.fontawesomeicons.solid.Paperclip
import compose.icons.fontawesomeicons.solid.Pen
import compose.icons.fontawesomeicons.solid.Star
import compose.icons.fontawesomeicons.solid.Thumbtack
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecipeDetailScreen(
    navController: NavController,
    id:String = "",
    recipeViewModel: RecipeViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    commentViewModel: CommentViewModel = viewModel(),
    collectionViewModel: CollectionsViewModel = viewModel(),
    recipeReportsViewModel: RecipeReportsViewModel = viewModel(),
    commentReportsViewModel: CommentReportsViewModel = viewModel()
) {
    Log.e("ID", "id is: $id")

    val selectedRecipe by recipeViewModel.selectedRecipe.collectAsState()
    val isLoading by recipeViewModel.isLoading.collectAsState()
    val errorMessage by recipeViewModel.errorMessage.collectAsState()

    val recipeUser by authViewModel.recipeUser.collectAsState()

    val currentUser by authViewModel.currentUser.collectAsState()

    val comment by commentViewModel.comments.collectAsState()

    val collections by collectionViewModel.collections.collectAsState()
//    val hasVoted = selectedRecipe?.let { recipe ->
//        currentUser?.let { user ->
//            recipeViewModel.hasUserVoted(recipe.recipeId, user.userId)
//        } ?: false
//    } ?: false
//
//    var currentRating by remember {
//        mutableStateOf(
//            selectedRecipe?.ratings?.find { rating ->
//                rating.userId == currentUser?.userId
//            }?.stars ?: 0f
//        )
//    }
    // Tính giá trị trung bình của tất cả các đánh giá
    val currentRating = selectedRecipe?.ratings?.map { it.stars }?.average()?.toFloat() ?: 0f

    // Kiểm tra xem người dùng đã đánh giá chưa
    val hasVoted by remember(selectedRecipe, currentUser) {
        derivedStateOf {
            selectedRecipe?.ratings?.any { it.userId == currentUser?.userId } == true
        }
    }

    // Lưu trữ điểm đánh giá mà người dùng chọn (chỉ dùng để gửi, không hiển thị)
    var userSelectedRating by remember { mutableStateOf<Float?>(null) }

    // Đặt lại userSelectedRating khi selectedRecipe hoặc currentUser thay đổi
    LaunchedEffect(selectedRecipe, currentUser) {
        val userRatingFromDb = selectedRecipe?.ratings?.find { it.userId == currentUser?.userId }?.stars
        userSelectedRating = userRatingFromDb // Lưu điểm đánh giá của người dùng (nếu có)
    }

    // Define coroutineScope and snackbarHostState
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Trigger fetching recipe only once on entering the screen
    LaunchedEffect(key1 = id) {
        recipeViewModel.fetchRecipeById(id)
    }

    // Once the recipe is available, fetch its user
    LaunchedEffect(key1 = selectedRecipe) {
        selectedRecipe?.let { authViewModel.fetchUserById(it.userId) }
    }

    LaunchedEffect(key1 = selectedRecipe) {
        selectedRecipe?.let { commentViewModel.loadComments(it.recipeId) }
    }

    LaunchedEffect(Unit) {
        collectionViewModel.loadCollections(currentUser?.userId ?: "")
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xl))
    ) {


        var commentText by remember { mutableStateOf("") }

        selectedRecipe?.let {
            DishImage(
                onBackClick = {navController.popBackStack()},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                it.resultImages

            )
        }

        FeatureIcon(
            onClick = {
                navController.navigate("recipe_edit/$id")
            },
            onShareCooksnapClick = {
                navController.navigate("cooksnap/$id")
            },
            collections = collections,
            collectionViewModel = collectionViewModel,
            currentUserId = currentUser?.userId?: "",
            recipeId =  selectedRecipe?.recipeId ?: "",
            recipeReportsViewModel = recipeReportsViewModel,
            recipeUserId = selectedRecipe?.userId ?: ""

        )

        DishContent(
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.spacing_l), end = dimensionResource(R.dimen.spacing_l))
            , recipe = selectedRecipe,
            recipeUser = recipeUser
        )


        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_xl)))


        RecipeAuthorInfo(
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.spacing_l), end = dimensionResource(R.dimen.spacing_l))
            ,recipeUser = recipeUser
        )


        RecipeRatingCard(
            rating = currentRating, // Hiển thị giá trị trung bình
            onRatingChanged = { newRating ->
                userSelectedRating = newRating // Lưu điểm người dùng chọn
                currentUser?.let { user ->
                    if (hasVoted) {
                        recipeViewModel.updateRating(id, user.userId, newRating) {}
                    } else {
                        recipeViewModel.addRating(id, user.userId, newRating) {}
                    }
                } ?: coroutineScope.launch {
                    snackbarHostState.showSnackbar("Vui lòng đăng nhập để đánh giá món ăn")
                }
            }
        )

        selectedRecipe?.let {
            currentUser?.let { it1 ->
                CommentListSection(
                    it1,
                    selectedRecipe = it,
                    commentViewModel    ,
                    commentList = comment,
                    onValueChange = { commentText = it },
                    commentText = commentText,
                    commentReportsViewModel = commentReportsViewModel
                )
            }
        }

        SameAuthor()
    }

}

@Composable
private fun DishImage(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageUri: String = ""
) {

    val painter = if (imageUri.isNotBlank()) {
        rememberAsyncImagePainter(model = imageUri)
    } else {
        painterResource(R.drawable.mockrecipeimage)
    }

    Box(
        modifier = modifier
    ) {
        // Ảnh món ăn
        Image(
            painter = painter,
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onShareCooksnapClick: () -> Unit,
    collections: List<Collections>,
    collectionViewModel: CollectionsViewModel,
    currentUserId: String,
    recipeId: String,
    recipeReportsViewModel: RecipeReportsViewModel,
    recipeUserId:String
) {
    var showDialog by remember { mutableStateOf(false) }

    var showRecipeReportDialog by remember { mutableStateOf(false) }
    var reportReason by remember { mutableStateOf("") }

    var recipeReportSubmitted by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

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
                        .clickable {
                            showDialog = true
                        }
                )


                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Save Recipe To Collection") },
                        text = {
                            Column {
                                collections.forEach { collection ->
                                    Text(
                                        text = collection.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                val updatedList =
                                                    collection.recipeIds.toMutableList()
                                                if (!updatedList.contains(recipeId)) {
                                                    updatedList.add(recipeId)
                                                    collectionViewModel.updateRecipesInCollection(
                                                        collection.id,
                                                        updatedList,
                                                        currentUserId
                                                    )
                                                }
                                                showDialog = false
                                            }
                                            .padding(vertical = 8.dp)
                                    )
                                }

                                if (collections.isEmpty()) {
                                    Text("No collections found. Please create one first.")
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Close")
                            }
                        }
                    )
                }
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
                    contentDescription = "",
                    tint = Color(0xFF3F764E),
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_small))
                        .clickable(enabled = !recipeReportSubmitted) {
                            showRecipeReportDialog = true
                        }
                )
            }
        }

        if (showRecipeReportDialog) {
            AlertDialog(
                onDismissRequest = { showRecipeReportDialog = false },
                title = {
                    Text("Report Recipe")
                },
                text = {
                    Column {
                        Text("Please enter the reason for reporting:")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = reportReason,
                            onValueChange = { reportReason = it },
                            placeholder = { Text("Enter reason...") },
                            singleLine = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (reportReason.isNotBlank()) {
                            val recipeReport = RecipeReport(
                                reportingUserId = currentUserId,
                                reportedRecipeId = recipeId,
                                reportedUserId = recipeUserId,
                                reason = reportReason
                            )
                            recipeReportsViewModel.submitReport(recipeReport)
                            showRecipeReportDialog = false
                            recipeReportSubmitted = true
                            reportReason = ""
                        }
                    }) {
                        Text("Submit")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showRecipeReportDialog = false
                        reportReason = ""
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }


        Spacer(Modifier.width(dimensionResource(R.dimen.spacing_xl)))

        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_large)),
                colors = IconButtonDefaults
                    .iconButtonColors(containerColor = Color(0xFFDBE6DE))
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Pen,
                    contentDescription = "",
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
                onClick = onShareCooksnapClick,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_large)),
                colors = IconButtonDefaults
                    .iconButtonColors(containerColor = Color(0xFFDBE6DE))
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.CameraRetro,
                    contentDescription = "",
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
    recipe: Recipe? = null,
    modifier: Modifier = Modifier,
    recipeUser: User? = null
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

            if (recipe != null) {
                SectionTitle(
                    recipe.title,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.spacing_s))
                )
            }
            else
            {
                SectionTitle(
                    "The Best Sweet and Sour Fish Soup",
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.spacing_s))
                )
            }

            HorizontalDivider(
                modifier = Modifier.weight(1f)
            )
        }

        CreatorInfo(
            recipe = recipe,
            recipeUser = recipeUser
        )

        DishProperties()

        if (recipe != null) {
            DishIngredients(
                ingredientsList = recipe.ingredients
            )
        }
        else
        {
            DishIngredients(
                ingredientsList = ingredientsList
            )
        }

        if (recipe != null) {
            CookingSteps(
                stepList = recipe.instructions
            )
        }
        else
        {
            CookingSteps(
                stepList = stepList
            )
        }

    }
}

@Composable
private fun RecipeAuthorInfo(
    modifier: Modifier = Modifier,
    recipeUser: User? = null,

) {
    val imageUri = recipeUser?.profileImageUrl
    val painter = if (imageUri?.isNotBlank() == true) {
        rememberAsyncImagePainter(model = imageUri)
    } else {
        painterResource(R.drawable.account)
    }


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painter,
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

        if (recipeUser != null) {
            Text(
                text = recipeUser.username,
                color = OliverGreen,
                fontWeight = FontWeight.Bold
            )
        }
        else
        {
            Text(
                text = "Veance",
                color = OliverGreen,
                fontWeight = FontWeight.Bold
            )
        }


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
    rating: Float,
    onRatingChanged: (Float) -> Unit = {}
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
                        val starFill = when {
                            index + 1 <= rating -> 1f // Sao sáng đầy đủ
                            index.toFloat() < rating -> rating - index // Sao sáng một phần
                            else -> 0f // Sao không sáng
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(4.dp)
                                .clickable {
                                    // Gọi hàm xử lý khi người dùng nhấn vào sao
                                    onRatingChanged(index + 1f) // Cập nhật giá trị mới cho rating
                                }
                        ) {
                            // Sao nền màu xám
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star ${index + 1}",
                                tint = Color.Gray,
                                modifier = Modifier.fillMaxSize()
                            )
                            // Sao màu vàng, sáng dần theo rating
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star ${index + 1}",
                                tint = Color.Yellow,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .drawWithContent {
                                        val fillWidth = size.width * starFill
                                        clipRect(0f, 0f, fillWidth, size.height) {
                                            this@drawWithContent.drawContent()
                                        }
                                    }
                            )
                        }
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
    currentUser: User,
    selectedRecipe : Recipe,
    commentViewModel: CommentViewModel,
    commentText: String,
    onValueChange: (String) -> Unit,
    commentList: List<Comment>,
    commentReportsViewModel: CommentReportsViewModel,
    modifier: Modifier = Modifier
        .padding(dimensionResource(R.dimen.spacing_m))
) {
    var isEditing by remember { mutableStateOf(false) }
    var editingCommentId by remember { mutableStateOf<String?>(null) }
    var filteredComments = commentList.filter { it.commentId != editingCommentId }

    val visibleComments = if (editingCommentId != null) {
        commentList.filter { it.commentId != editingCommentId }
    } else {
        commentList // unfiltered
    }

    val imageUri = currentUser.profileImageUrl
    val painter = if (imageUri.isNotBlank()) {
        rememberAsyncImagePainter(model = imageUri)
    } else {
        painterResource(R.drawable.account)
    }

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
                painter = painter,
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
                        fontSize = 14.sp
                    )
                },
                textStyle = TextStyle(fontSize = 14.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                trailingIcon = {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.ArrowDown,
                        contentDescription = "Submit comment",
                        tint = Color.Black, // Make it pop on white background
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_small))
                            .clickable {

                                val comment = currentUser?.let {
                                    Comment(
                                        recipeId = selectedRecipe.recipeId,
                                        userId = it.userId,
                                        username = it.username,
                                        text = commentText,
                                        timestamp = System.currentTimeMillis(),
                                        isReported = false
                                    )
                                }
                                if (comment != null) {
                                    if (isEditing) {
                                        val newComment = currentUser.let {
                                            Comment(
                                                commentId = editingCommentId!!,
                                                recipeId = selectedRecipe.recipeId,
                                                userId = it.userId,
                                                username = it.username,
                                                text = commentText,
                                                timestamp = System.currentTimeMillis(),
                                                isReported = false
                                            )
                                        }

                                        Log.d(
                                            "CommentListSection/Recipe Detail/Editing",
                                            "Comment: $newComment from user: $currentUser in recipe: $selectedRecipe"
                                        )
                                        commentViewModel.updateComment(newComment)
                                        isEditing = false
                                        editingCommentId =
                                            null // this will show the full list again
                                        onValueChange("")
                                    } else {
                                        Log.d(
                                            "CommentListSection/Recipe Detail/Posting",
                                            "Comment: $commentText from user: $currentUser in recipe: $selectedRecipe"
                                        )
                                        commentViewModel.postComment(comment)
                                        onValueChange("")

                                    }


                                }

                            }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )




        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_m)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
        ) {
            filteredComments.forEach { comment ->
                CommentItem(
                    comment = comment,
                    onEdit = {
                        onValueChange(comment.text)
                        editingCommentId = comment.commentId
                        isEditing = true
                    },
                    onDelete = {
                        commentViewModel.deleteComment(comment.commentId, selectedRecipe.recipeId)
                        Log.d("CommentListSection/Recipe Detail", "Delete comment: $comment")
                    }
                    , currentUser = currentUser,
                    commentReportsViewModel = commentReportsViewModel,
                    selectedRecipe = selectedRecipe
                )
            }
        }


    }

}


@Composable
fun CommentItem(
    comment: Comment,
    modifier: Modifier = Modifier,
    currentUser: User? = null,
    onEdit: (String) -> Unit, // Callback for edit action
    onDelete: () -> Unit,    // Callback for delete action
    commentReportsViewModel: CommentReportsViewModel,
    selectedRecipe : Recipe
) {
    var expanded by remember { mutableStateOf(false) }
    var showCommentReportDialog by remember { mutableStateOf(false) }
    val imageUri = currentUser?.profileImageUrl
    var reportReason by remember { mutableStateOf("") }


    val painter = if (imageUri?.isNotBlank() == true) {
        rememberAsyncImagePainter(model = imageUri)
    } else {
        painterResource(R.drawable.account)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset -> // 'offset' provides the position of the long press
                        expanded = true
                    }
                )
            }
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.spacing_s)) // Add some padding around the content
        ) {
            Image(
                painter = painter,
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = comment.username,
                    )

                    Spacer(Modifier.weight(1f))
                    val formattedDate = formatTimestamp(comment.timestamp)

                    Text(
                        text = formattedDate,
                    )
                }

                Text(
                    text = comment.text,
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 0.dp, y = 20.dp)
        ) {
            if (currentUser != null) {
                if (comment.userId == currentUser.userId) {
                    // Show Edit & Delete for own comment
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            expanded = false
                            onEdit(comment.text)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            expanded = false
                            onDelete()
                        }
                    )
                } else {
                    // Show Report for other users
                    DropdownMenuItem(
                        text = { Text("Report") },
                        onClick = {
                            expanded = false
                            showCommentReportDialog = true
                 }
                    )
                }
            }
        }

        if (showCommentReportDialog) {
            AlertDialog(
                onDismissRequest = { showCommentReportDialog = false },
                title = {
                    Text("Report Comment")
                },
                text = {
                    Column {
                        Text("Please enter the reason for reporting:")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = reportReason,
                            onValueChange = { reportReason = it },
                            placeholder = { Text("Enter reason...") },
                            singleLine = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {

                        val commentReport = currentUser?.let {
                            CommentReport(
                                reportingUserId = it.userId,
                                reportedUserId = selectedRecipe.userId,
                                reportedCommentId = comment.commentId,
                                reason = reportReason
                            )
                        }
                        if (commentReport != null) {
                            commentReportsViewModel.submitReport(commentReport)
                            showCommentReportDialog = false
                            reportReason = ""
                        }

                    }) {
                        Text("Submit")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showCommentReportDialog = false
                        reportReason = ""
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }

    }

}



fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Change format as needed
    val date = Date(timestamp)  // Convert timestamp to Date object
    return sdf.format(date)  // Format and return as string
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
private fun CreatorInfo(
    modifier: Modifier = Modifier,
    recipeUser: User? = null,
    recipe: Recipe? = null
) {

    val imageUri = recipeUser?.profileImageUrl
    val painter = if (imageUri?.isNotBlank() == true) {
        rememberAsyncImagePainter(model = imageUri)
    } else {
        painterResource(R.drawable.account)
    }

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
                    painter = painter,
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s))
            ) {
                Text(
                    text = recipeUser?.username ?: "Veance",
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
                        text =  "VietNam",
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
            if (recipe != null) {
                OutlinedTextField(
                    value = recipe.story ?: "",
                    onValueChange = {},
                    placeholder = { Text("Story behind this dish ...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.spacing_s) * 15)
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_m)))
                        .background(colorResource(R.color.text_field_background)),
                    enabled = false, // This disables the field
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
            else
            {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Story behind this dish ...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.spacing_s) * 15)
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_m)))
                        .background(colorResource(R.color.text_field_background)),
                    enabled = false, // This disables the field
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
}

@Composable
private fun DishProperties(
    modifier: Modifier = Modifier,
    recipe: Recipe? = null
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
            if (recipe != null) {
                IconAndText(
                    imageVector = FontAwesomeIcons.Regular.Clock,
                    color = Color((0xFF3F764E)),
                    contentDescription = "Cook Time",
                    text = recipe.cookingTime,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_medium))
                )
            }
            else
            {
                IconAndText(
                    imageVector = FontAwesomeIcons.Regular.Clock,
                    color = Color((0xFF3F764E)),
                    contentDescription = "Cook Time",
                    text = "30'",
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_medium))
                )
            }

            Spacer(Modifier.width(dimensionResource(R.dimen.spacing_xl)))

            if (recipe != null) {
                IconAndText(
                    imageVector = FontAwesomeIcons.Regular.Hourglass,
                    color = Color((0xFF3F764E)),
                    contentDescription = "Serving size Time",
                    text = recipe.servingSize,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_medium))
                )
            }
            else
            {
                IconAndText(
                    imageVector = FontAwesomeIcons.Regular.Hourglass,
                    color = Color((0xFF3F764E)),
                    contentDescription = "Serving size Time",
                    text = "4 people",
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_medium))
                )
            }



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

                    LazyRow {
                        items(step.imageUrl) { image ->

                            val painter = if (image.isNotBlank()) {
                                rememberAsyncImagePainter(model = image)
                            } else {
                                painterResource(R.drawable.mockrecipeimage)
                            }

                            Image(
                                painter = painter,
                                contentDescription = "Cooking step image",
                                contentScale = ContentScale.Crop, // 👈 quan trọng nè!
                                modifier = Modifier
                                    .width(dimensionResource(R.dimen.image_size_large))
                                    .height(dimensionResource(R.dimen.image_size_medium))
                                    .padding(end = dimensionResource(R.dimen.spacing_xs))
                                    .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_s))) // nếu muốn bo góc nữa
                            )

                        }
                    }

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
        navController = rememberNavController(),
        id="abc"
    )
}