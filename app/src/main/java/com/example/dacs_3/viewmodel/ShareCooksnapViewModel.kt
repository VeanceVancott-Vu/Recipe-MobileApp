package com.example.dacs_3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.dacs_3.model.Cooksnap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShareCooksnapViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _sending = MutableStateFlow(false)
    val sending: StateFlow<Boolean> = _sending.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success.asStateFlow()

    fun submitCooksnap(recipeId: String, imageUrl: String, description: String) {
        val userId = auth.currentUser?.uid
        if (userId.isNullOrBlank()) {
            _error.value = "User chưa đăng nhập"
            return
        }
        if (imageUrl.isBlank()) {
            _error.value = "Ảnh không hợp lệ"
            return
        }
        if (description.isBlank()) {
            _error.value = "Vui lòng nhập mô tả"
            return
        }

        _sending.value = true
        _error.value = null

        val newDoc = firestore.collection("cooksnaps").document()
        val cooksnap = Cooksnap(
            cooksnapId = newDoc.id,
            recipeId = recipeId,
            userId = userId,
            description = description,
            imageResult = imageUrl,
            hearts = 0,
            slaps = 0,
            smiles = 0
        )

        newDoc.set(cooksnap)
            .addOnSuccessListener {
                _sending.value = false
                _success.value = true
            }
            .addOnFailureListener { e ->
                _sending.value = false
                _error.value = "Lỗi gửi dữ liệu: ${e.message}"
            }
    }

    fun reset() {
        _sending.value = false
        _error.value = null
        _success.value = false
    }
}
