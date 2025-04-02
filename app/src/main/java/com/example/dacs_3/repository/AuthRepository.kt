package com.example.dacs_3.repository

import android.util.Log
import com.example.dacs_3.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun signUp(email: String, password: String, username: String): Pair<Boolean, String?> {
        // Q:
        // Kiểm tra đầu vào trước khi gọi Firebase
        if (email.isBlank() || password.isBlank() || username.isBlank()) {
            return Pair(false, "Email, mật khẩu và tên người dùng không được để trống")
        }
        if (password.length < 6) {
            return Pair(false, "Mật khẩu phải có ít nhất 6 ký tự")
        }

        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid

            if (userId != null) {
                val user = User(userId, email, username, profileImageUrl = "")

                // Store user details in Firestore
                firestore.collection("users").document(userId).set(user).await()

                Log.d("AuthRepository", "Sign up successful and user data stored for $email")
                Pair(true, "Sign up successful!")
            } else {
                Pair(false, "Sign up failed: No user ID")
            }




        } catch (e: Exception) {
            Log.e("AuthRepository", "Sign up failed: ${e.localizedMessage}", e)
            Pair(false, e.localizedMessage ?: "Sign up failed")
        }
    }

    suspend fun login(email: String, password: String): Pair<Boolean, String?> {
        if (email.isBlank() || password.isBlank()) {
            return Pair(false, "Email or password cannot be empty")
        }

        return try {
            Log.d("AuthRepository", "Attempting login for $email")
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Log.d("AuthRepository", "Login successful for $email")
            Pair(true, "Login successful!")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Login failed: ${e.localizedMessage}", e)
            Pair(false, e.localizedMessage ?: "Login failed")
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    // Q:
    // Reset mật khẩu qua link 
    fun sendOtpToEmail(email: String, onResult: (Boolean, String?) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Nếu gửi thành công
                    onResult(true, null)
                } else {
                    // Nếu có lỗi, trả về thông báo lỗi
                    onResult(false, task.exception?.message)
                }
            }
    }

    // Q:
    // Lụm UID
    fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

}
