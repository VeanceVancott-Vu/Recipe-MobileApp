package com.example.dacs_3.ui.theme.main.admin

import android.graphics.DashPathEffect
import android.graphics.PathMeasure
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.AndroidPath
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.R
import com.example.dacs_3.ui.theme.OliverGreen
import com.example.dacs_3.utils.BottomNavBar
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Exclamation
import compose.icons.fontawesomeicons.solid.User
import compose.icons.fontawesomeicons.solid.Utensils
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import com.example.dacs_3.viewmodel.AuthViewModel
import com.example.dacs_3.viewmodel.CommentReportsViewModel
import com.example.dacs_3.viewmodel.RecipeReportsViewModel
import com.example.dacs_3.viewmodel.RecipeViewModel


@Composable
fun DashboardScreen(
    navController: NavController,
    recipeViewModel: RecipeViewModel,
    authViewModel: AuthViewModel,
    commentReportsViewModel: CommentReportsViewModel,
    recipeReportsViewModel: RecipeReportsViewModel
) {

    val weeklyData = listOf(3, 5, 2, 8, 6, 7, 4) // Số lượng món mới từ T2 đến CN

    val commentReports by commentReportsViewModel.allReports.collectAsState()
    val recipeReports by recipeReportsViewModel.allReports.collectAsState()

         val allUser by authViewModel.allUsers.collectAsState()
        val allRecipe by recipeViewModel.recipes.collectAsState()

    LaunchedEffect(Unit) {
        commentReportsViewModel.fetchReports()
        recipeReportsViewModel.loadAllReports()
        recipeViewModel.fetchRecipes()
        authViewModel.loadAllUsers()
    }

    Scaffold(

        containerColor = Color(0xFFF7F7F7),
        modifier = Modifier
            .fillMaxSize()
//            .padding(
//                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
//                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
//            )
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Important: respect Scaffold's inner padding
                .background(Color(0xFFF7F7F7))
        ) {

            item {
                DashboardHeader()
            }

            item {
                DashboardCardsSection(
                    commentReports.size+recipeReports.size,
                    allUser.size,
                    allRecipe.size,
                    navController
                    )
            }

            item { WeeklyNewDishesChart(dataPoints = weeklyData) }


        }
    }
}

@Composable
fun DashboardHeader() {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Dashboard",
                    fontSize = with(LocalDensity.current) {
                        dimensionResource(R.dimen.text_size_title).toSp()
                    },
                    color = OliverGreen,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun DashboardCardsSection(allReportsSize:Int,
                          allUserSize:Int,
                          allRecipeSize:Int,
                          navController: NavController) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_l)),
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.spacing_m))
            .padding(top = dimensionResource(R.dimen.spacing_l))
    ) {
        DashboardCardItem(
            icon = FontAwesomeIcons.Solid.Utensils,
            title = "Recipes",
            number = allRecipeSize.toString(),
            {navController.navigate("violation_reports")}
        )

        DashboardCardItem(
            icon = FontAwesomeIcons.Solid.Exclamation,
            title = "Reports",
            number = allReportsSize.toString(),
            {navController.navigate("violation_reports")}
        )

        DashboardCardItem(
            icon = FontAwesomeIcons.Solid.User,
            title = "Users",
            number = allUserSize.toString(),
            {navController.navigate("violation_reports")}
        )
    }
}


@Composable
fun DashboardCardItem(
    icon: ImageVector,
    title: String,
    number: String,
    onclick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{onclick()},
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)), // nền trắng
        shape = RoundedCornerShape(dimensionResource(R.dimen.spacing_m)), // bo góc nhỏ hơn spacing_m (ảnh bo tròn nhẹ)
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.spacing_s)) // bóng nhẹ
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(R.dimen.spacing_l),
                    horizontal = dimensionResource(R.dimen.spacing_m)
                ),
            verticalAlignment = Alignment.CenterVertically, // căn giữa theo chiều dọc
            horizontalArrangement = Arrangement.Start // để icon và text sát bên trái, number sẽ dùng Spacer đẩy sang phải
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = OliverGreen,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium))
            )

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_s)))

            Text(
                text = title,
                color = OliverGreen,
                fontWeight = FontWeight.Bold,
                fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.text_size_large).toSp() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = number,
                color = OliverGreen,
                fontWeight = FontWeight.Bold,
                fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.text_size_large).toSp() }
            )
        }
    }
}


