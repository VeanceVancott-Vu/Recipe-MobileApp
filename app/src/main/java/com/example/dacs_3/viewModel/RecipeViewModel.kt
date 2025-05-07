package com.example.dacs_3.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
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

class RecipeViewModel: ViewModel() {
    private val repository: RecipeRepository = RecipeRepository()
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _instructions = mutableStateListOf<Instruction>()
    val instructions: List<Instruction> get() = _instructions

    // Temporarily store local image URIs for each instruction
    private val instructionImageUris = mutableMapOf<Int, MutableList<Uri>>()

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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUrl = suspendCoroutine<String> { continuation ->
                    CloudinaryUploader.uploadImageFromUri(
                        context,
                        uri,
                        uploadPreset,
                        onSuccess = { url -> continuation.resume(url) },
                        onError = { exception -> continuation.resumeWithException(exception) }
                    )
                }

                withContext(Dispatchers.Main) {
                    onSuccess(imageUrl) // ✅ Return the image URL
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }


    fun uploadRecipe(recipe: Recipe) {
        _isUploading.value = true
        repository.addRecipe(recipe) { success ->
            Log.d("RecipeViewModel", "uploadRecipe: $success")
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

    fun addInstruction(step: Instruction) {
        _instructions.add(step)
    }

    fun hasInstructionImages(): Boolean {
        return instructionImageUris.values.any { it.isNotEmpty() }
    }

    fun addImageUriToInstruction(index: Int, uri: Uri) {
        Log.d("RecipeViewModel", "addImageUriToInstruction: $uri")
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
        // Update the mutable list and trigger reactivity
        _instructions[index] = _instructions[index].copy(description = description)
    }

    // Function to insert a new instruction at a specific position
    fun insertInstructionAt(position: Int) {
        val newInstruction = Instruction(stepNumber = position + 1)
        _instructions.add(position, newInstruction)
        // Ensure the step numbers remain in order
        reorderInstructions()
    }

    // Function to delete an instruction at a specific position
    fun deleteInstructionAt(position: Int) {
        if (_instructions.size > 1) {
            _instructions.removeAt(position)
            // Ensure the step numbers remain in order
            reorderInstructions()
        }
    }

    // Helper function to reorder instructions based on their step number
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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updatedInstructions = _instructions.mapIndexed { index, instruction ->
                    val uris = instructionImageUris[index] ?: emptyList()
                    val urls = mutableListOf<String>()

                    uris.forEach { uri ->
                        val url = suspendCoroutine<String> { continuation ->
                            CloudinaryUploader.uploadImageFromUri(
                                context, uri, uploadPreset,
                                onSuccess = { uploadedUrl -> continuation.resume(uploadedUrl) },
                                onError = { exception -> continuation.resumeWithException(exception) }
                            )
                        }
                        urls.add(url)
                    }

                    instruction.copy(imageUrl = urls)
                }

                withContext(Dispatchers.Main) {
                    _instructions.clear()
                    _instructions.addAll(updatedInstructions)
                    onComplete(updatedInstructions)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }




}