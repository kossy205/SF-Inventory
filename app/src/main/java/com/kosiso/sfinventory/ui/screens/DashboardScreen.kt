package com.kosiso.sfinventory.ui.screens

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kosiso.sfinventory.R
import com.kosiso.sfinventory.data.model.Product
import com.kosiso.sfinventory.ui.theme.BackgroundColor
import com.kosiso.sfinventory.ui.theme.Black
import com.kosiso.sfinventory.ui.theme.Pink
import com.kosiso.sfinventory.ui.theme.White
import com.kosiso.sfinventory.ui.theme.onest
import com.kosiso.sfinventory.ui.viewmodel.MainViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import com.kosiso.sfinventory.ui.theme.LightPink
import com.kosiso.sfinventory.ui.theme.LighterPink
import com.kosiso.sfinventory.ui.theme.LightestPink
import com.kosiso.sfinventory.ui.uiModel.PieChartData

@Preview
@Composable
private fun Preview(){
    val sampleData = listOf(
        PieChartData(100f, Color.Red),
        PieChartData(80f, Color.Blue),
        PieChartData(70f, Color.Green),
        PieChartData(50f, Color.Yellow)
    )
    DonutPieChart(
        data = sampleData,
        itemCount = 300,
        modifier = Modifier.size(200.dp)
    )
}

