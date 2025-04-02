package com.example.dacs_3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dacs_3.ui.theme.auth.ForgotPasswordScreen
import com.example.dacs_3.ui.theme.auth.LoginScreen
import com.example.dacs_3.ui.theme.auth.SignupScreen
import com.example.dacs_3.ui.theme.main.HomePageScreen
import com.example.dacs_3.viewmodel.AuthViewModel

@Composable
fun AppNavigation(navController: NavHostController, authViewModel: AuthViewModel) {
    val userId = authViewModel.getCurrentUserId() // Lấy UID của user đang đăng nhập

    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("homepage") { HomePageScreen(navController, userId) } // Truyền UID vào HomePage
        composable("forgot_password") { ForgotPasswordScreen(navController) }
    }
}

