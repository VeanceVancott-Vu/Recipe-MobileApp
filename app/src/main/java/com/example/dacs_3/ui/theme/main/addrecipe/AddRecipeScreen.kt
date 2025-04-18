package com.example.dacs_3.ui.theme.main.addrecipe

import ai.codia.x.composeui.demo.AddRecipeScreen
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.dacs_3.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.dacs_3.model.Instruction
import com.example.dacs_3.utils.bottomShadow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreenn(
    onBack: () -> Unit,
    onAddPhoto: () -> Unit,
    onEditPhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val instructions = remember { mutableStateListOf<Instruction>() }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = {}) { Text(text = "Post") }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {}) {
                Text(text = "Save Draft")
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
                value = "",
                onValueChange = {},
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
                value = "",
                onValueChange = {},
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
                    value = "",
                    onValueChange = {},
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
                    value = "",
                    onValueChange = {},
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
            IngredientInputList()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.spacing_s)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
        ) {
            Text("Instructions")

            // Bắt đầu sửa
            DisplayCookingInstructions(stepNumber = 1)
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
fun IngredientInputList() {
    var textFields by remember { mutableStateOf(listOf("")) }
    var expandedIndex by remember { mutableStateOf(-1) }
    val options = listOf("Add source", "Add ingredient", "Remove ingredient")

    Column(
        modifier = Modifier
    ) {
        textFields.forEachIndexed { index, text ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_s))
            ) {
                TextField(
                    value = text,
                    onValueChange = { newValue ->
                        textFields = textFields.toMutableList().apply {
                            this[index] = newValue
                        }
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
                    onDismissRequest = { expandedIndex = -1 } // Tự động ẩn khi nhấn ra ngoài
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                expandedIndex = -1
                                when (option) {
                                    "Add ingredient" -> {
                                        textFields = textFields.toMutableList().apply {
                                            add(index + 1, "")
                                        }
                                    }
                                    "Remove ingredient" -> {
                                        if (textFields.size > 1) {
                                            textFields = textFields.toMutableList().apply {
                                                removeAt(index)
                                            }
                                        }
                                    }
                                    "Add source" -> {
                                        // TODO:
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
fun DisplayCookingInstructions(
    stepNumber: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
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
                Text(
                    text = stepNumber.toString()
                )
            }

            StepOptionsMenu()
        }

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
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

            InstructionWidthImages()
            ShowAttachments()
        }
    }

}

@Composable
fun StepOptionsMenu() {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Add steps", "Delete this step", "Add a source")

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } // Tự động ẩn khi nhấn ra ngoài
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        expanded = false
                        // TODO: Thực hiện hành động với từng lựa chọn
                    }
                )
            }
        }
    }
}

@Composable
fun InstructionWidthImages() {
    val context = LocalContext.current
    val imageList = remember { mutableStateListOf<String>() }
    val showDialog = remember { mutableStateOf(false) }
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    // 1. Launcher chọn ảnh từ thư viện
    // Đăng kí 1 intent ---> Mở

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        uri: Uri? ->
        uri?.let {
            imageList.add(0, it.toString())
        }
    }

    // 2. Launcher chụp ảnh từ camera
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {
        success ->
        if (success) {
            cameraImageUri.value?.let {
                imageList.add(0, it.toString())
            }
        }
    }

    // 3. Hàm tạo Uri để lưu ảnh chụp
    fun createImageUri(context: Context): Uri {
        val contenValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }

        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contenValues
        )!!
    }

    // 4. Dialog chọn nguồn ảnh
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                Column {
                    TextButton(onClick = {
                        showDialog.value = false
                        pickImageLauncher.launch("image/*")
                    }) {
                        Text("Từ thư viện")
                    }

                    TextButton(onClick = {
                        showDialog.value = false
                        val uri = createImageUri(context)
                        cameraImageUri.value = uri
                        takePictureLauncher.launch(uri)
                    }) {
                        Text("Chụp ảnh")
                    }
                }
            },
            title = {
                Text("Chọn cách thêm ảnh")
            }
        )
    }

    // 5. Giao diện
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
    ) {
        // Ảnh đã chọn
        itemsIndexed(imageList) { index, uriString ->
            val painter = rememberAsyncImagePainter(uriString)

            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(R.dimen.spacing_s))
                    .background(Color.LightGray),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(dimensionResource(R.dimen.spacing_xs)),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = {
                        pickImageLauncher.launch("image/*")
                        imageList.removeAt(index)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Change",
                            tint = Color.White
                        )
                    }

                    IconButton(onClick = {
                        imageList.removeAt(index)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // Ảnh trống cuối cùng
        item {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_s)))
                    .background(Color(0xFFF5F2EF))
                    .clickable {
                        showDialog.value = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Add",
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium)),
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ShowAttachments() {
    val fileList = remember { mutableStateListOf<String>() }

    Column(modifier = Modifier.padding(dimensionResource(R.dimen.spacing_m))) {
        Button(onClick = {
            fileList.add("Attach file")
        }) {
            Text("Theem file")
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_m)))

        // Danh sách dòng file đã thêm
        fileList.forEachIndexed { index, fileName ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.spacing_xs))
            ) {
                Icon(
                    painter = painterResource(R.drawable.attach),
                    contentDescription = "File attach"
                )

                Text(
                    text = fileName,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = dimensionResource(R.dimen.spacing_s))
                )

                IconButton(onClick = {
                    fileList.removeAt(index)
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove file"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRecipeScreenPreview() {
    AddRecipeScreenn(
        onBack = {},
        onAddPhoto = {},
        onEditPhoto = {}
    )
}
