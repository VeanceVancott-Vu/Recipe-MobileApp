package com.example.dacs_3.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.User
import com.example.dacs_3.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _authResult = MutableStateFlow<Pair<Boolean, String?>>(false to null)
    val authResult: StateFlow<Pair<Boolean, String?>> = _authResult

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _recipeUser = MutableStateFlow<User?>(null)
    val recipeUser: StateFlow<User?> = _recipeUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _deleteAccountStatus = MutableStateFlow<Result<Boolean>>(Result.success(false))
    val deleteAccountStatus: StateFlow<Result<Boolean>> = _deleteAccountStatus

    val currentUserUid: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

    fun signUp(email: String, password: String, username: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.signUp(email, password, username)
            _authResult.value = result
            _isLoading.value = false
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.login(email, password)
            _authResult.value = result

            if (result.first) {
                val user = authRepository.fetchCurrentUserData()
                _currentUser.value = user
            }
            _isLoading.value = false
        }
    }

    fun fetchUserById(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            authRepository.getUserById(
                userId = userId,
                onSuccess = { user ->
                    _recipeUser.value = user
                    _isLoading.value = false
                },
                onFailure = { e ->
                    _errorMessage.value = e.message
                    _isLoading.value = false
                }
            )
        }
    }

    fun fetchAndSetCurrentUser() {
        viewModelScope.launch {
            val user = authRepository.fetchCurrentUserData()
            _currentUser.value = user
        }
    }

    fun logout() {
        authRepository.logout()
        _currentUser.value = null
    }

    fun isUserLoggedIn(): Boolean = authRepository.isUserLoggedIn()

    fun sendOtpToEmail(email: String, onResult: (Boolean, String?) -> Unit) {
        authRepository.sendOtpToEmail(email, onResult)
    }

    fun getCurrentUserId(): String? = authRepository.getCurrentUserId()

    fun updateUser(user: User, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.updateUser(user)
                    .addOnSuccessListener {
                        _currentUser.value = user
                        onResult(true)
                    }
                    .addOnFailureListener {
                        Log.e("AuthViewModel", "Update failed: ${it.message}")
                        onResult(false)
                    }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Update exception: ${e.message}")
                onResult(false)
            }
        }
    }

    fun deleteAccount(password: String) {
        viewModelScope.launch {
            try {
                authRepository.deleteUserAccount(password)
                _deleteAccountStatus.value = Result.success(true)
            } catch (e: Exception) {
                _deleteAccountStatus.value = Result.failure(e)
            }
        }
    }
}
