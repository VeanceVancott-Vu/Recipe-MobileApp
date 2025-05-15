package com.example.dacs_3.ui.theme.main.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.R
import com.example.dacs_3.model.Cooksnap
import com.example.dacs_3.model.CooksnapReport
import com.example.dacs_3.model.RecipeReport
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.ui.theme.main.SectionTitle
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.SlidersH

@Composable
fun CooksnapReportsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val sampleCooksnapReports = listOf(
        CooksnapReport(
            reportingUserId = "user101",
            reportedUserId = "user202",
            reportedCooksnapId = "cooksnap301",
            reason = "Inappropriate image content",
            status = "Unresolved",
            date = 1684000000000 // ví dụ timestamp
        ),
        CooksnapReport(
            reportingUserId = "user102",
            reportedUserId = "user203",
            reportedCooksnapId = "cooksnap302",
            reason = "Spam or advertisement",
            status = "Processed",
            date = 1684100000000
        ),
        CooksnapReport(
            reportingUserId = "user103",
            reportedUserId = "user204",
            reportedCooksnapId = "cooksnap303",
            reason = "Offensive language in description",
            status = "Unresolved",
            date = 1684200000000
        )
    )


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m)),
        horizontalAlignment = Alignment.CenterHorizontally

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

                SectionTitle(
                    text = "Cooksnap Reports"
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = FontAwesomeIcons.Solid.SlidersH,
                    contentDescription = "Filter",
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_medium)),
                    tint = OliverGreen
                )

            }
        }

        ReportSummary()

        CooksnapReportTableWithReasonDialog(reports = sampleCooksnapReports)

        DeleteProcessedReportsButton(
            onClick = {}
        )
    }

}

@Composable
fun CooksnapReportTableWithReasonDialog(
    reports: List<CooksnapReport>,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedReason by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.spacing_m))
            .padding(top = dimensionResource(R.dimen.spacing_xl))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE6F0E9))
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Rep. User", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color(0xFF567F67))
            Text(text = "Rpt. Recipe", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color(0xFF567F67))
            Text(text = "Reason", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color(0xFF567F67))
            Text(text = "Status", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color(0xFF567F67))
        }

        Divider(color = Color.LightGray, thickness = 1.dp)

        reports.forEach { report ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rep. User, 1 dòng, ellipsis
                Text(
                    text = report.reportingUserId.let {
                        if (it.length > 5) it.take(5) + "..." else it
                    },
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF567F67)
                )

                // Rpt. User, 1 dòng, ellipsis
                Text(
                    text = report.reportedCooksnapId.let {
                        if (it.length > 5) it.take(5) + "..." else it
                    },
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF567F67)
                )

                // Reason, 1 dòng, ellipsis, có thể nhấn được để mở dialog
                Text(
                    text = report.reason.let {
                        if (it.length > 5) it.take(5) + "..." else it
                    },
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedReason = report.reason
                            showDialog = true
                        },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF567F67),
                    fontWeight = FontWeight.Normal
                )

                // Status box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE6F0E9))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = report.status,
                        color = Color(0xFF567F67),
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    )
                }
            }

            Divider(color = Color.LightGray, thickness = 1.dp)
        }
    }

    // Dialog hiện khi nhấn reason
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Reason Detail") },
            text = {
                Text(text = selectedReason)
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CooksnapReportsScreenPreview() {
    val navController = rememberNavController()
    CooksnapReportsScreen(navController = navController)
}