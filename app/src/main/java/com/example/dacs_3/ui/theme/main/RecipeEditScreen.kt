package com.example.dacs_3.ui.theme.main

import AddRecipeScreen
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.dacs_3.R
import com.example.dacs_3.cloudinary.imageupload.CloudinaryUploader
import com.example.dacs_3.model.Instruction
import com.example.dacs_3.model.Recipe
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.utils.bottomShadow
import com.example.dacs_3.viewmodel.AuthViewModel
import com.example.dacs_3.viewmodel.RecipeViewModel
import okhttp3.MultipartBody
import okhttp3.OkHttpClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeEditScreen(
    navController: NavController,
    id: String,
    recipeViewModel: RecipeViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val selectedRecipe by recipeViewModel.selectedRecipe.collectAsState()


    LaunchedEffect(id) {
        recipeViewModel.fetchRecipeById(id)
    }

    Log.e("RecipeEditScreen", "ViewModel: $recipeViewModel")

    val originalImageUri = selectedRecipe?.resultImages?.let { Uri.parse(it) }
    val initialImageIsEmpty = originalImageUri == null || originalImageUri.toString().isBlank()
    var selectedImageUri by remember { mutableStateOf(originalImageUri) }
    val isImageChanged by remember(selectedImageUri, originalImageUri) { derivedStateOf { selectedImageUri != originalImageUri } }
    val context = LocalContext.current
    val title = remember(selectedRecipe) { mutableStateOf(selectedRecipe?.title ?: "") }
    val story = remember(selectedRecipe) { mutableStateOf(selectedRecipe?.story ?: "") }
    val servingSize = remember(selectedRecipe) { mutableStateOf(selectedRecipe?.servingSize ?: "") }
    val cookingTime = remember(selectedRecipe) { mutableStateOf(selectedRecipe?.cookingTime ?: "") }
    val image = remember(selectedRecipe) { mutableStateOf(selectedRecipe?.resultImages ?: "") }
    val ingredients = remember(selectedRecipe) { mutableStateListOf(*selectedRecipe?.ingredients?.toTypedArray() ?: arrayOf()) }
    val instructions = remember { mutableStateListOf(*selectedRecipe?.instructions?.toTypedArray() ?: arrayOf()) }
    val userId = remember(selectedRecipe) { mutableStateOf(selectedRecipe?.userId ?: "") }


    Log.e("RecipeInstructions", "Instructions: $instructions")

    val launcher = rememberLauncherForActivityResult( contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let {
        selectedImageUri = it
        image.value = it.toString()
        }
    }


    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = {
                val mainImageUri = selectedImageUri
                val postRecipe: (String, List<Instruction>) -> Unit = { imageUrl, instructions ->
                    val recipe = Recipe(
                        recipeId = selectedRecipe?.recipeId ?: "", // 👈 LẤY LẠI ID
                        title = title.value,
                        story = story.value,
                        servingSize = servingSize.value,
                        cookingTime = cookingTime.value,
                        resultImages = imageUrl,
                        ingredients = ingredients.toList(),
                        instructions = instructions,
                        userId = userId.value
                    )

                    recipeViewModel.updateRecipe(recipe)


                    Toast.makeText(context, "Recipe posted successfully!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }


                val uploadInstructionsIfNeeded: (String) -> Unit = { mainImageUrl ->

                    val hasInstructionImages = recipeViewModel.hasInstructionImages()

                    if (hasInstructionImages) {

                        // Upload tất cả ảnh hướng dẫn
//                        recipeViewModel.uploadAllInstructionImages(
//                            context = context,
//                            uploadPreset = "koylin_unsigned",
//                            onComplete = { updatedInstructions ->
//                                postRecipe(mainImageUrl, updatedInstructions)
//                            },
//                            onError = { e ->
//                                Toast.makeText(context, "Instruction image upload failed: ${e.message}", Toast.LENGTH_LONG).show()
//                                postRecipe(mainImageUrl, recipeViewModel.instructions) // fallback to current instructions
//                            }
//                        )


                    } else {
                        // No instruction images, just use current instructions
                        postRecipe(mainImageUrl, instructions)

                        Log.e("RecipeInstructions", "Instructions: $instructions")

                    }
                }

                if (initialImageIsEmpty) {
                    // Trường hợp 1 hoặc 2
                    if (mainImageUri != null) {
                        // Trường hợp 1: ban đầu rỗng, giờ có ảnh → upload
                        recipeViewModel.uploadMainImage(
                            context = context,
                            uri = mainImageUri!!,
                            uploadPreset = "koylin_unsigned",
                            // Load ảnh hướng dẫn nếu có
                            onSuccess = { url -> uploadInstructionsIfNeeded(url) },
                            onError = { e ->
                                Toast.makeText(context, "Main image upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                                uploadInstructionsIfNeeded("")
                            }
                        )
                    } else {
                        // Trường hợp 2: vẫn không có ảnh → không làm gì
                        uploadInstructionsIfNeeded("")
                    }
                } else {
                    // Trường hợp 3 hoặc 4
                    if (isImageChanged && mainImageUri != null) {
                        // Trường hợp 4: có ảnh ban đầu, ảnh đã thay → upload
                        recipeViewModel.uploadMainImage(
                            context = context,
                            uri = mainImageUri!!,
                            uploadPreset = "koylin_unsigned",
                            onSuccess = { url -> uploadInstructionsIfNeeded(url) },
                            onError = { e ->
                                Toast.makeText(context, "Main image upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                                uploadInstructionsIfNeeded("")
                            }
                        )
                    } else {
                        // Trường hợp 3: ảnh cũ không đổi → không làm gì
                        uploadInstructionsIfNeeded(originalImageUri.toString())
                    }
                }

            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F764E))) {
                Text(text = "Save")
            }
        }

        Row() {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_m))).background(Color.LightGray)
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Chưa có ảnh", color = Color.DarkGray)
                    }
                }


                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(dimensionResource(R.dimen.spacing_s))
                ) {
                    DeleteButton()

                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_s)))

                    AddPhotoButton({
                        launcher.launch("image/*")
                    })
                }
            }
        }

        Column (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
        ) {
            OutlinedTextField(
                value = title.value,
                onValueChange = {title.value = it},
                placeholder = { Text("Example: The Best Sweet and Sour Fish Soup")},
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_m)))
                    .background(colorResource(R.color.text_field_background))
                    .bottomShadow(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = OliverGreen,
                    unfocusedTextColor = OliverGreen,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                )
            )

            OutlinedTextField(
                value = story.value,
                onValueChange = {story.value = it},
                placeholder = { Text("Share the story behind this dish ...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.spacing_s) * 15)
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_m)))
                    .background(colorResource(R.color.text_field_background))
                    .bottomShadow(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = OliverGreen,
                    unfocusedTextColor = OliverGreen,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                )
            )
        }

        // Q:
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.spacing_s))
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
            ) {
                Text("Serving size")

                OutlinedTextField(
                    value = servingSize.value,
                    onValueChange = {servingSize.value=it},
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(dimensionResource(R.dimen.spacing_xll))
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_m)))
                        .background(colorResource(R.color.text_field_background))
                        .bottomShadow(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = OliverGreen,
                        unfocusedTextColor = OliverGreen,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                    )
                )
            }


            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
            ) {
                Text("Cooking time")

                OutlinedTextField(
                    value = cookingTime.value,
                    onValueChange = {cookingTime.value = it },
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(dimensionResource(R.dimen.spacing_xll))
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_m)))
                        .background(colorResource(R.color.text_field_background))
                        .bottomShadow(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = OliverGreen,
                        unfocusedTextColor = OliverGreen,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                    )
                )
            }
        }

        //Q:
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.spacing_s)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
        ) {
            Text("Ingredients")
            IngredientInputList(ingredients )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.spacing_s)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
        ) {
            Text("Instructions")


            if (instructions.isNotEmpty()) {
                // Gọi InstructionInputList và truyền vào callback
                InstructionInputList(
                    instructions = instructions,
                    onInstructionChanged = { index, updatedInstruction ->
                        instructions[index] = updatedInstruction  // Cập nhật lại danh sách instructions
                    }
                )

                Log.e("RecipeEditScreen", "ViewModel: $recipeViewModel")


            } else {
                // Optional: Hiển thị thông báo hoặc placeholder
                Text("Chưa có hướng dẫn")
            }


        }
    }
}

