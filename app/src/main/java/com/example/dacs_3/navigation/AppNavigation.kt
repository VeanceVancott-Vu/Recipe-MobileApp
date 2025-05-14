package com.example.dacs_3.navigation

import AddRecipeScreen
import EditProfileScreen
import MyProfileScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dacs_3.ui.theme.auth.ForgotPasswordScreen
import com.example.dacs_3.ui.theme.auth.LoginScreen
import com.example.dacs_3.ui.theme.auth.SignupScreen
import com.example.dacs_3.ui.theme.main.CooksnapScreen
import com.example.dacs_3.ui.theme.main.DetailScreen
import com.example.dacs_3.ui.theme.main.FilterScreen
import com.example.dacs_3.ui.theme.main.HomePageScreen
import com.example.dacs_3.ui.theme.main.NotificationsAndKitchenBuddies
import com.example.dacs_3.ui.theme.main.PersonalFood
import com.example.dacs_3.ui.theme.main.RecipeDetailScreen
import com.example.dacs_3.ui.theme.main.RecipeEditScreen
import com.example.dacs_3.ui.theme.main.SearchScreen
import com.example.dacs_3.ui.theme.main.ShareCooksnapScreen
import com.example.dacs_3.viewmodel.AuthViewModel
import com.example.dacs_3.viewmodel.CommentViewModel
import com.example.dacs_3.viewmodel.RecipeViewModel


@Composable
fun AppNavigation(navController: NavHostController, authViewModel: AuthViewModel,recipeViewModel: RecipeViewModel, commentViewModel: CommentViewModel) {
    val userId = authViewModel.getCurrentUserId() // Get the current user's ID

    NavHost(navController, startDestination = "login") {
        composable("login")
        { LoginScreen(navController,authViewModel) }

        composable("signup")
        { SignupScreen(navController,authViewModel) }

        composable("homepage")
        { HomePageScreen(navController, userId,recipeViewModel) }

        composable("forgot_password")
        { ForgotPasswordScreen(navController) }

        composable("addRecipe")
        { AddRecipeScreen(recipeViewModel,navController) }

        composable("detail/{id}")
        { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            DetailScreen(id)
        }

        composable("my_profile")
        { MyProfileScreen(authViewModel,navController)
        }

        composable("edit_profile")
        {EditProfileScreen(authViewModel,navController)

        }


        composable("recipe_detail/{id}")
        {
                backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            RecipeDetailScreen(navController,id,recipeViewModel,authViewModel, commentViewModel)
        }

        composable("recipe_edit/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            RecipeEditScreen(navController, id, recipeViewModel, authViewModel)
        }

        composable("search") {
            SearchScreen(
                navController = navController,
                onBack = { /* Xử lý quay lại nếu cần */ },
                onSearch = { query -> /* Xử lý tìm kiếm */ },
                onFilter = { /* Xử lý lọc nếu cần */ }
            )
        }

        composable("filter") {
            FilterScreen(
                navController = navController,
                onBack = { /* Xử lý quay lại nếu cần */ }
            )
        }

        composable("notifications_and_kitchen_buddies") {
            NotificationsAndKitchenBuddies(
                navController = navController
            ) // Màn hình điều hướng đến
        }

        composable("personal_food") {
            PersonalFood(navController = navController)  // Điều hướng đến PersonalFood
        }

        composable("cooksnap/{id}")
        { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            CooksnapScreen(navController, id, recipeViewModel)
        }

        composable("share_cooksnap/{id}")
        { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            ShareCooksnapScreen(navController, id, recipeViewModel)
        }


    }

}

