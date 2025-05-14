package com.example.dacs_3.viewmodel

import CooksnapRepository
import androidx.lifecycle.ViewModel
import com.example.dacs_3.model.Cooksnap
import com.example.dacs_3.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CooksnapViewModel : ViewModel() {

    private val repository = CooksnapRepository()

    private val _cooksnaps = MutableStateFlow<List<Cooksnap>>(emptyList())
    val cooksnaps: StateFlow<List<Cooksnap>> = _cooksnaps.asStateFlow()

    private val _usersMap = MutableStateFlow<Map<String, User>>(emptyMap())
    val usersMap: StateFlow<Map<String, User>> = _usersMap.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Giữ tập userId đã đăng ký listener để tránh tạo lại
    private val observedUserIds = mutableSetOf<String>()

    /**
     * Bắt đầu lắng nghe realtime danh sách Cooksnap cho recipeId.
     * Đồng thời với mỗi userId mới trong cooksnap sẽ mở realtime listener user.
     */
    fun loadCooksnaps(recipeId: String) {
        repository.listenCooksnapsByRecipeId(recipeId,
            onSuccess = { cooksnapsList ->
                _cooksnaps.value = cooksnapsList

                // Với mỗi cooksnap, nếu userId chưa có listener thì đăng ký realtime listener user
                cooksnapsList.forEach { cooksnap ->
                    val userId = cooksnap.userId
                    if (userId.isNotBlank() && !observedUserIds.contains(userId)) {
                        observedUserIds.add(userId)
                        listenUserRealtime(userId)
                    }
                }
            },
            onError = { e ->
                _errorMessage.value = "Lỗi tải cooksnap: ${e.message}"
            }
        )
    }

    /**
     * Mở realtime listener cho userId, cập nhật map usersMap khi dữ liệu user thay đổi.
     */
    private fun listenUserRealtime(userId: String) {
        repository.listenUserById(userId,
            onSuccess = { user ->
                if (user != null) {
                    // Cập nhật usersMap mới
                    val newMap = _usersMap.value.toMutableMap()
                    newMap[userId] = user
                    _usersMap.value = newMap.toMap()
                }
            },
            onError = { e ->
                _errorMessage.value = "Lỗi tải user: ${e.message}"
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        repository.removeCooksnapListener()
        repository.removeAllUserListeners()
    }
}
