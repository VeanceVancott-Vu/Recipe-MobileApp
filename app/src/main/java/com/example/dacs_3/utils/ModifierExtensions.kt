package com.example.dacs_3.utils

import androidx.annotation.ColorRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dacs_3.R
import androidx.compose.ui.res.colorResource as colorResource1

fun Modifier.bottomShadow(
    color: Color = Color(0xFF888888),
    height: Dp = 4.dp,
    alpha: Float = 0.5f
): Modifier = this.drawWithContent {
    drawContent()
    drawRect(
        color = color.copy(alpha = alpha),
        size = Size(width = size.width, height = height.toPx()),
        topLeft = Offset(x = 0f, y = size.height - height.toPx())
    )
}

