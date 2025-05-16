package com.example.dacs_3.ui.theme.auth

import DACS_3Theme
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
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
import kotlin.math.log


@Composable

fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    val authResult by authViewModel.authResult.collectAsState()
    val userRole by authViewModel.userRole.collectAsState()
    val loginAttempted by authViewModel.loginAttempted.collectAsState()



    Box(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth() // ✅ Set width to full
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()), // ✅ Now inside Box with height constraints
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(180.dp))

            Text(
                text = "Welcome Back",
                color = Color(0xff3f764e),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Login to your account",
                color = Color(0xff6a6363),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            InputField(
                label = "Email",
                text = email,
                onTextChange = { email = it },
                isPassword = false,
                icon = R.drawable.email
            )

            Spacer(modifier = Modifier.height(16.dp))

            InputField(
                label = "Password",
                text = password,
                onTextChange = { password = it },
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible },
                icon = R.drawable.password
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = passwordVisible,
                    onCheckedChange = { passwordVisible = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xff3f764e),
                        uncheckedColor = Color(0xff9ab0a3),
                        checkmarkColor = Color.White
                    ),
                    modifier = Modifier
                        .scale(0.7f)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text("Show Password", fontSize = 16.sp, color = Color(0xff9ab0a3))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xff3f764e),
                            uncheckedColor = Color(0xff9ab0a3),
                            checkmarkColor = Color.White
                        ),
                        modifier = Modifier
                            .scale(0.7f)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("Remember me", fontSize = 16.sp, color = Color(0xff9ab0a3))
                    Spacer(modifier = Modifier.width(20.dp))

                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Forgot Password?",
                        fontSize = 16.sp,
                        color = Color(0xff3b684d),
                        modifier = Modifier.clickable {
                            navController.navigate("forgot_password")
                        }
                    )

                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color(0xe83f764e), RoundedCornerShape(50.dp))
                    .clickable {
                        authViewModel.logout()
                        Log.d("LoginScreen", "Login button clicked with email: $email")
                        authViewModel.login(email, password)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Login", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = buildAnnotatedString {
                    append("Don’t have an account? ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            color = Color(0xff3b684d)
                        )
                    ) {
                        append("Sign up")
                    }
                },
                fontSize = 16.sp,
                color = Color(0xff3b684d),
                modifier = Modifier.clickable {
                    navController.navigate("signup")
                }
            )
        }

        // Trigger role loading only after login was attempted and successful
        LaunchedEffect(authResult, loginAttempted) {
            if (loginAttempted && authResult.first) {
                authViewModel.loadUserRole()
            }
        }

        // Navigate only after role is loaded AND login was user-triggered
        LaunchedEffect(userRole, loginAttempted) {
            if (loginAttempted && userRole != null) {
                when (userRole) {
                    "Admin" -> {
                        Log.d("LoginScreen", "Navigating to admin dashboard")
                        navController.navigate("admin_dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    }

                    "User" -> {
                        Log.d("LoginScreen", "Navigating to homepage")
                        navController.navigate("homepage") {
                            popUpTo("login") { inclusive = true }
                        }
                    }

                    else -> {
                        Log.w("LoginScreen", "Unknown role: $userRole, defaulting to homepage")
                        navController.navigate("homepage") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
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
        isPassword: Boolean = false,
        passwordVisible: Boolean = false,
        onPasswordToggle: (() -> Unit)? = null,
        icon: Int? = null
    ) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text(label, fontSize = 18.sp, color = Color(0xff9ab0a3)) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xa8dbe6de), RoundedCornerShape(16.dp)),
            singleLine = true,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text),
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

    @Preview(showBackground = true)
    @Composable
    fun PreviewLoginView() {
        DACS_3Theme {
            val navController = rememberNavController() // Mock NavController for preview
            val authViewModel = remember { AuthViewModel() } // Mock AuthViewModel for preview() // Mock NavController for preview
            LoginScreen(navController,authViewModel)
     
        }
    }



