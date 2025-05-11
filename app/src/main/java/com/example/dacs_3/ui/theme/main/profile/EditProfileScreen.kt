import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.dacs_3.R
import com.example.dacs_3.cloudinary.imageupload.CloudinaryUploader
import com.example.dacs_3.utils.TopBar
import com.example.dacs_3.viewmodel.AuthViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Camera

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopBar("My profile", showRightIcon = false)
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = innerPadding.calculateTopPadding() - 35.dp)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //  Lắng nghe (observe) sự thay đổi dữ liệu người dùng từ ViewModel
                val user by authViewModel.currentUser.collectAsState()

                Log.d("Edit profile Screen", "user: $user")
                LaunchedEffect(Unit) {
                    // Lấy dữ liệu người dùng từ ViewModel khi màn hình được hiển thị lần đầu.
                    authViewModel.fetchAndSetCurrentUser()


                }

                var name by remember { mutableStateOf(user?.let { TextFieldValue(it.username) }) }
                var id by remember { mutableStateOf(user?.let { TextFieldValue(it.userId) }) }
                var email by remember { mutableStateOf(user?.let { TextFieldValue(it.email) }) }
                var location by remember { mutableStateOf(TextFieldValue("Viet Nam")) }
                var about by remember { mutableStateOf(TextFieldValue("Burned the kitchen 3 times, still call myself a Master Chef. Anyone wanna save my pasta?")) }

                val context = LocalContext.current
                var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = { uri -> selectedImageUri = uri }
                )

                // Profile Image
                Box(contentAlignment = Alignment.Center) {
                    user?.profileImageUrl?.let { imageUrl ->
                        // Log hình ảnh mỗi khi URL thay đổi
                        Log.d("EditProfileScreen", "Profile Image URL: $imageUrl")

                        AsyncImage(
                            model = imageUrl,  // URL ảnh đã tải lên Cloudinary
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(100.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } ?: run {
                        // Nếu không có URL, hiển thị một ảnh mặc định hoặc placeholder
                        Log.d("EditProfileScreen", "No profile image URL found")
                        // Bạn có thể thay thế ảnh mặc định ở đây
                        AsyncImage(
                            model = R.drawable.mockrecipeimage ,  // Sử dụng ảnh mặc định nếu không có URL
                            contentDescription = "Default Profile Image",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(100.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Image(
                        imageVector = FontAwesomeIcons.Solid.Camera,
                        contentDescription = "Camera",
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .size(35.dp)
                            .clickable {
                                launcher.launch("image/*")  // Mở trình chọn ảnh
                            }
                    )

                    // Xử lý upload ảnh tự động khi selectedImageUri thay đổi
                    LaunchedEffect(selectedImageUri) {
                        selectedImageUri?.let { uri ->
                            CloudinaryUploader.uploadImageFromUri(
                                context = context,
                                uri = uri,
                                uploadPreset = "koylin_unsigned",
                                onSuccess = { imageUrl ->
                                    user?.let {

                                        authViewModel.updateProfileImageUrl(it.userId, imageUrl) { success ->
                                            if (success) {
                                                Log.d("EditProfileScreen", "Profile image URL updated in Firebase")
                                            } else {
                                                Log.e("EditProfileScreen", "Failed to update profile image URL")
                                            }
                                        }
                                    }
                                },
                                onError = { e ->
                                    Log.e("Cloudinary", "Upload failed", e)
                                }
                            )
                        }
                    }


                }

                Spacer(modifier = Modifier.height(32.dp))
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                )
                {
                    name?.let { EditableField("Your Name", it) { name = it } }
                    id?.let { EditableField("ID Cookpad", it) { id = it } }
                    email?.let { EditableField("Email", it) { email = it } }
                    EditableField("Come From", location) { location = it }

                    Spacer(modifier = Modifier.height(16.dp))

                    // About me
                    Text(
                        text = "About Me",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xff3b684d),
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 4.dp)
                    )

                    TextField(
                        value = about,
                        onValueChange = { about = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xa8dbe6de), RoundedCornerShape(25.dp)),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            color = Color(0xff9ab0a3)
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Update button


                    Spacer(modifier = Modifier.height(64.dp))
                }

                Button(
                    onClick = {
                        user?.let { currentUser ->
                            val updatedUser = currentUser.copy(
                                username = name?.text ?: currentUser.username,
                                email = email?.text ?: currentUser.email,
                                userId = id?.text ?: currentUser.userId
                                // Add other fields like location, about if your data class supports them
                            )

                            authViewModel.updateUser(updatedUser) { success ->
                                if (success) {
                                    Log.d("EditProfileScreen", "User updated successfully")
                                    navController.navigate("my_profile")
                                } else {
                                    Log.d("EditProfileScreen", "Failed to update user")
                                }
                            }
                        } ?: Log.e("EditProfileScreen", "User is null, cannot update.")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff3f764e)),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .height(56.dp)
                        .width(200.dp)
                ) {
                    Text(
                        text = "Update",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }

            }
        }


    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableField(label: String, value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    Text(
        text = label,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xff3b684d),
        modifier = Modifier
            .padding(top = 16.dp, bottom = 4.dp)

    )
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xa8dbe6de), RoundedCornerShape(25.dp))
        ,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 18.sp,
            color = Color(0xff9ab0a3)
        ),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}



@Preview
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(navController = rememberNavController())
}

