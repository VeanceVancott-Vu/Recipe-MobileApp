package com.example.dacs_3.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OtherUserProfileViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun loadUser(userId: String) {
        viewModelScope.launch {
            try {
                val doc = firestore.collection("users").document(userId).get().await()
                _user.value = if (doc.exists()) doc.toObject(User::class.java) else null
            } catch (e: Exception) {
                Log.e("OtherUserProfileVM", "Failed to load user", e)
                _user.value = null
            }
        }
    }
}
