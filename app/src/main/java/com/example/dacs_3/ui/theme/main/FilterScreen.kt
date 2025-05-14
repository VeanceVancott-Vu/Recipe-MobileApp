package com.example.dacs_3.ui.theme.main

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dacs_3.R
import com.example.dacs_3.ui.theme.OliverGreen

@Composable
fun FilterScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var query by remember { mutableStateOf("") }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.height_top_bar)),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(R.dimen.spacing_m), end = dimensionResource(
                        R.dimen.spacing_m)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))

            ) {
                // ← Back icon
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable {
                            navController.popBackStack() // Quay lại màn hình trước (SearchScreen)
                        },
                    tint = OliverGreen
                )

                SectionTitle(
                    text = "Filter"
                )

            }
        }

        Column(
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.spacing_m), end = dimensionResource(
                    R.dimen.spacing_m)
                )
        ) {
            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_xl)))

            Text(
                text = "Show dishes with:",
                fontSize = with(LocalDensity.current) {
                    dimensionResource(R.dimen.text_size_medium_large).toSp()
                },
                color = OliverGreen
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_s)))

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Type ingredient names ... ") },
                    modifier = Modifier
                        .fillMaxWidth()
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

            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_xl)))

            Text(
                text = "Show dishes without:",
                fontSize = with(LocalDensity.current) {
                    dimensionResource(R.dimen.text_size_medium_large).toSp()
                },
                color = OliverGreen,
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_s)))

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Type ingredients ...") },
                    modifier = Modifier
                        .fillMaxWidth()
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

            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_xll)))

            Row() {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD1E8D8),
                        contentColor = OliverGreen
                    ),
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.spacing_xll))
                ) {
                    Text(
                        text = "Delete",
                        fontSize = with(LocalDensity.current) {
                            dimensionResource(R.dimen.text_size_medium_large).toSp()
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OliverGreen,
                        contentColor = Color.White     // màu chữ hoặc icon
                    ),
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.spacing_xll))
                ) {
                    Text(
                        text = "Show dishes",
                        fontSize = with(LocalDensity.current) {
                            dimensionResource(R.dimen.text_size_medium_large).toSp()
                        }
                    )
                }
            }
        }
    }
}

