package com.example.dacs_3.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.cloudinary.imageupload.CloudinaryUploader
import com.example.dacs_3.model.Instruction
import com.example.dacs_3.model.RatingEntry
import com.example.dacs_3.model.Recipe
import com.example.dacs_3.repository.RecipeRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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


    private val _recipesByCollection = MutableStateFlow<List<Recipe>>(emptyList())
    val recipesByCollection: StateFlow<List<Recipe>> = _recipesByCollection

    private val _searchResults = MutableStateFlow<List<Recipe>>(emptyList())
    val searchResults: StateFlow<List<Recipe>> = _searchResults

    private val _searchError = MutableStateFlow<String?>(null)
    val searchError: StateFlow<String?> = _searchError

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

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
                Log.d("UploadImage", "Start uploading image...")  // Log khi bắt đầu upload

                val imageUrl = withContext(Dispatchers.IO) {
                    suspendCoroutine<String> { continuation ->
                        CloudinaryUploader.uploadImageFromUri(
                            context,
                            uri,
                            uploadPreset,
                            onSuccess = { url ->
                                Log.d("UploadImage", "Upload successful, image URL: $url")  // Log khi thành công
                                continuation.resume(url)
                            },
                            onError = { e ->
                                Log.e("UploadImage", "Error occurred during upload: ${e.message}", e)  // Log lỗi upload
                                continuation.resumeWithException(e)
                            }
                        )
                    }
                }

                onSuccess(imageUrl)

            } catch (e: Exception) {
                // Log exception nếu có lỗi không bắt được trong CloudinaryUploader
                Log.e("UploadImage", "Exception caught during upload: ${e.message}", e)
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
        Log.e("AddRecipeScreen", "Adding image URI to instruction $index: $uri")

        // 1. Lấy danh sách ảnh của instruction tại vị trí 'index' hoặc khởi tạo danh sách mới nếu chưa có.
        val list = instructionImageUris.getOrPut(index) { mutableListOf() }
        Log.e("AddRecipeScreen", "Image list for instruction $index: $list")

        // 2. Thêm URI của ảnh vào danh sách.
        list.add(uri)
        Log.d("AddRecipeScreen", "Updated image list for instruction $index: $list")

        // 3. Nếu chỉ số 'index' hợp lệ trong dãy _instructions, cập nhật ảnh cho instruction.
        if (index in _instructions.indices) {
            // 4. Lấy danh sách ảnh hiện tại của instruction tại vị trí 'index'.
            val updatedImageUrls = _instructions[index].imageUrl.toMutableList()
            Log.e("AddRecipeScreen", "Current image URLs for instruction $index: $updatedImageUrls")

            // 5. Thêm URI mới vào danh sách ảnh.
            updatedImageUrls.add(uri.toString())
            Log.e("AddRecipeScreen", "Updated image URLs after adding new image: $updatedImageUrls")

            // 6. Cập nhật lại instruction tại vị trí 'index' với danh sách ảnh mới.
            _instructions[index] = _instructions[index].copy(imageUrl = updatedImageUrls)
            Log.e("AddRecipeScreen", "Updated instruction at index $index with new image URLs.")
        } else {
            Log.e("AddRecipeScreen", "Index $index is out of bounds in _instructions")
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

    fun loadRecipesByCollection(recipeIds: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            val allRecipes = _recipes.value // Get the current list of all recipes
            _isLoading.value = false

            // Now we filter recipes that match the recipeIds in the collection
            val filteredRecipes = allRecipes.filter { recipe ->
                recipeIds.contains(recipe.recipeId)
            }
            _recipesByCollection.value = filteredRecipes
        }
    }


    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.updateRecipe(recipe) { success ->
                Log.d("RecipeViewModel", "updateRecipe: $success")
                if (!success) _errorMessage.value = "Failed to update recipe"
            }
        }
    }

    fun addRating(recipeId: String, userId: String, rating: Float, onSuccess: () -> Unit) {
        val recipeRef = FirebaseFirestore.getInstance().collection("recipes").document(recipeId)

        val updatedRatings = _selectedRecipe.value?.ratings?.toMutableList() ?: mutableListOf()

        if (updatedRatings.any { it.userId == userId }) {
            updateRating(recipeId, userId, rating, onSuccess)
            return
        }

        updatedRatings.add(RatingEntry(userId, rating))
        val newAverage = updatedRatings.map { it.stars }.average().toFloat()

        recipeRef.update(
            "ratings", updatedRatings,
            "averageRating", newAverage
        ).addOnSuccessListener {
            _selectedRecipe.value = _selectedRecipe.value?.copy(
                ratings = updatedRatings // Only update ratings
            )
            onSuccess()
        }.addOnFailureListener {
            Log.e("RecipeViewModel", "Failed to add rating: $it")
        }
    }

    fun updateRating(recipeId: String, userId: String, newRating: Float, onSuccess: () -> Unit) {
        val recipeRef = FirebaseFirestore.getInstance().collection("recipes").document(recipeId)

        val updatedRatings = _selectedRecipe.value?.ratings?.toMutableList() ?: mutableListOf()

        val index = updatedRatings.indexOfFirst { it.userId == userId }
        if (index != -1) {
            updatedRatings[index] = updatedRatings[index].copy(stars = newRating)
        } else {
            updatedRatings.add(RatingEntry(userId, newRating))
        }

        val newAverage = updatedRatings.map { it.stars }.average().toFloat()

        recipeRef.update(
            "ratings", updatedRatings,
            "averageRating", newAverage
        ).addOnSuccessListener {
            _selectedRecipe.value = _selectedRecipe.value?.copy(
                ratings = updatedRatings // Only update ratings
            )
            onSuccess()
        }.addOnFailureListener {
            Log.e("RecipeViewModel", "Failed to update rating: $it")
        }
    }

    fun searchRecipesByName(name: String) {
        _isSearching.value = true
        repository.getRecipesByName(
            query = name,
            onSuccess = {
                _searchResults.value = it
                _isSearching.value = false
            },
            onFailure = { e ->
                _searchError.value = e.message
                _isSearching.value = false
            }
        )
    }



}
