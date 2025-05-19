package com.example.dacs_3.navigation

import AddRecipeScreen
import EditProfileScreen
import MyProfileScreen
import android.net.Uri
import UserReportViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.dacs_3.ui.theme.main.EditCooksnapScreen
import com.example.dacs_3.ui.theme.main.FilterScreen
import com.example.dacs_3.ui.theme.main.GeoTagScreen
import com.example.dacs_3.ui.theme.main.HomePageScreen
import com.example.dacs_3.ui.theme.main.LocationItem
import com.example.dacs_3.ui.theme.main.NotificationsAndKitchenBuddies
import com.example.dacs_3.ui.theme.main.PersonalFood
import com.example.dacs_3.ui.theme.main.PersonalFood
import com.example.dacs_3.ui.theme.main.RecipeDetailScreen
import com.example.dacs_3.ui.theme.main.RecipeEditScreen
import com.example.dacs_3.ui.theme.main.SearchScreen
import com.example.dacs_3.ui.theme.main.ShareCooksnapScreen
import com.example.dacs_3.ui.theme.main.admin.CommentReportsScreen
import com.example.dacs_3.ui.theme.main.admin.CooksnapReportsScreen
import com.example.dacs_3.ui.theme.main.admin.DashboardScreen
import com.example.dacs_3.ui.theme.main.admin.RecipeReportsScreen
import com.example.dacs_3.ui.theme.main.admin.UserReportsScreen
import com.example.dacs_3.ui.theme.main.admin.ViolationReportsScreen
import com.example.dacs_3.ui.theme.main.profile.FollowerListScreen
import com.example.dacs_3.ui.theme.main.profile.FollowingScreen
import com.example.dacs_3.ui.theme.main.profile.FriendListScreen
import com.example.dacs_3.ui.theme.main.profile.KitchenBuddyScreen
import com.example.dacs_3.ui.theme.main.profile.OtherUserProfileScreen
import com.example.dacs_3.viewmodel.AuthViewModel
import com.example.dacs_3.viewmodel.CollectionsViewModel
import com.example.dacs_3.viewmodel.CommentReportsViewModel
import com.example.dacs_3.viewmodel.CommentViewModel
import com.example.dacs_3.viewmodel.NotificationViewModel
import com.example.dacs_3.viewmodel.RecipeReportsViewModel
import com.example.dacs_3.viewmodel.RecipeViewModel
import com.example.dacs_3.viewmodel.SearchHistoryViewModel


@Composable
fun AppNavigation(navController: NavHostController,
                  authViewModel: AuthViewModel,
                  recipeViewModel: RecipeViewModel,
                  commentViewModel: CommentViewModel,
                  collectionsViewModel: CollectionsViewModel,
                  searchHistoryViewModel: SearchHistoryViewModel,
                  recipeReportsViewModel : RecipeReportsViewModel,
                  notificationViewModel: NotificationViewModel,
                  commentReportsViewModel: CommentReportsViewModel,
                  userReportViewModel: UserReportViewModel
) {
    val userId = authViewModel.getCurrentUserId().toString() // Get the current user's ID
    val userRole by authViewModel.userRole.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.loadUserRole()
        authViewModel.loadAllUsers()
    }

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

        composable(
            "other_user_profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            OtherUserProfileScreen(navController = navController, userId = userId, userReportViewModel = userReportViewModel , recipeViewModel = recipeViewModel,userViewModel = authViewModel)
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
                navController = navController,
                viewModel = notificationViewModel
            ) // Màn hình điều hướng đến
        }

        composable("personal_food") {
            PersonalFood(collectionsViewModel = collectionsViewModel, navController = navController, userId = userId, recipeViewModel = recipeViewModel, authViewModel = authViewModel) // Điều hướng đến PersonalFood
        }

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

        composable(
            "edit_cooksnap/{cooksnapId}/{imageUrl}/{description}",
            arguments = listOf(
                navArgument("cooksnapId") { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val cooksnapId = backStackEntry.arguments?.getString("cooksnapId") ?: ""
            val imageUrl = Uri.decode(backStackEntry.arguments?.getString("imageUrl") ?: "")
            val description = Uri.decode(backStackEntry.arguments?.getString("description") ?: "")
            EditCooksnapScreen(navController, cooksnapId, imageUrl, description)
        }



        composable(
            route = "kitchen_buddy/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            KitchenBuddyScreen(userId = userId ?: "")
        }

        composable(
            route = "following/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            FollowingScreen(userId = userId ?: "")
        }

        composable(
            route = "friendlist/{currentUserId}/{targetUserId}",
            arguments = listOf(
                navArgument("currentUserId") { type = NavType.StringType },
                navArgument("targetUserId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val currentUserId = backStackEntry.arguments?.getString("currentUserId") ?: ""
            val targetUserId = backStackEntry.arguments?.getString("targetUserId") ?: ""
            FriendListScreen(
                viewModel = viewModel(),
                currentUserId = currentUserId,
                targetUserId = targetUserId
            )
        }

        composable(
            route = "followerlist/{currentUserId}/{targetUserId}",
            arguments = listOf(
                navArgument("currentUserId") { type = NavType.StringType },
                navArgument("targetUserId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val currentUserId = backStackEntry.arguments?.getString("currentUserId") ?: ""
            val targetUserId = backStackEntry.arguments?.getString("targetUserId") ?: ""
            FollowerListScreen(
                viewModel = viewModel(),
                currentUserId = currentUserId,
                targetUserId = targetUserId
            )
        }


        composable("admin_dashboard") {
            DashboardScreen(
                navController,
                recipeViewModel ,
                authViewModel,
                commentReportsViewModel ,
                recipeReportsViewModel
            )
        }

        composable("violation_reports") {
            ViolationReportsScreen(
                navController = navController,
                commentReportsViewModel =commentReportsViewModel,
                recipeReportsViewModel = recipeReportsViewModel,
                userReportViewModel = userReportViewModel

            ) // Màn hình điều hướng đến
        }
        composable("comment_report") {
            CommentReportsScreen(
                navController = navController,
                commentViewModel = commentViewModel,
                commentReportViewModel = commentReportsViewModel


            )
        }
        composable("recipe_report") {
            RecipeReportsScreen(
                navController = navController,
                recipeReportsViewModel = recipeReportsViewModel,
                recipeViewModel = recipeViewModel

            )
        }
        composable("user_report") {
            UserReportsScreen(
                navController = navController,
                userReportViewModel = userReportViewModel
                , authViewModel = authViewModel

            )
        }
        composable("cooksnap_report") {
            CooksnapReportsScreen(
                navController = navController,


                )
        }

        composable("geotag_screen") {
            val users by authViewModel.allUsers.collectAsState()

            val locationItems = users.map { user ->
                LocationItem(name = user.username, address = user.location)
            }
            GeoTagScreen(locationItems,navController)
        }


    }
}

