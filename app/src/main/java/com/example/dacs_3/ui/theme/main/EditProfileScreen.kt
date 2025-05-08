import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.R
import com.example.dacs_3.utils.TopBar
import com.example.dacs_3.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(authViewModel: AuthViewModel = viewModel(),
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

                val user by authViewModel.currentUser.collectAsState()
                Log.d("Edit profile Screen", "user: $user")
                LaunchedEffect(Unit) {
                    authViewModel.fetchAndSetCurrentUser()
                }

                var name by remember { mutableStateOf(user?.let { TextFieldValue(it.username) }) }
                var id by remember { mutableStateOf(user?.let { TextFieldValue(it.userId) }) }
                var email by remember { mutableStateOf(user?.let { TextFieldValue(it.email) }) }
                var location by remember { mutableStateOf(TextFieldValue("Viet Nam")) }
                var about by remember { mutableStateOf(TextFieldValue("Burned the kitchen 3 times, still call myself a Master Chef. Anyone wanna save my pasta?")) }


                // Profile Image
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.mockrecipeimage),
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(100.dp))
                    )
                    Image(
                        painter = painterResource(id = R.drawable.mockrecipeimage),
                        contentDescription = "Camera",
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .size(35.dp)
                    )
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
