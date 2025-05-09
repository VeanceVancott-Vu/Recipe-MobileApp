package com.example.dacs_3.ui.theme.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.R
import com.example.dacs_3.viewmodel.AuthViewModel
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.io.Files.append

@Composable
fun SignupScreen(navController: NavController,  authViewModel: AuthViewModel ) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    val authResult by authViewModel.authResult.collectAsState()


    // Q: Remember me
    var rememberMe by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(80.dp))


            Text(
                text = "Register",
                color = Color(0xff3f764e),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )


            Text(
                text = "Create your new account",
                color = Color(0xff9ab0a3),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(62.dp))


            InputField(
                label = "Full Name",
                text = fullName,
                onTextChange = { fullName = it },
                icon = R.drawable.fullname
            )


            Spacer(modifier = Modifier.height(16.dp))


            InputField(label = "Email", text = email ,onTextChange = { email = it }, icon = R.drawable.email)
            Spacer(modifier = Modifier.height(16.dp))


            InputField(label = "Password",text = password ,onTextChange = { password = it }, icon = R.drawable.password)
            Spacer(modifier = Modifier.height(32.dp))


            Button(
                onClick = { /* Handle signup */
                    Log.d("Sign up info","Full Name: $fullName" + "Email: $email"+"Password: $password")


                    authViewModel.signUp(email, password,fullName)


                },
                colors = ButtonDefaults.buttonColors(Color(0xe83f764e)),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Sign Up", color = Color.White, fontSize = 18.sp)
            }


            Spacer(modifier = Modifier.height(16.dp))


            // Q: Remember me
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xff3f764e)),
                    modifier = Modifier
                        .scale(0.7f) // Giảm kích thước checkbox
                )


                Spacer(modifier = Modifier.width(8.dp))


                Text(
                    text = "Remember Me",
                    fontSize = 14.sp,
                    color = Color(0xff3b684d),
                    modifier = Modifier.clickable { rememberMe = !rememberMe }
                )
            }


            Spacer(modifier = Modifier.height(64.dp))




            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                )


                Text(
                    text = "Or continue with",
                    color = Color(0xff3b684d),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp) // Tạo khoảng cách giữa text và hr
                )


                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                )
            }


            Spacer(modifier = Modifier.height(32.dp))


            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                SocialLoginIcon(R.drawable.facebook)
                SocialLoginIcon(R.drawable.google)
                SocialLoginIcon(R.drawable.apple)
            }


            Spacer(modifier = Modifier.height(64.dp))


            Text(
                buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold, // In đậm
                            textDecoration = TextDecoration.Underline, // Gạch chân
                            color = Color(0xff3b684d) // Màu nổi bật hơn cho dễ nhìn
                        )
                    ) {
                        append("Sign in")
                    }
                },
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color(0xff9ab0a3),
                modifier = Modifier.clickable {
                    navController.navigate("login")
                }
            )




            authResult?.let { (success, message) ->
                if (message != null) {
                    Log.d("SignupScreen", "Success value: $success")
                }
                else
                {
                    Log.d("SignupScreen", "Fail value: $success")
                }
            }


        }
    }
}


@Composable
fun InputField(
    label: String,
    text: String,
    onTextChange: (String) -> Unit,
    icon: Int
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        placeholder = { Text(label, fontSize = 18.sp, color = Color(0xff9ab0a3)) },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xa8dbe6de), RoundedCornerShape(16.dp)),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xa8dbe6de),
            unfocusedContainerColor = Color(0xa8dbe6de),
            disabledContainerColor = Color(0xa8dbe6de),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            disabledTextColor = Color.Black,
            errorContainerColor = Color(0xa8dbe6de)
        ),
        textStyle = TextStyle(color = Color.Black),
        leadingIcon = icon?.let {
            {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = Color(0xFF3B684D),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    )
}


@Composable
fun SocialLoginIcon(icon: Int) {
    Image(
        painter = painterResource(id = icon),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier.size(50.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewSignUpView() {
    val navController = rememberNavController() // Mock NavController for preview
    val authViewModel = remember { AuthViewModel() } // Mock AuthViewModel for preview() // Mock NavController for preview

    SignupScreen(navController,authViewModel)
}

