package com.example.dacs_3.testfollow

import com.example.dacs_3.model.User
import com.example.dacs_3.model.Follow
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

object MockUserFollowSeeder {
    fun seedMockData() {
        val db = FirebaseFirestore.getInstance()

        // Define 11 users
        val users = listOf(
            // Main user
            User(userId = "user1", username = "Alice", profileImageUrl = "https://example.com/alice.jpg"),
            // Friends (mutual follows with Alice)
            User(userId = "user2", username = "Bob", profileImageUrl = "https://example.com/bob.jpg"),
            User(userId = "user3", username = "Charlie", profileImageUrl = "https://example.com/charlie.jpg"),
            User(userId = "user4", username = "Diana", profileImageUrl = "https://example.com/diana.jpg"),
            User(userId = "user5", username = "Ethan", profileImageUrl = "https://example.com/ethan.jpg"),
            User(userId = "user6", username = "Fiona", profileImageUrl = "https://example.com/fiona.jpg"),
            // Users Alice follows (one-way)
            User(userId = "user7", username = "George", profileImageUrl = "https://example.com/george.jpg"),
            User(userId = "user8", username = "Hannah", profileImageUrl = "https://example.com/hannah.jpg"),
            User(userId = "user9", username = "Ian", profileImageUrl = "https://example.com/ian.jpg"),
            User(userId = "user10", username = "Julia", profileImageUrl = "https://example.com/julia.jpg"),
            User(userId = "user11", username = "Kevin", profileImageUrl = "https://example.com/kevin.jpg")
        )

        // Define a mutable list of follow relationships
        val follows = mutableListOf(
            // Mutual follows (friends: Alice ↔ Bob, Charlie, Diana, Ethan, Fiona)
            Follow(followerId = "user1", followedId = "user2"), // Alice follows Bob
            Follow(followerId = "user2", followedId = "user1"), // Bob follows Alice
            Follow(followerId = "user1", followedId = "user3"), // Alice follows Charlie
            Follow(followerId = "user3", followedId = "user1"), // Charlie follows Alice
            Follow(followerId = "user1", followedId = "user4"), // Alice follows Diana
            Follow(followerId = "user4", followedId = "user1"), // Diana follows Alice
            Follow(followerId = "user1", followedId = "user5"), // Alice follows Ethan
            Follow(followerId = "user5", followedId = "user1"), // Ethan follows Alice
            Follow(followerId = "user1", followedId = "user6"), // Alice follows Fiona
            Follow(followerId = "user6", followedId = "user1"), // Fiona follows Alice
            // One-way follows (Alice follows these users, but they don't follow back)
            Follow(followerId = "user1", followedId = "user7"), // Alice follows George
            Follow(followerId = "user1", followedId = "user8"), // Alice follows Hannah
            Follow(followerId = "user1", followedId = "user9"), // Alice follows Ian
            Follow(followerId = "user1", followedId = "user10"), // Alice follows Julia
            Follow(followerId = "user1", followedId = "user11") // Alice follows Kevin
        )

        // Ensure that User 2 follows at least 3 of User 1's friends (mutual follows)
        val user2Follows = listOf("user3", "user4", "user5", "user6", "user2") // Bob follows Charlie, Diana, Ethan, Fiona, and himself

        // Add these follow relationships to the follows list
        user2Follows.forEach { followedUserId ->
            follows.add(Follow(followerId = "user2", followedId = followedUserId)) // Bob follows these users
        }

        // Seed users to Firestore
        users.forEach { user ->
            db.collection("Users").document(user.userId).set(user)
                .addOnSuccessListener {
                    Log.d("Seeder", "✅ User ${user.userId} (${user.username}) created.")
                }
                .addOnFailureListener { e ->
                    Log.e("Seeder", "❌ Error creating user ${user.userId}: ${e.message}")
                    e.printStackTrace()
                }
        }

        // Seed follow relationships to Firestore
        follows.forEach { follow ->
            val docId = "${follow.followerId}_${follow.followedId}"
            db.collection("Follows").document(docId).set(follow)
                .addOnSuccessListener {
                    Log.d("Seeder", "✅ Follow $docId created.")
                }
                .addOnFailureListener { e ->
                    Log.e("Seeder", "❌ Error creating follow $docId: ${e.message}")
                    e.printStackTrace()
                }
        }
    }
}
