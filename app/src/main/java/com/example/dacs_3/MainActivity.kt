package com.example.dacs_3

import DACS_3Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier


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
                Surface(modifier = Modifier.fillMaxSize()) {

                }
            }

        }
    }
}
