package com.example.dacs_3.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.cloudinary.imageupload.CloudinaryUploader
import com.example.dacs_3.model.Instruction
import com.example.dacs_3.model.Recipe
import com.example.dacs_3.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RecipeViewModel : ViewModel() {
    private val repository: RecipeRepository = RecipeRepository()
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _instructions = mutableStateListOf<Instruction>()
    val instructions: List<Instruction> get() = _instructions

    private val instructionImageUris = mutableMapOf<Int, MutableList<Uri>>()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe: StateFlow<Recipe?> = _selectedRecipe

    init {
        observeRecipes()
    }

    fun uploadMainImage(
        context: Context,
        uri: Uri,
        uploadPreset: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val imageUrl = withContext(Dispatchers.IO) {
                    suspendCoroutine<String> { continuation ->
                        CloudinaryUploader.uploadImageFromUri(
                            context,
                            uri,
                            uploadPreset,
                            onSuccess = { url -> continuation.resume(url) },
                            onError = { e -> continuation.resumeWithException(e) }
                        )
                    }
                }
                onSuccess(imageUrl)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun fetchRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.getRecipes(
                onSuccess = { list ->
                    Log.d("RecipeViewModel", "Fetch recipes: $list")
                    _recipes.value = list
                    _isLoading.value = false
                },
                onFailure = { e ->
                    _errorMessage.value = e.message
                    _isLoading.value = false
                }
            )
        }
    }

    fun uploadRecipe(recipe: Recipe) {
        viewModelScope.launch {
            _isUploading.value = true
            repository.addRecipe(recipe) { success ->
                Log.d("RecipeViewModel", "uploadRecipe: $success")
                _isUploading.value = false
                if (!success) _errorMessage.value = "Failed to upload recipe"
            }
        }
    }

    private fun observeRecipes() {
        viewModelScope.launch {
            repository.observeRecipes { recipeList ->
                _recipes.value = recipeList
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun addInstruction(step: Instruction) {
        _instructions.add(step)
    }

    fun hasInstructionImages(): Boolean {
        return instructionImageUris.values.any { it.isNotEmpty() }
    }

    fun addImageUriToInstruction(index: Int, uri: Uri) {
        val list = instructionImageUris.getOrPut(index) { mutableListOf() }
        list.add(uri)

        if (index in _instructions.indices) {
            val updatedImageUrls = _instructions[index].imageUrl.toMutableList()
            updatedImageUrls.add(uri.toString())
            _instructions[index] = _instructions[index].copy(imageUrl = updatedImageUrls)
        }
    }

    fun deleteImageFromInstruction(index: Int, imageIndex: Int) {
        instructionImageUris[index]?.removeAt(imageIndex)
    }

    fun getImageUrisForInstruction(index: Int): List<Uri> {
        return instructionImageUris[index] ?: emptyList()
    }

    fun updateInstructionDescription(index: Int, description: String) {
        _instructions[index] = _instructions[index].copy(description = description)
    }

    fun insertInstructionAt(position: Int) {
        val newInstruction = Instruction(stepNumber = position + 1)
        _instructions.add(position, newInstruction)
        reorderInstructions()
    }

    fun deleteInstructionAt(position: Int) {
        if (_instructions.size > 1) {
            _instructions.removeAt(position)
            reorderInstructions()
        }
    }

    private fun reorderInstructions() {
        _instructions.forEachIndexed { index, instruction ->
            _instructions[index] = instruction.copy(stepNumber = index + 1)
        }
    }

    fun uploadAllInstructionImages(
        context: Context,
        uploadPreset: String,
        onComplete: (List<Instruction>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val updatedInstructions = withContext(Dispatchers.IO) {
                    _instructions.mapIndexed { index, instruction ->
                        val uris = instructionImageUris[index] ?: emptyList()
                        val urls = mutableListOf<String>()
                        uris.forEach { uri ->
                            val url = suspendCoroutine<String> { continuation ->
                                CloudinaryUploader.uploadImageFromUri(
                                    context, uri, uploadPreset,
                                    onSuccess = { uploadedUrl -> continuation.resume(uploadedUrl) },
                                    onError = { e -> continuation.resumeWithException(e) }
                                )
                            }
                            urls.add(url)
                        }
                        instruction.copy(imageUrl = urls)
                    }
                }

                _instructions.clear()
                _instructions.addAll(updatedInstructions)
                onComplete(updatedInstructions)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun fetchRecipeById(recipeId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.getRecipeById(
                recipeId,
                onSuccess = { recipe ->
                    _selectedRecipe.value = recipe
                    _isLoading.value = false
                },
                onFailure = { e ->
                    _errorMessage.value = e.message
                    _isLoading.value = false
                }
            )
        }
    }
}
