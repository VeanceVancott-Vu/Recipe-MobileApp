package com.example.dacs_3

import AddRecipeScreen
import DACS_3Theme
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.cloudinary.imageupload.CloudinaryUploader
import com.example.dacs_3.cloudinary.imageupload.UploadImageScreen
import com.example.dacs_3.navigation.AppNavigation
import com.example.dacs_3.ui.theme.main.NotificationsAndKitchenBuddies
import com.example.dacs_3.ui.theme.main.PersonalFood
import com.example.dacs_3.ui.theme.main.RecipeDetailScreen
import com.example.dacs_3.viewModel.RecipeViewModel
import com.example.dacs_3.viewmodel.AuthViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel() // Tạo ViewModel
            val recipeViewModel: RecipeViewModel = viewModel() // Tạo ViewModel

//            DACS_3Theme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//
//
//                    // Q:
//
                     AppNavigation(navController, authViewModel,recipeViewModel)
  //                  AddRecipeScreen()
//                    IconDropdownMenuSample()
//                }
//            }

//            DACS_3Theme {
//                Surface {
//                    UploadImageScreen()
//                }
//            }


        }
    }
}