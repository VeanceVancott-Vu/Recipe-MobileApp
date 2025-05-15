package com.example.dacs_3.ui.theme.main.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import compose.icons.fontawesomeicons.solid.Check
import java.time.LocalDate
import android.app.DatePickerDialog
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.time.format.DateTimeFormatter


@Composable
fun FilterReportScreen(
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

                SectionTitle(text = "Filter")

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = FontAwesomeIcons.Solid.Check,
                    contentDescription = "Filter",
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_medium)),
                    tint = OliverGreen
                )

            }
        }

        ReportFilterSection()
    }
}

@Composable
fun ReportFilterSection() {
    val context = LocalContext.current

    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }

    // Hàm format ngày đẹp ví dụ "15 May 2025"
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")


    val buttonModifier = Modifier
        .padding(3.dp)
        .height(40.dp)
        .defaultMinSize(minWidth = 90.dp)

    // Hàm mở DatePickerDialog cho startDate
    fun openStartDatePicker() {
        val now = LocalDate.now()
        val dialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                startDate = LocalDate.of(year, month + 1, dayOfMonth)
            },
            now.year, now.monthValue - 1, now.dayOfMonth
        )
        dialog.show()
    }

    // Hàm mở DatePickerDialog cho endDate
    fun openEndDatePicker() {
        val now = LocalDate.now()
        val dialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                endDate = LocalDate.of(year, month + 1, dayOfMonth)
            },
            now.year, now.monthValue - 1, now.dayOfMonth
        )
        dialog.show()
    }


    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        SectionTitle(text = "Report Date")

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_s)))

        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
        ) {
            OutlinedButton(
                onClick = { openStartDatePicker() },
                modifier = buttonModifier,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF84A68D),
                    containerColor = Color.White
                ),
                border = BorderStroke(1.dp, Color(0xFFD0D0D0)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select start date",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = startDate?.format(formatter) ?: "Start date")
            }

            OutlinedButton(
                onClick = { openEndDatePicker() },
                modifier = buttonModifier,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF84A68D),
                    containerColor = Color.White
                ),
                border = BorderStroke(1.dp, Color(0xFFD0D0D0)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select end date",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = endDate?.format(formatter) ?: "End date")
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
        ) {
            listOf("Last day", "Last week", "Last month").forEach { label ->
                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = buttonModifier,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF84A68D),
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color(0xFFD0D0D0)),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(label)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle(text = "Report Status")

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_s)))

        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
        ) {
            listOf("Unresolved", "Processed").forEach { label ->
                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = buttonModifier,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF84A68D),
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color(0xFFD0D0D0)),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(label)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FilterReportScreenPreview() {
    val navController = rememberNavController()
    FilterReportScreen(navController = navController)
}