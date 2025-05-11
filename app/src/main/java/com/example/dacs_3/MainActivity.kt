package com.example.dacs_3

import DACS_3Theme
import MyProfileScreen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.navigation.AppNavigation
import com.example.dacs_3.ui.theme.main.RecipeDetailScreen
import com.example.dacs_3.ui.theme.uploadSampleRecipeToFirestore
import com.example.dacs_3.utils.askForLocationPermission
import com.example.dacs_3.viewmodel.RecipeViewModel
import com.example.dacs_3.viewmodel.CommentViewModel
import com.example.dacs_3.viewmodel.AuthViewModel


class MainActivity : ComponentActivity() {
    // Lấy ViewModel AuthViewModel
    private val authViewModel: AuthViewModel by viewModels()

    // Register for permissions result
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Quyền đã được cấp, tiếp tục xử lý
                askForLocationPermission(this) { location ->
                    // Xử lý địa chỉ người dùng nếu cần
                }
            } else {
                // Nếu quyền bị từ chối, thông báo cho người dùng
                Toast.makeText(this, "Cần cấp quyền vị trí để tiếp tục", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel() // Tạo ViewModel
            val recipeViewModel: RecipeViewModel = viewModel() // Tạo ViewModel
            val commentViewModel: CommentViewModel = viewModel() // Tạo ViewModel

            DACS_3Theme {
                Surface {
//                    AppNavigation(navController = navController)

//                    EditProfileScreen(
//                        authViewModel = authViewModel,
//                        navController = navController
//                    )
//
//                    MockUserFollowSeeder.seedMockData()

                    // Danh sách bạn của người khác
//                    FriendListScreen(
//                        viewModel = viewModel(), // Automatically creates or retrieves FriendListViewModel
//                        currentUserId = "user2", // Replace with actual user ID
//                        targetUserId = "user1" // Replace with actual user ID
//                    )


                    // Danh sách người theo dõi của người khác
//                    FollowerListScreen(
//                        viewModel = viewModel(), // Automatically creates or retrieves FriendListViewModel
//                        currentUserId = "user2", // Replace with actual user ID
//                        targetUserId = "user1"
//                    )

//                    FollowingScreen()

//                    FollowStatusScreen()

            //        MyProfileScreen(navController = navController)
                    AppNavigation(navController, authViewModel,recipeViewModel,commentViewModel)

                }
            }


        }
    }
}