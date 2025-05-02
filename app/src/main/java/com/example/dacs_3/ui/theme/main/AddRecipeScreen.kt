
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.dacs_3.R
import com.example.dacs_3.model.Instruction
import com.example.dacs_3.model.Recipe
import com.example.dacs_3.utils.bottomShadow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(

) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val title = remember { mutableStateOf("") }
    val story = remember { mutableStateOf("") }
    val servingSize = remember { mutableStateOf("") }
    val cookingTime = remember { mutableStateOf("") }
    val image = remember { mutableStateOf("") }

    val ingredients = remember { mutableStateListOf<String>() }

    val instructions = remember {    mutableStateListOf(
        Instruction(stepNumber = 1)
    ) }

    val likes = remember { mutableStateOf(0) }
    val smiles = remember { mutableStateOf(0) }
    val claps = remember { mutableStateOf(0) }

    val userId = remember { mutableStateOf("") }

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

                val recipe = Recipe(
                    title = title.value,
                    story = story.value,
                    servingSize = servingSize.value,
                    cookingTime = cookingTime.value,
                    resultImages = image.value,
                    ingredients = ingredients.toList(),
                    instructions = instructions.toList(),
                    likes = likes.value,
                    smiles = smiles.value,
                    claps = claps.value,
                    userId = userId.value
                )
                Log.d("AddRecipeScreen", "Post button clicked recipe: $recipe")



                             },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F764E))
            ) { Text(text = "Post") }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { /* TODO: Save Draft */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F764E))
            ) {
                Text("Save Draft", color = Color.White)
            }

        }

        Row() {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_m)))  // Bo góc
                    .background(Color.LightGray)
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(dimensionResource(R.dimen.spacing_s))
                ) {
                    DeleteButton()
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_s)))
                    AddPhotoButton()
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

            // Bắt đầu sửa
            InstructionInputList(
                instructions = instructions,
                onInstructionsChange = {
                    instructions.clear()
                    instructions.addAll(it)
                }
            )

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
                        ingredients[index] = newValue // Update the parent list as well
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(dimensionResource(R.dimen.spacing_xll))
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_m)))
                        .background(colorResource(R.color.text_field_background))
                        .bottomShadow(),
                    colors = TextFieldDefaults.textFieldColors(
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
                                    }

                                    "Remove ingredient" -> {
                                        if (ingredientList.size > 1) {
                                            ingredientList.removeAt(index)
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
    onInstructionsChange: (List<Instruction>) -> Unit
) {
    var expandedIndex by remember { mutableStateOf(-1) }
    val options = listOf("Add step", "Delete this step", "Add source")

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
    ) {
        instructions.forEachIndexed { index, instruction ->
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
                        Text(text = "${index + 1}")
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
                                        val updatedList = instructions.toMutableList()
                                        when (option) {
                                            "Add step" -> {
                                                updatedList.add(index + 1, Instruction(stepNumber = index + 2))
                                            }

                                            "Delete this step" -> {
                                                if (updatedList.size > 1) {
                                                    updatedList.removeAt(index)
                                                }
                                            }

                                            "Add source" -> {
                                                // TODO: Handle image selection and set imageUrl
                                            }
                                        }

                                        // Update step numbers
                                        val reordered = updatedList.mapIndexed { i, it -> it.copy(stepNumber = i + 1) }
                                        onInstructionsChange(reordered)
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
                        onValueChange = { newValue ->
                            val updated = instructions.toMutableList()
                            updated[index] = instruction.copy(description = newValue)
                            onInstructionsChange(updated)
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
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                        )
                    )

                    InstructionWithImages(
                        instruction = instruction,
                        onAddImage = { uri ->
                            val updated = instructions.toMutableList()
                            val newImages = instruction.imageUrl.toMutableList().apply { add(uri) }
                            updated[index] = instruction.copy(imageUrl = newImages)
                            onInstructionsChange(updated)
                        },
                        onDeleteImage = { imageIndex ->
                            val updated = instructions.toMutableList()
                            val newImages = instruction.imageUrl.toMutableList().apply { removeAt(imageIndex) }
                            updated[index] = instruction.copy(imageUrl = newImages)
                            onInstructionsChange(updated)
                        }
                    )

                }
            }
        }
    }
}

@Composable
fun InstructionWithImages(
    instruction: Instruction,
    onAddImage: (String) -> Unit,
    onDeleteImage: (Int) -> Unit
) {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onAddImage(it.toString()) }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri.value?.let { onAddImage(it.toString()) }
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

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s))
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
        ) {
            itemsIndexed(instruction.imageUrl) { idx, url ->
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .width(120.dp)
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_s)))
                        .background(Color.LightGray)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(url),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    IconButton(
                        onClick = { onDeleteImage(idx) },
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
                        .height(100.dp)
                        .width(120.dp)
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_s)))
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


@Preview(showBackground = true)
@Composable
fun AddRecipeScreenPreview() {
    AddRecipeScreen(

    )
}
