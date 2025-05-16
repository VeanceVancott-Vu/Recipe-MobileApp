package com.example.dacs_3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserRecipesViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    // StateFlow lưu danh sách món ăn của user
    private val _userRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val userRecipes: StateFlow<List<Recipe>> = _userRecipes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Hàm load món ăn của user 1 lần
    fun loadRecipesByUser(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val querySnapshot = firestore.collection("recipes")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                val list = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Recipe::class.java)
                }

                _userRecipes.value = list
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Lỗi tải món ăn"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