@Composable
fun DeleteButton(onClick: () -> Unit = {}) {
    val textSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_size_medium).toSp() }

    // LocalDensity.current cung cấp thông tin về độ phân giải của màn hình hiện tại

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.spacing_xl)),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.spacing_m),
            vertical = dimensionResource(id = R.dimen.spacing_s)
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit",
            tint = Color(0xFF2F5D50),
            modifier = Modifier.size(dimensionResource(id = R.dimen.icon_size_small))
        )

        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_s)))

        Text("Edit", color = Color(0xFF2F5D50), fontSize = textSize) // Sử dụng TextUnit cho fontSize
    }
}

@Composable
fun AddPhotoButton(onClick: () -> Unit = {}) {
    val textSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_size_medium).toSp() }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.spacing_xl)),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.spacing_m),
            vertical = dimensionResource(id = R.dimen.spacing_s)
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Photo",
            tint = Color(0xFF2F5D50),
            modifier = Modifier.size(dimensionResource(id = R.dimen.icon_size_small))
        )

        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_s)))

        Text("Add photo", color = Color(0xFF2F5D50), fontSize = textSize) // Sử dụng TextUnit cho fontSize
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientInputList(ingredients: MutableList<String>) {

    var expandedIndex by remember { mutableStateOf(-1) }
    val options = listOf("Add source", "Add ingredient", "Remove ingredient")

    // Make ingredients a mutable state list to ensure UI reflects changes
    val ingredientList = remember { mutableStateListOf(*ingredients.toTypedArray()) }

    // Ensure that there's always at least one ingredient field
    if (ingredientList.isEmpty()) {
        ingredientList.add("") // Add an empty ingredient field if none exists
    }

    Column {
        ingredientList.forEachIndexed { index, text ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_s))
            ) {
                TextField(
                    value = text,
                    onValueChange = { newValue ->
                        ingredientList[index] = newValue
                        ingredients.clear()
                        ingredients.addAll(ingredientList)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(dimensionResource(R.dimen.spacing_xll))
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_m)))
                        .background(colorResource(R.color.text_field_background))
                        .bottomShadow(),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedTextColor = OliverGreen,
                        unfocusedTextColor = OliverGreen,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        containerColor = Color.Transparent
                    )
                )

                Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                    IconButton(onClick = {
                        expandedIndex = index
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }

                DropdownMenu(
                    expanded = expandedIndex == index,
                    onDismissRequest = { expandedIndex = -1 }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                expandedIndex = -1
                                when (option) {
                                    "Add ingredient" -> {
                                        ingredientList.add(index + 1, "")
                                        ingredients.clear()
                                        ingredients.addAll(ingredientList)
                                    }

                                    "Remove ingredient" -> {
                                        if (ingredientList.size > 1) {
                                            ingredientList.removeAt(index)
                                            ingredients.clear()
                                            ingredients.addAll(ingredientList)
                                        }
                                    }

                                    "Add source" -> {
                                        // TODO: Implement source attachment logic
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructionInputList(
    instructions: MutableList<Instruction>,
    onInstructionChanged: (Int, Instruction) -> Unit
) {
    var expandedIndex by remember { mutableStateOf(-1) }
    val options = listOf("Add step", "Delete this step", "Add source")

    val instructionList = remember { mutableStateListOf(*instructions.toTypedArray()) }

    if (instructionList.isEmpty()) {
        instructionList.add(Instruction(description = "")) // Thêm bước mới nếu không có bước nào
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
    ) {
        instructionList.forEachIndexed { index, instruction ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs)),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
                ) {
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_xl))
                            .background(colorResource(R.color.text_field_background), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "${index + 1}", color = OliverGreen)
                    }

                    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                        IconButton(onClick = {
                            expandedIndex = index
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }

                        DropdownMenu(
                            expanded = expandedIndex == index,
                            onDismissRequest = { expandedIndex = -1 }
                        ) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        expandedIndex = -1
                                        when (option) {
                                            "Add step" -> {

                                                instructionList.add(index + 1, Instruction(description = "", imageUrl = emptyList()))
                                                instructions.clear() // Xóa hết danh sách instructions cũ
                                                instructions.addAll(instructionList)
                                            }

                                            "Delete this step" -> {
//                                                recipeViewModel.deleteInstructionAt(index)
                                                instructionList.removeAt(index)
                                                instructions.clear() // Xóa hết danh sách instructions cũ
                                                instructions.addAll(instructionList)
                                            }

                                            "Add source" -> {
                                                // optional: you could trigger an image picker
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
                ) {
                    OutlinedTextField(
                        value = instruction.description,
                        onValueChange = { newDescription ->
                            // Cập nhật mô tả mới cho bước tại index
                            instructionList[index] = instruction.copy(description = newDescription)
                            instructions.clear() // Xóa hết danh sách instructions cũ
                            instructions.addAll(instructionList)
                        },
                        singleLine = false,
                        maxLines = Int.MAX_VALUE,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.spacing_xll))
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_m)))
                            .background(colorResource(R.color.text_field_background))
                            .bottomShadow(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedTextColor = OliverGreen,
                            unfocusedTextColor = OliverGreen,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                        )
                    )

                    InstructionWithImages(
                        index = index,
                        instruction = remember { mutableStateOf(instruction) } , // Sử dụng mutableStateOf để tạo MutableState<Instruction>
                        onInstructionChanged = { updatedInstruction ->
                            // Cập nhật lại instruction trong danh sách khi có thay đổi
                            instructionList[index] = updatedInstruction
                            onInstructionChanged(index, updatedInstruction)
                        }
                    )

                }
            }
        }
    }
}


