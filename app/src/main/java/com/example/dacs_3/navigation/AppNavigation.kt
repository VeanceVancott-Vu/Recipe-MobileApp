package com.example.dacs_3.navigation

import AddRecipeScreen
import EditProfileScreen
import MyProfileScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dacs_3.ui.theme.auth.ForgotPasswordScreen
import com.example.dacs_3.ui.theme.auth.LoginScreen
import com.example.dacs_3.ui.theme.auth.SignupScreen
import com.example.dacs_3.ui.theme.main.DetailScreen
import com.example.dacs_3.ui.theme.main.HomePageScreen
import com.example.dacs_3.viewmodel.AuthViewModel


@Composable
fun AppNavigation(navController: NavHostController, authViewModel: AuthViewModel) {
    val userId = authViewModel.getCurrentUserId() // Get the current user's ID

    NavHost(navController, startDestination = "login") {
        composable("login")
        { LoginScreen(navController,authViewModel) }

        composable("signup")
        { SignupScreen(navController,authViewModel) }

        composable("homepage")
        { HomePageScreen(navController, userId) }

        composable("forgot_password")
        { ForgotPasswordScreen(navController) }

        composable("addRecipe")
        { AddRecipeScreen() }

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

    }
}

