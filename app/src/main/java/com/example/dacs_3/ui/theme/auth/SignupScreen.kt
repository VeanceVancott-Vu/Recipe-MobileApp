package com.example.dacs_3.ui.theme.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.R
import com.example.dacs_3.viewmodel.AuthViewModel

@Composable
fun SignupScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    
    val authResult by AuthViewModel().authResult.observeAsState()

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

            Spacer(modifier = Modifier.height(24.dp))

            InputField(label = "Full Name",text = fullName ,onTextChange = { fullName = it }, icon = R.drawable.fullname)
            Spacer(modifier = Modifier.height(16.dp))

            InputField(label = "Email", text = email ,onTextChange = { email = it }, icon = R.drawable.email)
            Spacer(modifier = Modifier.height(16.dp))

            InputField(label = "Password",text = password ,onTextChange = { password = it }, icon = R.drawable.check)
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* Handle signup */
                Log.d("Sign up info","Full Name: $fullName" + "Email: $email"+"Password: $password")

                    AuthViewModel().signUp(email, password)

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

            Text(
                text = "Or continue with",
                color = Color(0xff3b684d),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                SocialLoginIcon(R.drawable.facebook)
                SocialLoginIcon(R.drawable.google)
                SocialLoginIcon(R.drawable.apple)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Already have an account? Sign in",
                color = Color(0xff9ab0a3),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
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
fun InputField(label: String, icon: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xa8dbe6de))
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = label,
            color = Color(0xff9ab0a3),
            fontSize = 16.sp,
            textAlign = TextAlign.Start
        )
    }
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
    SignupScreen(navController)
}
