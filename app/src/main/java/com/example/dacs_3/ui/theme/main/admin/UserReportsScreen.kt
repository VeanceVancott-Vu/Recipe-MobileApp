package com.example.dacs_3.ui.theme.main.admin

import UserReportViewModel
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.R
import com.example.dacs_3.model.UserReport
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.ui.theme.main.SectionTitle
import com.example.dacs_3.utils.BottomNavBar
import com.example.dacs_3.utils.ReportSummary
import com.example.dacs_3.viewmodel.AuthViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Check
import compose.icons.fontawesomeicons.solid.Hourglass
import compose.icons.fontawesomeicons.solid.SlidersH

@Composable
fun UserReportsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    userReportViewModel: UserReportViewModel,
    authViewModel: AuthViewModel
) {

    val allUserReport by userReportViewModel.userReports.collectAsState()
    LaunchedEffect(Unit) {
        userReportViewModel.fetchAllReports()
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

                SectionTitle(
                    text = "User Reports"
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

        UserReportTableWithReasonDialog(reports = allUserReport, userReportViewModel = userReportViewModel, authViewModel = authViewModel, navController = navController)

        DeleteProcessedReportsButton(
            onClick = {}
        )

    }

}


@Composable
fun UserReportTableWithReasonDialog(
    reports: List<UserReport>,
    modifier: Modifier = Modifier,
    navController: NavController,
    userReportViewModel: UserReportViewModel,
    authViewModel: AuthViewModel
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
            Text(text = "Rpt. User", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color(0xFF567F67))
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
                    text = report.reportedUserId.let {
                        if (it.length > 5) it.take(5) + "..." else it
                    },
                    modifier = Modifier.weight(1f)
                        .clickable { navController.navigate("other_user_profile/${report.reportedUserId}") },

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
                var expanded by remember { mutableStateOf(false) }
                val showSuccessDialog = remember { mutableStateOf(false) }

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
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Mark as Pending") },
                        onClick = {
                            expanded = false
                            userReportViewModel.updateUserReportStatus(report.id, "Pending")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Report") },
                        onClick = {
                            expanded = false
                            userReportViewModel.deleteUserReport(report.id)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Comment") },
                        onClick = {
                            expanded = false
                            authViewModel.deleteAccount(report.reportedUserId) { isSuccess ->
                                if (isSuccess) {
                                    userReportViewModel.deleteUserReport(report.id)
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

@Composable
fun DeleteProcessedReportsButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(16.dp)
            .height(48.dp),
        shape = RoundedCornerShape(24.dp), // bo góc tròn giống hình
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3D664A), // màu xanh đậm gần giống hình
            contentColor = Color.White           // chữ màu trắng
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp
        )
    ) {
        Text(
            text = "Delete all reports",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun UserReportsScreenPreview() {
    val navController = rememberNavController()
  //  UserReportsScreen(navController = navController)
}