@Composable
fun DashboardScreen(mainViewModel: MainViewModel){
    var productList = remember { mutableStateOf(emptyList<Product>()) }
    val getAllProducts = mainViewModel.getProductListFromLocaldb()
    val allProduct = mainViewModel.productsList.collectAsState().value.apply {
        onSuccess {
            productList.value = it
        }
        onFailure {
            Log.i("get all products list", "failed: ${it.message}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 15.dp)
            .padding(bottom = 65.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Dashboard",
            style = TextStyle(
                color = Black,
                fontFamily = onest,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 25.sp
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        DashboardReportSection(productList.value)

        Spacer(modifier = Modifier.height(15.dp))

        StockChartBox(productList.value)
    }
}

@Composable
private fun DashboardReportSection(productList: List<Product>){
    val totalStock = productList.map { it.quantity }.sum()
    val rawTotalPrice = productList.map { it.price }.sum()
    val totalCategory = productList.map { it.category }.distinct().count()
    val totalSupplier = productList.map { it.supplier }.distinct().count()
    val totalPrice = NumberFormat.getNumberInstance(Locale.US).format(rawTotalPrice)

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(top = 12.dp)
        ) {
            TotalStockBox(
                modifier = Modifier
                    .weight(1f),
                valueColor = Pink,
                valueText = totalStock,
                boxTitle = "Total Stock",
                icon = R.drawable.ic_dashboard,
                iconColor = Pink,
            )

            Spacer(modifier = Modifier.width(15.dp))

            SpecialTotalStockBox(
                modifier = Modifier
                    .weight(1f),
                valueColor = Pink,
                valueText = totalPrice,
                boxTitle = "Total Value",
                icon = R.drawable.ic_dollar,
                iconColor = Pink,
            )
        }

        Spacer(modifier = Modifier.width(15.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(top = 12.dp)
        ) {
            TotalStockBox(
                modifier = Modifier
                    .weight(1f),
                valueColor = Pink,
                valueText = totalCategory,
                boxTitle = "Total Category",
                icon = R.drawable.ic_arrange1,
                iconColor = Pink,
            )

            Spacer(modifier = Modifier.width(15.dp))

            TotalStockBox(
                modifier = Modifier
                    .weight(1f),
                valueColor = Pink,
                valueText = totalSupplier,
                boxTitle = "Total Supplier",
                icon = R.drawable.ic_chart,
                iconColor = Pink,
            )
        }
    }
}

@Composable
private fun TotalStockBox(
    modifier: Modifier,
    valueColor: Color,
    valueText: Int,
    boxTitle: String,
    icon: Int,
    iconColor: Color
) {

    var animationPlayed = remember { mutableStateOf(false) }
    val animatedCount = animateIntAsState(
        targetValue = if (animationPlayed.value) valueText else 0,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        ),
        label = "CountAnimation"
    )

    LaunchedEffect(Unit) {
        animationPlayed.value = true
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(White)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .fillMaxSize(),
                        painter = painterResource(icon),
                        contentDescription = "",
                        tint = iconColor
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "${animatedCount.value}",
                    style = TextStyle(
                        color = valueColor,
                        fontFamily = onest,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = boxTitle,
                    style = TextStyle(
                        color = Black,
                        fontFamily = onest,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun SpecialTotalStockBox(
    modifier: Modifier,
    valueColor: Color,
    valueText: String,
    boxTitle: String,
    icon: Int,
    iconColor: Color
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(White)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .fillMaxSize(),
                        painter = painterResource(icon),
                        contentDescription = "",
                        tint = iconColor
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))
                Row {
                    Text(
                        text = "₦",
                        style = TextStyle(
                            color = valueColor,
                            fontFamily = onest,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = "${valueText}",
                        style = TextStyle(
                            color = valueColor,
                            fontFamily = onest,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }


                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = boxTitle,
                    style = TextStyle(
                        color = Black,
                        fontFamily = onest,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

private fun floatPercentageValue(value: Int, totalProductItem: Int): Float{
    return if(value == 0) 0f else ((value.toFloat()/totalProductItem)*100)
}


@Composable
private fun StockChartBox(productList: List<Product>) {
    val totalProductItem = productList.count()

    //out of stock (quantity = 0)
    val numberOfProductOutOfStock = productList.count { it.quantity == 0 }
    val outOfStockPercentage = floatPercentageValue(
        numberOfProductOutOfStock,
        totalProductItem
    )
    Log.i("out of stock", "$numberOfProductOutOfStock, $outOfStockPercentage")


    //low stock (quantity = 1 .. 5)
    val numberOfProductLowStock = productList.count { it.quantity in 1..5 }
    val lowStockPercentage = floatPercentageValue(
        numberOfProductLowStock,
        totalProductItem
    )
    Log.i("low stock", "$numberOfProductLowStock, $lowStockPercentage")

    //near low stock (quantity = 6 .. 10)
    val numberOfProductNearLowStock = productList.count { it.quantity in 6..10}
    val nearLowStockPercentage = floatPercentageValue(
        numberOfProductNearLowStock,
        totalProductItem
    )
    Log.i("near low stock", "$numberOfProductNearLowStock, $nearLowStockPercentage")

    //in stock (quantity > 10)
    val numberOfProductInStock = productList.count { it.quantity > 10 }
    val inStockPercentage = floatPercentageValue(
        numberOfProductInStock,
        totalProductItem
    )
    Log.i("in stock", "$numberOfProductInStock, $inStockPercentage")

    val data = listOf(
        PieChartData(inStockPercentage, Pink),
        PieChartData(nearLowStockPercentage, LighterPink),
        PieChartData(lowStockPercentage, LightPink),
        PieChartData(outOfStockPercentage, LightestPink)
    )
    Log.i("stock chart", data.toString())

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(White)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Stock Level",
                style = TextStyle(
                    color = Black,
                    fontFamily = onest,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(150.dp)
            ){

                DonutPieChart(
                    data = data,
                    itemCount = totalProductItem,
                    modifier = Modifier.size(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                ){

                    Row(

                    ) {
                        Box(
                            modifier = Modifier
                                .background(Pink)
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "In Stock:",
                            style = TextStyle(
                                color = Black,
                                fontFamily = onest,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${inStockPercentage.toInt()}%",
                            style = TextStyle(
                                color = Black,
                                fontFamily = onest,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row {
                        Box(
                            modifier = Modifier
                                .background(LightPink)
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Low Stock:",
                            style = TextStyle(
                                color = Black,
                                fontFamily = onest,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${lowStockPercentage.toInt()}%",
                            style = TextStyle(
                                color = Black,
                                fontFamily = onest,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row {
                        Box(
                            modifier = Modifier
                                .background(LighterPink)
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Near Low Stock:",
                            style = TextStyle(
                                color = Black,
                                fontFamily = onest,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${nearLowStockPercentage.toInt()}%",
                            style = TextStyle(
                                color = Black,
                                fontFamily = onest,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row {
                        Box(
                            modifier = Modifier
                                .background(LightestPink)
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Out Stock:",
                            style = TextStyle(
                                color = Black,
                                fontFamily = onest,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${outOfStockPercentage.toInt()}%",
                            style = TextStyle(
                                color = Black,
                                fontFamily = onest,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }

                }
            }


        }

    }
}

@Composable
fun DonutPieChart(
    data: List<PieChartData>,
    itemCount: Int,
    modifier: Modifier = Modifier,
    subText: String = "Products",
    donutThickness: Float = 60f,
    gapDegrees: Float = 5f
) {
    val textMeasurer = rememberTextMeasurer()
    val totalValue = data.sumOf { it.value.toDouble() }.toFloat()
    val sweepAngle = (360f - (data.size * gapDegrees)) / totalValue

    var animationPlayed = remember { mutableStateOf(false) }
    val animationProgress = animateFloatAsState(
        targetValue = if (animationPlayed.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        ),
        label = "DonutAnimation"
    )

    val animatedCount = animateIntAsState(
        targetValue = if (animationPlayed.value) itemCount else 0,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        ),
        label = "CountAnimation"
    )

    LaunchedEffect(Unit) {
        animationPlayed.value = true
    }


    Canvas(modifier = modifier.size(160.dp)) {
        val radius = size.minDimension / 2
        val innerRadius = radius - donutThickness
        val rotationAngle = animationProgress.value * 35f
        var startAngle = rotationAngle
//        var startAngle = 0f

        // Draw each segment of the donut chart
        data.forEach { segment ->
            val angle = segment.value * sweepAngle
            drawArc(
                color = segment.color,
                startAngle = startAngle,
                sweepAngle = angle,
                useCenter = false,
                topLeft = Offset(
                    x = center.x - radius,
                    y = center.y - radius
                ),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = donutThickness)
            )
            startAngle += angle + gapDegrees
        }

        // Center texts
        val totalCountTextLayout = textMeasurer.measure(
            text = "${animatedCount.value}",
            style = TextStyle(
                color = Color.Black,
                fontFamily = onest,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        )

        // Subtext
        val subTextLayout = textMeasurer.measure(
            text = subText,
            style = TextStyle(
                color = Color.Black,
                fontFamily = onest,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        )

        // Draw total count
        drawText(
            textLayoutResult = totalCountTextLayout,
            topLeft = Offset(
                x = center.x - totalCountTextLayout.size.width / 2,
                y = center.y - totalCountTextLayout.size.height - -8.dp.toPx()
            )
        )

        // Draw subtext below total count
        drawText(
            textLayoutResult = subTextLayout,
            topLeft = Offset(
                x = center.x - subTextLayout.size.width / 2,
                y = center.y + 18.dp.toPx()
            )
        )
    }
}
