package com.example.dacs_3

import DACS_3Theme
import ai.codia.x.composeui.demo.AddRecipeScreen
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.dacs_3.cloudinary.imageupload.CloudinaryUploader
import com.example.dacs_3.cloudinary.imageupload.UploadImageScreen
import com.example.dacs_3.ui.theme.main.DishSuggestionByIngredient
import com.example.dacs_3.ui.theme.main.NotificationsAndKitchenBuddies
import com.example.dacs_3.ui.theme.main.PersonalFood
import com.example.dacs_3.ui.theme.main.RecipeDetailScreen


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
//            DACS_3Theme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//
//                    val navController = rememberNavController()
//
//                    // Q:
//                    // val authViewModel: AuthViewModel = viewModel() // Tạo ViewModel
//
//                    // AppNavigation(navController, authViewModel)
//
//                    IconDropdownMenuSample()
//                }
//            }

            DACS_3Theme {
                Surface {
                    UploadImageScreen()
                }
            }


        }
    }
}