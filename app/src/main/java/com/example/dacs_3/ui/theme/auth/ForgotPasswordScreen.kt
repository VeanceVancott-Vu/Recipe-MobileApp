package com.example.dacs_3.ui.theme.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.R

@Composable
fun ForgotPasswordScreen(
    navController: NavController

) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(90.dp))

        Text(
            text = "Forgot Password",
            color = Color(0xff3f764e),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Enter your email address and we will send you a link to reset your password.",
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xe83f764e), RoundedCornerShape(50.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("Send Code", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                "Back to Login",
                fontSize = 16.sp,
                color = Color(0xff3b684d),
                fontWeight = FontWeight.Bold,  // In đậm
                textDecoration = TextDecoration.Underline,  // Gạch chân
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
                    .clickable {
                        navController.navigate("login")  // Điều hướng đúng về màn hình Login
                    }
            )


        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    val navController = rememberNavController()  // Thêm dòng này
    ForgotPasswordScreen(navController = navController)
}

