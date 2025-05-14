package com.example.dacs_3.viewmodel

import CooksnapRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.Cooksnap
import com.example.dacs_3.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CooksnapViewModel : ViewModel() {

    private val repository = CooksnapRepository()

    private val _cooksnaps = MutableStateFlow<List<Cooksnap>>(emptyList())
    val cooksnaps: StateFlow<List<Cooksnap>> = _cooksnaps.asStateFlow()

    private val _usersMap = MutableStateFlow<Map<String, User>>(emptyMap())
    val usersMap: StateFlow<Map<String, User>> = _usersMap.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val observedUserIds = mutableSetOf<String>()

    fun loadCooksnaps(recipeId: String) {
        viewModelScope.launch {
            try {
                repository.listenCooksnapsByRecipeId(recipeId,
                    onSuccess = { cooksnapsList ->
                        _cooksnaps.value = cooksnapsList

                        // Fetch users in batch
                        val userIdsToFetch = cooksnapsList.map { it.userId }
                            .filter { it.isNotBlank() && !observedUserIds.contains(it) }
                            .distinct()

                        userIdsToFetch.forEach { observedUserIds.add(it) }

                        if (userIdsToFetch.isNotEmpty()) {
                            viewModelScope.launch {
                                try {
                                    val users = repository.getUsersByIdsBatch(userIdsToFetch)
                                    val updatedMap = _usersMap.value.toMutableMap()
                                    users.forEach { updatedMap[it.userId] = it }
                                    _usersMap.value = updatedMap
                                } catch (e: Exception) {
                                    _errorMessage.value = "Failed to load users: ${e.message}"
                                }
                            }
                        }
                    },
                    onError = { e ->
                        _errorMessage.value = "Failed to load cooksnaps: ${e.message}"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error: ${e.message}"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.removeCooksnapListener()
        repository.removeAllUserListeners()
    }
}
