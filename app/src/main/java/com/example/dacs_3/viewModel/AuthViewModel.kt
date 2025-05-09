package com.example.dacs_3.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.User
import com.example.dacs_3.repository.AuthRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _authResult = MutableLiveData<Pair<Boolean, String?>>()
    val authResult: LiveData<Pair<Boolean, String?>> = _authResult

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser


    private val _deleteAccountStatus = MutableStateFlow<Result<Boolean>>(Result.success(false))
    val deleteAccountStatus: StateFlow<Result<Boolean>> = _deleteAccountStatus



    fun signUp(email: String, password: String, username: String) {
        viewModelScope.launch {
            val result = authRepository.signUp(email, password, username)
            _authResult.postValue(result)
        }
    }

    fun login(email: String, password: String) {
        Log.d("AuthViewModel", "Attempting login with email: $email")

        viewModelScope.launch {
            val app = FirebaseApp.getInstance()
            Log.d("FirebaseDebug", "Firebase project: ${app.options.projectId}")

            val result = authRepository.login(email, password)
            Log.d("AuthViewModel", "Login result: Success=${result.first}, Message=${result.second}")
            _authResult.postValue(result)

            if (result.first) {
                // Fetch user data if login is successful
                authRepository.fetchCurrentUserData()
            }
        }
    }

    fun fetchAndSetCurrentUser() {

//        val testUid = "0iY44FTfxpflQXImL1XPSOT2Knu1" // UID có sẵn
        val testUid = "Ey6lmMLz77gJxFNLll4OFsrSsRl1"

        viewModelScope.launch {
            // Gọi phương thức fetchUserDataByUid từ AuthRepository
            val user = authRepository.fetchUserDataByUid(testUid)

            // Log dữ liệu người dùng
            Log.d("AuthViewModel", "Fetched user: $user")  // In ra dữ liệu của user

            // Cập nhật _currentUser với dữ liệu người dùng
            _currentUser.postValue(user)
        }
//        viewModelScope.launch {
//            val user = authRepository.fetchCurrentUserData()
//            _currentUser.postValue(user)
//        }
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



    fun updateUser(user: User, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            authRepository.updateUser(user)
                .addOnSuccessListener {
                    Log.d("AuthViewModel", "User updated successfully.")
                    onResult(true)
                    _currentUser.postValue(user)
                }
                .addOnFailureListener { e ->
                    Log.e("AuthViewModel", "Failed to update user: ${e.message}")
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

    // Q:


    fun updateProfileImageUrl(userId: String, imageUrl: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Giả sử bạn lưu dữ liệu người dùng trong Firestore
                val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

                userRef.update("profileImageUrl", imageUrl)
                    .addOnSuccessListener {
                        Log.d("AuthViewModel", "Profile image URL updated successfully.")
                        // Cập nhật currentUser
                        _currentUser.value = _currentUser.value?.copy(profileImageUrl = imageUrl)
                        onResult(true)
                    }

                    .addOnFailureListener { e ->
                        Log.e("AuthViewModel", "Failed to update profile image URL", e)
                        onResult(false)
                    }
                
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error updating profile image URL: ${e.message}")
                onResult(false)
            }
        }
    }
}