@Composable
fun WeeklyNewDishesChart(
    dataPoints: List<Int>,    // Số lượng món mới mỗi ngày, chiều dài = 7
    modifier: Modifier = Modifier
) {
    val maxValue = (dataPoints.maxOrNull() ?: 1).coerceAtLeast(1)

    val days = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")


    // Animation khi vẽ biểu đồ
//    val animationProgress = remember { Animatable(0f) }
//    LaunchedEffect(Unit) {
//        animationProgress.animateTo(1f, animationSpec = tween(1000))
//    }

    val animationProgress = remember { Animatable(1f) }  // Khởi tạo thẳng = 1f để vẽ full đường


    Column(
        modifier = modifier.padding(
            vertical = dimensionResource(R.dimen.spacing_xl),
            horizontal = dimensionResource(R.dimen.spacing_m)
        ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s))
    ) {
        Text(
            text = "Number of New Dishes in the Week",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
            color = OliverGreen
        )


        val chartHeight = 200.dp

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight),
            shape = RoundedCornerShape(12.dp),           // Bo góc 12dp
            elevation = CardDefaults.cardElevation(8.dp), // Đổ bóng vừa phải
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray), // Viền mỏng xám nhạt
            colors = CardDefaults.cardColors(containerColor = Color.White) // Nền trắng nếu muốn
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight)
                    .padding(dimensionResource(R.dimen.spacing_m))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val spacing = w / (dataPoints.size - 1)
                    val bottomPadding = 30f

                    // Vẽ lưới ngang (5 đường)
                    val gridLines = 5
                    val gridStep = (h - bottomPadding) / gridLines
                    for (i in 0..gridLines) {
                        val y = h - bottomPadding - i * gridStep
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                        // Vẽ nhãn giá trị ở lề trái
                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                ((maxValue * i) / gridLines).toString(),
                                0f,
                                y - 4f,
                                android.graphics.Paint().apply {
                                    textSize = 30f
                                    color = android.graphics.Color.DKGRAY
                                    textAlign = android.graphics.Paint.Align.LEFT
                                }
                            )
                        }
                    }

                    // Tạo danh sách điểm trên biểu đồ
                    val pathPoints = dataPoints.mapIndexed { index, value ->
                        Offset(
                            x = index * spacing,
                            y = h - bottomPadding - (value / maxValue.toFloat()) * (h - bottomPadding)
                        )
                    }

                    // Vẽ đường nối liền mạch có animation
                    val pathLength = pathPoints.size - 1
                    val progress = animationProgress.value * pathLength

                    val fullSegments = progress.toInt()
                    val partialSegmentProgress = progress - fullSegments

                    val animatedPath = Path()

                    // Vẽ các đoạn path đã hoàn thành
                    if (fullSegments > 0) {
                        animatedPath.moveTo(pathPoints[0].x, pathPoints[0].y)
                        for (i in 1..fullSegments) {
                            animatedPath.lineTo(pathPoints[i].x, pathPoints[i].y)
                        }
                    }

                    // Vẽ đoạn path đang vẽ dở (partial segment)
                    if (fullSegments < pathLength) {
                        val start = pathPoints[fullSegments]
                        val end = pathPoints[fullSegments + 1]
                        val x = start.x + (end.x - start.x) * partialSegmentProgress
                        val y = start.y + (end.y - start.y) * partialSegmentProgress
                        if (fullSegments == 0) {
                            animatedPath.moveTo(start.x, start.y)
                        }
                        animatedPath.lineTo(x, y)
                    }

                    drawPath(
                        path = animatedPath,
                        color = OliverGreen,
                        style = Stroke(width = 4f)
                    )

                    // Vẽ điểm và nhãn giá trị
                    pathPoints.forEachIndexed { index, point ->
                        drawCircle(
                            color = Color(0xFF3F51B5),
                            radius = 6f,
                            center = point
                        )

                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                dataPoints[index].toString(),
                                point.x,
                                point.y - 12,
                                android.graphics.Paint().apply {
                                    textSize = 28f
                                    color = android.graphics.Color.BLACK
                                    textAlign = android.graphics.Paint.Align.CENTER
                                    isFakeBoldText = true
                                }
                            )
                        }
                    }

                    // Vẽ nhãn ngày ở trục hoành
                    days.forEachIndexed { index, day ->
                        val x = index * spacing
                        val y = h - bottomPadding + 20
                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                day,
                                x,
                                y,
                                android.graphics.Paint().apply {
                                    textSize = 28f
                                    color = android.graphics.Color.DKGRAY
                                    textAlign = android.graphics.Paint.Align.CENTER
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    val navController = rememberNavController()
   // DashboardScreen(navController = navController)
}

