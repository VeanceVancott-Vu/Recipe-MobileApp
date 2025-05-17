package com.example.dacs_3.viewmodel

import CooksnapRepository
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.cloudinary.imageupload.CloudinaryUploader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditCooksnapViewModel : ViewModel() {

    private val repository = CooksnapRepository()

    private val _sending = MutableStateFlow(false)
    val sending: StateFlow<Boolean> = _sending.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success.asStateFlow()

    /**
     * Upload ảnh lên Cloudinary từ Uri.
     * Sau khi upload thành công, gọi onSuccess với url ảnh.
     * Nếu lỗi, gọi onError.
     */
    fun uploadImageFromUri(
        context: Context,
        uri: Uri,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        _sending.value = true
        _error.value = null

        // Giả sử bạn có class CloudinaryUploader tương tự trong project
        CloudinaryUploader.uploadImageFromUri(
            context = context,
            uri = uri,
            uploadPreset = "koylin_unsigned",
            onSuccess = { imageUrl ->
                _sending.value = false
                onSuccess(imageUrl)
            },
            onError = { e ->
                _sending.value = false
                _error.value = "Upload ảnh thất bại: ${e.message}"
                onError(e)
            }
        )
    }

    /**
     * Cập nhật Cooksnap trong Firestore.
     * Gọi reset() để đặt lại trạng thái khi cần.
     */
    fun updateCooksnap(cooksnapId: String, imageUrl: String, description: String) {
        if (cooksnapId.isBlank()) {
            _error.value = "ID Cooksnap không hợp lệ"
            return
        }
        if (description.isBlank()) {
            _error.value = "Mô tả không được để trống"
            return
        }

        _sending.value = true
        _error.value = null
        _success.value = false

        viewModelScope.launch {
            try {
                repository.updateCooksnapDocument(cooksnapId, imageUrl, description)
                _success.value = true
            } catch (e: Exception) {
                _error.value = "Cập nhật thất bại: ${e.message}"
            } finally {
                _sending.value = false
            }
        }
    }

    /**
     * Reset trạng thái error, success, sending
     */
    fun reset() {
        _error.value = null
        _success.value = false
        _sending.value = false
    }
}
