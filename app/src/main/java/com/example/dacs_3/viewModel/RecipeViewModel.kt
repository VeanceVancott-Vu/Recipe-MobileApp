package com.example.dacs_3.viewModel

import androidx.lifecycle.ViewModel
import com.example.dacs_3.model.Recipe
import com.example.dacs_3.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeViewModel: ViewModel() {
    private val repository: RecipeRepository = RecipeRepository()
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        observeRecipes()
    }

    fun uploadRecipe(recipe: Recipe) {
        _isUploading.value = true
        repository.addRecipe(recipe) { success ->
            _isUploading.value = false
            if (!success) {
                _errorMessage.value = "Failed to upload recipe"
            }
        }
    }

    private fun observeRecipes() {
        repository.observeRecipes { recipeList ->
            _recipes.value = recipeList
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}