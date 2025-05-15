package com.example.dacs_3.ui.theme.main.admin

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.R
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.ui.theme.main.SectionTitle
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Camera
import compose.icons.fontawesomeicons.solid.CameraRetro
import compose.icons.fontawesomeicons.solid.Check
import compose.icons.fontawesomeicons.solid.Comment
import compose.icons.fontawesomeicons.solid.File
import compose.icons.fontawesomeicons.solid.Hourglass
import compose.icons.fontawesomeicons.solid.User

@Composable
fun ViolationReportsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))

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

                Spacer(modifier = Modifier.weight(1f))

                SectionTitle(text = "Violation Reports")

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        ViolationReportSummary()

    }
}

@Composable
fun ViolationReportSummary() {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_l))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.spacing_m))
        ) {
            ReportSummaryCard(
                icon = FontAwesomeIcons.Solid.File,
                titleLine1 = "Recipe",
                titleLine2 = "Reports",
                count = "28"
            )

            Spacer(modifier = Modifier.weight(1f))

            ReportSummaryCard(
                icon = FontAwesomeIcons.Solid.User,
                titleLine1 = "User",
                titleLine2 = "Reports",
                count = "126"
            )
        }

        Row(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.spacing_m))
        ) {
            ReportSummaryCard(
                icon = FontAwesomeIcons.Solid.Comment,
                titleLine1 = "Comment",
                titleLine2 = "Reports",
                count = "28"
            )

            Spacer(modifier = Modifier.weight(1f))

            ReportSummaryCard(
                icon = FontAwesomeIcons.Solid.Camera,
                titleLine1 = "Cooksnap",
                titleLine2 = "Reports",
                count = "126"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViolationReportsScreenPreview() {
    val navController = rememberNavController()
    ViolationReportsScreen(navController = navController)
}