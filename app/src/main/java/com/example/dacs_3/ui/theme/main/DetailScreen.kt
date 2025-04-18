package com.example.dacs_3.ui.theme.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DetailScreen(id: String) {
    var text by remember { mutableStateOf("") }

    LaunchedEffect(id) {
        val db = FirebaseFirestore.getInstance()
        db.collection("testData").document(id).get()
            .addOnSuccessListener { document ->
                text = document.getString("text") ?: "Không có dữ liệu"
            }
    }

    Column {
        Text(text = "ID: $id")
        Text(text = "Nội dung: $text")
    }
}
