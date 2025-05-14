package com.example.dacs_3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.SearchHistory
import com.example.dacs_3.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchHistoryViewModel : ViewModel() {

    private val repository: SearchHistoryRepository = SearchHistoryRepository()

    private val _history = MutableStateFlow<List<SearchHistory>>(emptyList())
    val history: StateFlow<List<SearchHistory>> = _history

    fun loadHistory(userId: String) {
        viewModelScope.launch {
            _history.value = repository.getSearchHistory(userId)
        }
    }

    fun addHistory(userId: String, query: String) {
        viewModelScope.launch {
            repository.addSearchHistory(userId, query)
            loadHistory(userId) // Refresh after adding
        }
    }

    fun deleteItem(documentId: String, userId: String) {
        viewModelScope.launch {
            repository.deleteSearchHistoryItem(documentId)
            loadHistory(userId) // Refresh after deletion
        }
    }

    fun clearHistory(userId: String) {
        viewModelScope.launch {
            repository.clearAllHistory(userId)
            loadHistory(userId)
        }
    }
}
