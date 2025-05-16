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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.R
import com.example.dacs_3.model.CommentReport
import com.example.dacs_3.model.Cooksnap
import com.example.dacs_3.model.CooksnapReport
import com.example.dacs_3.model.RecipeReport
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.ui.theme.main.SectionTitle
import com.example.dacs_3.utils.ReportSummary
import com.example.dacs_3.utils.countCommentReportStatuses
import com.example.dacs_3.viewmodel.CommentReportsViewModel
import com.example.dacs_3.viewmodel.CommentViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.SlidersH

@Composable
fun CommentReportsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    commentReportViewModel : CommentReportsViewModel,
    commentViewModel: CommentViewModel
) {
    val sampleCommentReports = listOf(
        CommentReport(
            reportingUserId = "userA01",
            reportedUserId = "userB01",
            reportedCommentId = "comment1001",
            reason = "Spam content",
            status = "Unresolved",
            date = 1684000000000
        ),
        CommentReport(
            reportingUserId = "userA02",
            reportedUserId = "userB02",
            reportedCommentId = "comment1002",
            reason = "Offensive language",
            status = "Processed",
            date = 1684100000000
        ),
        CommentReport(
            reportingUserId = "userA03",
            reportedUserId = "userB03",
            reportedCommentId = "comment1003",
            reason = "Irrelevant content",
            status = "Unresolved",
            date = 1684200000000
        )
    )

    val allCommentReports by commentReportViewModel.allReports.collectAsState()
    LaunchedEffect(Unit) {
        commentReportViewModel.fetchReports()
    }

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
                    text = "Comment Reports"
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


        val (pending, resolved) = countCommentReportStatuses(allCommentReports)
        ReportSummary(pending.toString(), resolved.toString())


        CommentReportTableWithReasonDialog(reports = allCommentReports,
            navController = navController,
            commentViewModel = commentViewModel,
            commentReportViewModel = commentReportViewModel)


        val resolvedReports = allCommentReports.filter { it.status == "Resolved" }
        DeleteProcessedReportsButton(
            onClick = {
                commentReportViewModel.deleteReports(resolvedReports)
            }
        )
    }

}

@Composable
fun CommentReportTableWithReasonDialog(
    reports: List<CommentReport>,
    modifier: Modifier = Modifier,
    navController: NavController,
    commentViewModel: CommentViewModel,
    commentReportViewModel: CommentReportsViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedReason by remember { mutableStateOf("") }
    val comment by commentViewModel.selectedComment.collectAsState()

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
                    text = report.reportedCommentId.let {
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
                            commentViewModel.fetchCommentById(report.reportedCommentId)
                            selectedReason = report.reason
                            showDialog = true
                        },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF567F67),
                    fontWeight = FontWeight.Normal
                )

                // Status box
                // Status box
                var expanded by remember { mutableStateOf(false) }
                val showSuccessDialog = remember { mutableStateOf(false) }


                // This is your clickable Box that shows the status
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE6F0E9))
                        .clickable { expanded = true }
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

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Mark as Pending") },
                        onClick = {
                            expanded = false
                            commentReportViewModel.updateReportStatus(report.id, "Pending")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Report") },
                        onClick = {
                            expanded = false
                            commentReportViewModel.updateReportStatus(report.id, "Resolved")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Comment") },
                        onClick = {
                            expanded = false
                            commentViewModel.deleteCommentByAdmin(report.reportedCommentId) { isSuccess ->
                                if (isSuccess) {
                                    commentReportViewModel.updateReportStatus(report.id, "Resolved")
                                    showSuccessDialog.value = true
                                } else {
                                    // You could show a Snackbar or Dialog for failure here
                                    showSuccessDialog.value = false
                                }
                            }
                        }
                    )
                }

                if (showSuccessDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showSuccessDialog.value = false },
                        title = { Text("Success") },
                        text = { Text("The comment has been deleted.") },
                        confirmButton = {
                            TextButton(onClick = { showSuccessDialog.value = false }) {
                                Text("OK")
                            }
                        }
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
            text = { Text(text = selectedReason) },
            confirmButton = {
                Row {
                    TextButton(onClick = {
                        showDialog = false
                        // Navigate to the recipe screen here
                        navController.navigate("recipeDetail/${comment?.recipeId}")
                    }) {
                        Text("See Recipe")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(onClick = { showDialog = false }) {
                        Text("Close")
                    }
                }
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun CommentReportsScreenPreview() {
    val navController = rememberNavController()
  //  CommentReportsScreen(navController = navController)
}