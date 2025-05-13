package com.example.dacs_3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.Collections
import com.example.dacs_3.repository.CollectionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CollectionsViewModel : ViewModel() {


    private val repository = CollectionsRepository()

    private val _collections = MutableStateFlow<List<Collections>>(emptyList())
    val collections: StateFlow<List<Collections>> = _collections

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadCollections(userId: String) {
        viewModelScope.launch {
            val result = repository.getCollectionsByUser(userId)
            if (result.isSuccess) {
                _collections.value = result.getOrDefault(emptyList())
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun addCollection(collectionName: String, userId: String) {
        val newCollection = Collections(
            name = collectionName,
            userId = userId,
            recipeIds = emptyList()
        )

        viewModelScope.launch {
            val result = repository.addCollection(newCollection)
            if (result.isSuccess) {
                loadCollections(userId)
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun deleteCollection(collectionId: String, userId: String) {
        viewModelScope.launch {
            val result = repository.deleteCollection(collectionId)
            if (result.isSuccess) {
                loadCollections(userId)
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun updateRecipesInCollection(collectionId: String, recipeIds: List<String>, userId: String) {
        viewModelScope.launch {
            val result = repository.updateCollectionRecipes(collectionId, recipeIds)
            if (result.isSuccess) {
                loadCollections(userId)
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }

}