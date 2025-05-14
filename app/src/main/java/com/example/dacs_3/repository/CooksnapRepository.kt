import com.example.dacs_3.model.Cooksnap
import com.example.dacs_3.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class CooksnapRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private var cooksnapListener: ListenerRegistration? = null
    private val userListeners = mutableMapOf<String, ListenerRegistration>()

    // Real-time version (kept for updates)
    fun listenCooksnapsByRecipeId(
        recipeId: String,
        onSuccess: (List<Cooksnap>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        cooksnapListener?.remove()
        cooksnapListener = firestore.collection("cooksnaps")
            .whereEqualTo("recipeId", recipeId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val cooksnaps = snapshot.toObjects(Cooksnap::class.java)
                    onSuccess(cooksnaps)
                }
            }
    }

    // New: One-time fetch version (faster initial load)
    suspend fun fetchCooksnapsByRecipeId(recipeId: String): List<Cooksnap> {
        return firestore.collection("cooksnaps")
            .whereEqualTo("recipeId", recipeId)
            .get()
            .await()
            .toObjects(Cooksnap::class.java)
    }

    // User realtime listener (keep as-is)
    fun listenUserById(
        userId: String,
        onSuccess: (User?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        userListeners[userId]?.remove()
        val listener = firestore.collection("users")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(User::class.java)
                    onSuccess(user)
                } else {
                    onSuccess(null)
                }
            }
        userListeners[userId] = listener
    }

    fun removeCooksnapListener() {
        cooksnapListener?.remove()
        cooksnapListener = null
    }

    fun removeAllUserListeners() {
        userListeners.values.forEach { it.remove() }
        userListeners.clear()
    }

    // Batch fetch users by IDs (used in ViewModel optimization)
    suspend fun getUsersByIdsBatch(userIds: List<String>): List<User> {
        val chunks = userIds.distinct().chunked(10) // Firestore limit
        val allUsers = mutableListOf<User>()

        for (chunk in chunks) {
            val snapshot = firestore.collection("users")
                .whereIn("id", chunk)
                .get()
                .await()
            allUsers.addAll(snapshot.toObjects(User::class.java))
        }
        return allUsers
    }
}
