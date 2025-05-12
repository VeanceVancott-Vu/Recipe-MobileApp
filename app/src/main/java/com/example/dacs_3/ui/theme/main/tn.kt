package com.example.dacs_3.ui.theme.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.dp
@Composable
fun TestRatingStars() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(50) { index ->
            val rating = (index + 1) * 0.1f
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Rating: ${"%.1f".format(rating)}")
                RatingStars(rating = rating)
            }
        }
    }
}

@Composable
fun RatingStars(rating: Float) {
    Row {
        repeat(5) { index ->
            val starFill = when {
                index + 1 <= rating -> 1f
                index.toFloat() < rating -> rating - index
                else -> 0f
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star ${index + 1}",
                    tint = Color.Gray,
                    modifier = Modifier.fillMaxSize()
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star ${index + 1}",
                    tint = Color.Yellow,
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithContent {
                            val fillWidth = size.width * starFill
                            clipRect(0f, 0f, fillWidth, size.height) {
                                this@drawWithContent.drawContent()
                            }
                        }
                )
            }
        }
    }
}
