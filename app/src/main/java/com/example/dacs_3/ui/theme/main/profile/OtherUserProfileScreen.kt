package com.example.dacs_3.ui.theme.main.profile

import BioCard
import ProfileHeader
import SectionCard
import UserReportViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dacs_3.R
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.ui.theme.main.DishCard
import com.example.dacs_3.utils.TopBar
import com.example.dacs_3.viewmodel.OtherUserProfileViewModel
import com.example.dacs_3.viewmodel.UserRecipesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dacs_3.model.UserReport
import com.example.dacs_3.viewmodel.AuthViewModel
import com.example.dacs_3.viewmodel.RecipeViewModel


@Composable
fun OtherUserProfileScreen(
    navController: NavController,
    userId: String,
    viewModel: OtherUserProfileViewModel = viewModel(),
    recipeViewModel: RecipeViewModel,
    userReportViewModel: UserReportViewModel,
    userViewModel: AuthViewModel,
) {

    val userState by viewModel.user.collectAsState()

    val currentUserId = userViewModel.currentUserUid



    // Load user khi userId thay đổi
    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }
    val userRecipes by recipeViewModel.recipesByUserId.collectAsState()

    LaunchedEffect(Unit) {
        recipeViewModel.fetchRecipeByUserId(userId)
    }

    var itemsToShow by remember { mutableIntStateOf(3) }


    var showDialog by remember { mutableStateOf(false) }
    var reportReason by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopBar("My profile", showRightIcon = false,
                onBackIconClick = {navController.popBackStack()} )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.systemBars.asPaddingValues())
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
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {showDialog = true},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F764E))
                ) {
                    Text(
                        text = "Report this user",
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
                        userRecipes.take(itemsToShow).forEach { dish ->
                            DishCard(
                                dish,
                                Modifier.padding(vertical = dimensionResource(R.dimen.spacing_m))
                                    .clickable { navController.navigate("recipe_detail/${dish.recipeId}") }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Nút "See more" chỉ hiện khi còn món chưa hiển thị
                        if (itemsToShow < userRecipes.size) {
                            Button(
                                onClick = {
                                    itemsToShow = (itemsToShow + 3).coerceAtMost(userRecipes.size)
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


//
//                    Text(
//                        text = "Cooksnap",
//                        fontSize = 24.sp,
//                        color = OliverGreen,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Box(
//                        modifier = Modifier.fillMaxWidth()
//                            .height(300.dp)
//
//                    ) {
//
//                        CooksnapGrid(
//                            cooksnapList = cooksnaps,
//                            usersMap = usersMap
//                        )
//                    }
                }
            }
        }
    )


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Report User") },
            text = {
                Column {
                    Text("Please enter the reason for reporting:")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = reportReason,
                        onValueChange = { reportReason = it },
                        placeholder = { Text("Reason...") },
                        singleLine = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    if (reportReason.isNotBlank()) {

                        val report = currentUserId?.let {
                            UserReport(
                                reportingUserId = it,
                                reportedUserId = userId,
                                reason = reportReason,
                            )
                        }
                        if (report != null) {
                            userReportViewModel.submitUserReport(report)
                        }
                    }
                }) {
                    Text("Submit")
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
}

@Preview(showBackground = true)
@Composable
private fun OtherUserProfileScreenPreview() {
//    OtherUserProfileScreen(
//        navController = rememberNavController()
//    )
}