@Composable
fun InstructionWithImages(
    index: Int,
    instruction: MutableState<Instruction>,  // Thay vì instruction, dùng MutableState để thay đổi trực tiếp
    onInstructionChanged: (Instruction) -> Unit
) {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }


    val imageUris = remember { mutableStateListOf(*instruction.value.imageUrl.toTypedArray()) }


    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        Log.d("AddRecipeScreen", "Selected image URI: $uri")
        uri?.let {
            // Gọi hàm upload ảnh lên Cloudinary
            CloudinaryUploader.uploadImageFromUri(
                context = context,
                uri = it,
                uploadPreset = "koylin_unsigned",
                onSuccess = { imageUrl ->
                    // Khi upload thành công, nhận đường link ảnh
                    Log.d("Cloudinary", "Image URL: $imageUrl")
                    // Sử dụng đường link ảnh (ví dụ: lưu vào database, hiển thị trong UI)

                    imageUris.add(imageUrl)
                    // Cập nhật lại instruction trực tiếp từ mutableStateOf
                    instruction.value = instruction.value.copy(
                        imageUrl = imageUris.toList()
                    )
                    // Gọi hàm onInstructionChanged để thông báo thay đổi
                    onInstructionChanged(instruction.value)
                },
                onError = { e ->
                    Log.e("Cloudinary", "Upload failed", e)
                }
            )
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            Log.d("AddRecipeScreen", "Image captured successfully URI: $cameraImageUri")
            cameraImageUri.value?.let { uri ->
                // Gọi hàm upload ảnh lên Cloudinary
                CloudinaryUploader.uploadImageFromUri(
                    context = context,
                    uri = uri,
                    uploadPreset = "koylin_unsigned",
                    onSuccess = { imageUrl ->
                        // Khi upload thành công, nhận đường link ảnh
                        Log.d("Cloudinary", "Image URL: $imageUrl")
                        // Sử dụng đường link ảnh (ví dụ: lưu vào database, hiển thị trong UI)

                        imageUris.add(imageUrl)
                        // Cập nhật lại instruction trực tiếp từ mutableStateOf
                        instruction.value = instruction.value.copy(
                            imageUrl = imageUris.toList()
                        )
                        // Gọi hàm onInstructionChanged để thông báo thay đổi
                        onInstructionChanged(instruction.value)
                    },
                    onError = { e ->
                        Log.e("Cloudinary", "Upload failed", e)
                    }
                )
            }
        }
    }


    fun createImageUri(context: Context): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )!!
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Add a photo") },
            confirmButton = {
                Column {
                    TextButton(onClick = {
                        showDialog.value = false
                        pickImageLauncher.launch("image/*")
                    }) {
                        Text("Choose from gallery")
                    }
                    TextButton(onClick = {
                        showDialog.value = false
                        val uri = createImageUri(context)
                        cameraImageUri.value = uri
                        takePictureLauncher.launch(uri)
                    }) {
                        Text("Take a photo")
                    }
                }
            }
        )
    }

    Column {
        LazyRow {
            itemsIndexed(imageUris) { idx, uri ->
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    IconButton(
                        onClick = {
                            // Xóa ảnh tại vị trí idx trong danh sách imageUris
                            imageUris.removeAt(idx)
                            // Cập nhật lại instruction trực tiếp từ mutableStateOf
                            instruction.value = instruction.value.copy(
                                imageUrl = imageUris.toList()
                            )
                            onInstructionChanged(instruction.value)

                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove image",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                        .clickable { showDialog.value = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add image",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
