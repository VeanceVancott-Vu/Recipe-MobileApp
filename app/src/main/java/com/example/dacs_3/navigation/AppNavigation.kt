package com.example.dacs_3.navigation

import AddRecipeScreen
import EditProfileScreen
import MyProfileScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.dacs_3.ui.theme.auth.ForgotPasswordScreen
import com.example.dacs_3.ui.theme.auth.LoginScreen
import com.example.dacs_3.ui.theme.auth.SignupScreen
import com.example.dacs_3.ui.theme.main.CooksnapScreen
import com.example.dacs_3.ui.theme.main.DetailScreen
import com.example.dacs_3.ui.theme.main.FilterScreen
import com.example.dacs_3.ui.theme.main.HomePageScreen
import com.example.dacs_3.ui.theme.main.NotificationsAndKitchenBuddies
import com.example.dacs_3.ui.theme.main.PersonalFood
import com.example.dacs_3.ui.theme.main.PersonalFood
import com.example.dacs_3.ui.theme.main.RecipeDetailScreen
import com.example.dacs_3.ui.theme.main.RecipeEditScreen
import com.example.dacs_3.ui.theme.main.SearchScreen
import com.example.dacs_3.ui.theme.main.ShareCooksnapScreen
import com.example.dacs_3.viewmodel.AuthViewModel
import com.example.dacs_3.viewmodel.CollectionsViewModel
import com.example.dacs_3.viewmodel.CommentViewModel
import com.example.dacs_3.viewmodel.RecipeViewModel
import com.example.dacs_3.viewmodel.SearchHistoryViewModel


@Composable
fun AppNavigation(navController: NavHostController, authViewModel: AuthViewModel,recipeViewModel: RecipeViewModel, commentViewModel: CommentViewModel,collectionsViewModel: CollectionsViewModel,searchHistoryViewModel: SearchHistoryViewModel) {
    val userId = authViewModel.getCurrentUserId().toString() // Get the current user's ID

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
            RecipeDetailScreen(navController,id,recipeViewModel,authViewModel, commentViewModel,collectionsViewModel)
        }

        composable("recipe_edit/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            RecipeEditScreen(navController, id, recipeViewModel, authViewModel)
        }

        composable("search") {
            SearchScreen(
                navController = navController,
                recipeViewModel = recipeViewModel,
                authViewModel = authViewModel,
                searchHistoryViewModel = searchHistoryViewModel,
                userId = userId
            )
        }

        composable("filter") {
            FilterScreen(
                navController = navController,
                onBack = { /* Xử lý quay lại nếu cần */ }
            )
        }

        composable("saved_recipe")
        {
            if (userId != null) {
                PersonalFood(collectionsViewModel = collectionsViewModel, navController = navController, userId = userId, authViewModel = authViewModel, recipeViewModel = recipeViewModel)
            }
        }

        composable("notifications_and_kitchen_buddies") {
            NotificationsAndKitchenBuddies(
                navController = navController
            ) // Màn hình điều hướng đến
        }

        composable("personal_food") {
            PersonalFood(collectionsViewModel = collectionsViewModel, navController = navController, userId = userId, recipeViewModel = recipeViewModel, authViewModel = authViewModel) // Điều hướng đến PersonalFood
        }

//        composable("cooksnap/{id}")
//        { backStackEntry ->
//            val id = backStackEntry.arguments?.getString("id") ?: ""
//            CooksnapScreen(navController, id, recipeViewModel)
//        }
//
//        composable("share_cooksnap/{id}")
//        { backStackEntry ->
//            val id = backStackEntry.arguments?.getString("id") ?: ""
//            ShareCooksnapScreen(navController, id, recipeViewModel)
//        }

        composable("cooksnap/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            CooksnapScreen(navController, id)
        }

        composable(
            "share_cooksnap/{recipeId}/{imageUrl}",
            arguments = listOf(
                navArgument("recipeId") { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
            val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
            ShareCooksnapScreen(navController, recipeId, imageUrl)
        }



    }
}

