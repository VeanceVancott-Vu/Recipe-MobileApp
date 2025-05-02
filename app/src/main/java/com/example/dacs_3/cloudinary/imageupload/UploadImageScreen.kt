package com.example.dacs_3.cloudinary.imageupload

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun UploadImageScreen() {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> selectedImageUri = uri }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Chọn ảnh")
        }

        selectedImageUri?.let { uri ->
            Button(onClick = {
                CloudinaryUploader.uploadImageFromUri(
                    context = context,
                    uri = uri,
                    uploadPreset = "koylin_unsigned", // Thay preset unsigned
                    onSuccess = { uploadedImageUrl = it },
                    onError = { e -> Log.e("Cloudinary", "Upload failed", e) }
                )
            }) {
                Text("Tải lên Cloudinary")
            }
        }


        uploadedImageUrl?.let { url ->
            Text("Ảnh sau khi upload:")
            AsyncImage(
                model = url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }
    }
}
