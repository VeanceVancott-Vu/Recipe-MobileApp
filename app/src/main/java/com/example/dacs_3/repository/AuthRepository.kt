package com.example.dacs_3.repository

import android.util.Log
import com.example.dacs_3.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun signUp(email: String, password: String, username: String): Pair<Boolean, String?> {
        // Q:
        // Kiểm tra đầu vào trước khi gọi Firebase

        Log.d("AuthRepository", "Sign up for user and data: $email, $username,$password")


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
    suspend fun getCurrentUserRole(): String? {
        val uid = firebaseAuth.currentUser?.uid ?: return null

        return try {
            val snapshot = firestore.collection("users")
                .document(uid)
                .get()
                .await()
            snapshot.getString("role")
        } catch (e: Exception) {
            null // or log error
        }
    }



    suspend fun fetchCurrentUserData(): User? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            return try {
                val document = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(currentUser.uid)
                    .get()
                    .await()

                if (document.exists()) {
                    val userData = document.toObject(User::class.java)
                    Log.d("LoginViewModel", "Fetched User: $userData")
                    userData
                } else {
                    Log.d("LoginViewModel", "No user document found for UID: ${currentUser.uid}")
                    null
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Failed to fetch user data: ${e.message}")
                null
            }
        } else {
            Log.e("LoginViewModel", "No current user after login")
            return null
        }
    }

    fun updateUser(user: User): Task<Void> {
        Log.d("updateUser","User: $user")
        return firestore
            .collection("users")
            .document(user.userId)
            .set(user)
    }


    suspend fun deleteUserAccount(password: String): Result<String> {
        val user = firebaseAuth.currentUser

        if (user == null) {
            return Result.failure(Exception("No user is currently signed in."))
        }

        val email = user.email
        if (email.isNullOrEmpty()) {
            return Result.failure(Exception("No email associated with user."))
        }

        return try {
            val credential = EmailAuthProvider.getCredential(email, password)
            user.reauthenticate(credential).await()

            // Delete the user document from Firestore
            firestore.collection("users").document(user.uid)
                .delete()
                .await()

            // Finally, delete the Firebase Auth user
            user.delete().await()

            Result.success("User account deleted successfully.")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Delete account failed: ${e.message}")
            Result.failure(e)
        }
    }


    suspend fun deleteUserAccountByAdmin(userId: String): Boolean {
        return try {
            firestore.collection("users").document(userId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Fake để test - Q
    // Trong AuthRepository
    suspend fun fetchUserDataByUid(uid: String): User? {
        val db = FirebaseFirestore.getInstance()

        return try {
            val document = db.collection("users").document(uid).get().await()
            document.toObject(User::class.java) // Chuyển đổi document thành đối tượng User
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error fetching user data: $e")
            null
        }
    }


    fun getUserById(
        userId: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        Log.d("AuthRepository", "Fetching user by ID: $userId")
        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                if (user != null) {
                    onSuccess(user)
                } else {
                    onFailure(Exception("User not found"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getAllUsers(
        onSuccess: (List<User>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("users")
            .get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.toObjects(User::class.java)
                onSuccess(users)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

}
