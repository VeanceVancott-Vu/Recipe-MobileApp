package com.example.dacs_3.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _authResult = MutableLiveData<Pair<Boolean, String?>>()
    val authResult: LiveData<Pair<Boolean, String?>> = _authResult

    fun signUp(email: String, password: String, username: String) {
        viewModelScope.launch {
            val result = authRepository.signUp(email, password, username)
            _authResult.postValue(result)
        }
    }

    fun login(email: String, password: String) {
        Log.d("AuthViewModel", "Attempting login with email: $email")

        viewModelScope.launch {
            val result = authRepository.login(email, password)
            Log.d("AuthViewModel", "Login result: Success=${result.first}, Message=${result.second}")
            _authResult.postValue(result)
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }

    // Q:
    // Phương thức gửi OTP
    fun sendOtpToEmail(email: String, onResult: (Boolean, String?) -> Unit) {
        authRepository.sendOtpToEmail(email, onResult)
    }

    // Q:
    // Lụm UID
    fun getCurrentUserId(): String? {
        return authRepository.getCurrentUserId()
    }


    // Q:
    // Thử nghiệm
    val currentUserUid: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

}
