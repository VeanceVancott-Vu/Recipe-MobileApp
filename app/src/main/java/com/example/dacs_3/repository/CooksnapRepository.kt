import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.Cooksnap
import com.example.dacs_3.model.CooksnapWithUser
import com.example.dacs_3.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CooksnapRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private var cooksnapListener: ListenerRegistration? = null
    private val userListeners = mutableMapOf<String, ListenerRegistration>()

    /**
     * Lắng nghe realtime danh sách Cooksnap theo recipeId.
     * Trả về callback success với danh sách cooksnap.
     * Callback error trả về lỗi nếu có.
     */
    fun listenCooksnapsByRecipeId(
        recipeId: String,
        onSuccess: (List<Cooksnap>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        // Hủy listener cũ nếu có
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

    /**
     * Lắng nghe realtime thông tin User theo userId.
     * Trả về callback success với User hoặc null.
     * Callback error trả về lỗi nếu có.
     */
    fun listenUserById(
        userId: String,
        onSuccess: (User?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        // Nếu đã có listener cho userId thì bỏ đi, rồi tạo lại để cập nhật mới nhất
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

    /**
     * Hủy listener Cooksnap realtime.
     */
    fun removeCooksnapListener() {
        cooksnapListener?.remove()
        cooksnapListener = null
    }

    /**
     * Hủy tất cả listener User realtime.
     */
    fun removeAllUserListeners() {
        userListeners.values.forEach { it.remove() }
        userListeners.clear()
    }
}
