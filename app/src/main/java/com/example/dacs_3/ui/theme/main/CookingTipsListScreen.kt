package com.example.dacs_3.ui.theme.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dacs_3.R
import com.example.dacs_3.ui.theme.OliverGreen

@Composable
fun CookingTipsListScreen() {
    Column() {
        CookingTipsTopBar(
            title = "Extract cooking tips", // Truyền tiêu đề
            onBackClick = { /* Sự kiện click Back */ },
            onSearchClick = { /* Sự kiện click Search */ }
        )

        
    }
}


@Composable
fun CookingTipsTopBar(
    title: String,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
        ) {
            // ← Back icon
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable { onBackClick() }, // Sự kiện click Back
                tint = OliverGreen
            )

            SectionTitle(
                text = title
            )

            Spacer(modifier = Modifier.weight(1f))

            // Search icon
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.clickable { onSearchClick() }, // Sự kiện click Search
                tint = OliverGreen
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CookingTipsListScreenPreview() {
    CookingTipsListScreen()
}