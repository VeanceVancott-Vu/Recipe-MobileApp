package com.example.dacs_3.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun signUp(email: String, password: String): Pair<Boolean, String?> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Log.d("AuthRepository", "Sign up successful for $email")
            Pair(true, "Sign up successful!")
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
}
