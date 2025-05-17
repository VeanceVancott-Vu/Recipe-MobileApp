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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()


    private val _loginAttempted = MutableStateFlow(false)
    val loginAttempted = _loginAttempted.asStateFlow()

    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUsers: StateFlow<List<User>> = _allUsers

    init {
        checkIfUserIsLoggedIn()
    }

    fun checkIfUserIsLoggedIn() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // Nếu người dùng đã đăng nhập, lấy thông tin người dùng từ Firestore
            fetchCurrentUserData()
        } else {
            _currentUser.value = null // Nếu không có người dùng đăng nhập, gán null
        }
    }

    fun fetchCurrentUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            val userRef = FirebaseFirestore.getInstance().collection("users").document(it.uid)
            userRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    _currentUser.value = user
                }
            }.addOnFailureListener {
                Log.e("AuthViewModel", "Failed to fetch user data")
            }
        }
    }


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
                _loginAttempted.value = true

            }
            _isLoading.value = false
            _loginAttempted.value = true

        }
    }

    fun loadAllUsers() {
        viewModelScope.launch {
            authRepository.getAllUsers(
                onSuccess = { users ->
                    _allUsers.value = users
                },
                onError = { error ->
                    _errorMessage.value = "Failed to load users: ${error.message}"
                }
            )
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

    // Cái này của Q - Q dùng test
    fun fetchAndSetCurrentUserTest() {

        val testUid = "Ey6lmMLz77gJxFNLll4OFsrSsRl1"

        viewModelScope.launch {
            // Gọi phương thức fetchUserDataByUid từ AuthRepository
            val user = authRepository.fetchUserDataByUid(testUid)

            // Log dữ liệu người dùng
            Log.d("AuthViewModel", "Fetched user: $user")  // In ra dữ liệu của user

            // Cập nhật _currentUser với dữ liệu người dùng
            _currentUser.value = user
        }
    }


  //   Cái này của Dũ
    fun fetchAndSetCurrentUser() {
        viewModelScope.launch {
            val user = authRepository.fetchCurrentUserData()
            _currentUser.value = user
        }
    }


    fun logout() {
        authRepository.logout()
        _currentUser.value = null
        _userRole.value= null
    }

    fun isUserLoggedIn(): Boolean = authRepository.isUserLoggedIn()

    // Q:
    // Phương thức gửi OTP
    fun sendOtpToEmail(email: String, onResult: (Boolean, String?) -> Unit) {
        authRepository.sendOtpToEmail(email, onResult)
    }

    fun getCurrentUserId(): String? = authRepository.getCurrentUserId()

    fun loadUserRole() {
        viewModelScope.launch {
            val role = authRepository.getCurrentUserRole()
            _userRole.value = role
        }
    }

    suspend fun getCurrentUserRole(): String? {
        return authRepository.getCurrentUserRole()
    }

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



    fun deleteAccount(userId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val success = authRepository.deleteUserAccountByAdmin(userId)
                onResult(success)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete account: ${e.message}"
                onResult(false)
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
