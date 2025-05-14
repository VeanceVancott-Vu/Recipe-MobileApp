package com.example.dacs_3.model

data class SearchHistory(
    val id: String = "",          // Document ID
    val userId: String = "",      // User who searched
    val query: String = "",       // Search term
    val timestamp: Long = System.currentTimeMillis()  // When the search occurred
)