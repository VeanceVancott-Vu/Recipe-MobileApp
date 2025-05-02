package com.example.dacs_3.cloudinary.imageupload

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.Cloudinary
import java.io.File

object CloudinaryUploader {

    // Thêm api_secret vào cấu hình Cloudinary
    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "drxb00t5h",  // Tên cloud của bạn
            "api_key" to "272947454727268",  // API key của bạn
            "api_secret" to "7sqBRXddc7hWBQa8IvlTFySuovs" // API secret của bạn
        )
    )

    fun uploadImageFromUri(
        context: Context,
        uri: Uri,
        uploadPreset: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir).apply {
            outputStream().use { out -> inputStream?.copyTo(out) }
        }

        Thread {
            try {
                val result = cloudinary.uploader().upload(
                    tempFile,
                    mapOf("upload_preset" to uploadPreset)  // Sử dụng upload preset của bạn
                )
                val imageUrl = result["secure_url"] as String
                Log.d("Cloudinary", "Image uploaded successfully: $imageUrl")
                onSuccess(imageUrl)  // Trả về URL của ảnh đã upload
            } catch (e: Exception) {
                Log.e("Cloudinary", "Upload failed", e)
                onError(e)  // Log lỗi nếu upload thất bại
            }
        }.start()
    }

}