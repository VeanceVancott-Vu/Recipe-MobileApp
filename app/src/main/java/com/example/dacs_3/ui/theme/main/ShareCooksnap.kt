package com.example.dacs_3.ui.theme.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.dacs_3.R
import com.example.dacs_3.ui.theme.OliverGreen
import androidx.compose.material3.Text as Text

@Composable
fun ShareCooksnapScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                        start = dimensionResource(R.dimen.spacing_m), end = dimensionResource(
                            R.dimen.spacing_m
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically

            ) {
                // ← Back icon
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable {

                        },
                    tint = OliverGreen
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = OliverGreen)
                ) {
                    Text(
                        text = "Save"
                    )
                }

            }
        }

        Image(
            painter = rememberAsyncImagePainter(
                model = "",
                placeholder = painterResource(R.drawable.loading),
                error = painterResource(R.drawable.uploadfailed)
            ),
            contentDescription = "Food Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)

        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(R.dimen.spacing_m), end = dimensionResource(
                        R.dimen.spacing_m
                    )
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Share your cooking experience ...") },
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
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun ShareCooksnapPreview() {
    ShareCooksnapScreen()
}