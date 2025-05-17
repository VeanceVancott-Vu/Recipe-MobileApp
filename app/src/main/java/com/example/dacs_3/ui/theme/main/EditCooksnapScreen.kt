package com.example.dacs_3.ui.theme.main

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.dacs_3.R
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.viewmodel.ShareCooksnapViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Camera
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dacs_3.viewmodel.EditCooksnapViewModel

@Composable
fun EditCooksnapScreen(
    navController: NavController,
    cooksnapId: String,
    initialImageUrl: String,
    initialDescription: String,
    viewModel: EditCooksnapViewModel = viewModel(), // Hoặc tạo ViewModel riêng cho edit nếu cần
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // State để quản lý imageUrl (có thể đổi)
    var imageUrl by remember { mutableStateOf(initialImageUrl) }

    // State để quản lý mô tả (có thể sửa)
    var description by remember { mutableStateOf(initialDescription) }

    // Trạng thái gửi, lỗi, thành công
    val sending by viewModel.sending.collectAsState()
    val error by viewModel.error.collectAsState()
    val success by viewModel.success.collectAsState()

    // Launcher chọn ảnh mới từ gallery hoặc camera
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.uploadImageFromUri(
                context = context,
                uri = it,
                onSuccess = { uploadedUrl ->
                    imageUrl = uploadedUrl
                },
                onError = { e ->
                    Toast.makeText(context, "Upload ảnh thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.height_top_bar)),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(R.dimen.spacing_m),
                        end = dimensionResource(R.dimen.spacing_m)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable {
                            navController.popBackStack()
                            viewModel.reset()
                        },
                    tint = OliverGreen
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        viewModel.updateCooksnap(cooksnapId, imageUrl, description)
                    },
                    enabled = !sending && description.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = OliverGreen)
                ) {
                    Text(text = "Save")
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_m)))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Cooksnap Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Icon(
                imageVector = FontAwesomeIcons.Solid.Camera,
                contentDescription = "Change Image",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(36.dp)
                    .clickable {
                        pickImageLauncher.launch("image/*")
                    },
                tint = OliverGreen
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(R.dimen.spacing_m),
                    end = dimensionResource(R.dimen.spacing_m)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Edit your cooking experience...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.spacing_m) * 15)
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.spacing_m)))
                    .background(colorResource(R.color.text_field_background)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    selectionColors = LocalTextSelectionColors.current,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                )
            )

            if (sending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            error?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(8.dp))
            }

            if (success) {
                LaunchedEffect(Unit) {
                    Toast.makeText(context, "Cooksnap đã được cập nhật!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                    viewModel.reset()
                }
            }
        }
    }
}
