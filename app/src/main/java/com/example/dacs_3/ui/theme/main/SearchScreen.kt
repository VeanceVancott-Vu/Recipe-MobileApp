package com.example.dacs_3.ui.theme.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dacs_3.R
import com.example.dacs_3.ui.theme.OliverGreen
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Clock
import compose.icons.fontawesomeicons.solid.Clock
import compose.icons.fontawesomeicons.solid.SlidersH

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onSearch: (String) -> Unit = {},
    onFilter: () -> Unit = {}
) {
    // Lịch sử tìm kiếm giả
    val fakeHistory = listOf(
        "Spaghetti Bolognese",
        "Classic Pancakes",
        "Grilled Chicken Salad",
        "Beef Tacos",
        "Strawberry Cheesecake"
    )

    var text by remember { mutableStateOf("") }

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
                    .padding(start = dimensionResource(R.dimen.spacing_m), end = dimensionResource(R.dimen.spacing_m)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))

            ) {
                // ← Back icon
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { onBack() } , // Thêm sự kiện click
                    tint = OliverGreen
                )


                Card(
                    modifier = Modifier
                        .weight(1f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)), // Bo góc cho Card
                ) {
                    // TextField dạng search
                    val containerColor = Color(0xFFDBE6DE)  // Nền xanh nhạt
                    TextField(
                        value = query,
                        onValueChange = {
                            query = it
                            onSearch(it)
                        },
                        placeholder = {
                            Text(
                                text = "Enter ingredient names …",
                            )
                        },
                        singleLine = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = Modifier
                                    .size(dimensionResource(R.dimen.icon_size_medium)),
                                tint = OliverGreen
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = containerColor,
                            unfocusedContainerColor = containerColor,
                            disabledContainerColor = containerColor,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)),
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                    )
                }




                // Filter/Settings icon
                Icon(
                    imageVector = FontAwesomeIcons.Solid.SlidersH,
                    contentDescription = "Filter",
                    modifier = Modifier
                        .clickable(onClick = onFilter)
                        .size(dimensionResource(R.dimen.icon_size_medium)),
                    tint = OliverGreen
                )

            }
        }

        Spacer(Modifier.height(12.dp))

        // --- 2. List lịch sử giả ---
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(fakeHistory) { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // khi click vào item, có thể đặt lại text để search lại
                            text = item
                        }
                        .padding(vertical = dimensionResource(R.dimen.spacing_s))
                        .padding(start = dimensionResource(R.dimen.spacing_m), end = dimensionResource(R.dimen.spacing_m))
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Regular.Clock,
                        contentDescription = null,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_average)),
                        tint = OliverGreen
                    )

                    Spacer(Modifier.width(dimensionResource(R.dimen.spacing_xl)))

                    Text(
                        text = item,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Xóa",
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_medium))
                            .clickable {
                                // Xử lý xóa item giả
                            },
                        tint = OliverGreen
                    )

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    SearchScreen()
}