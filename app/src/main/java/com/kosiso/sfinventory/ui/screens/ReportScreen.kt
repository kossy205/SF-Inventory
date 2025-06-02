package com.kosiso.sfinventory.ui.screens

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kosiso.sfinventory.R
import com.kosiso.sfinventory.data.model.Product
import com.kosiso.sfinventory.ui.theme.BackgroundColor
import com.kosiso.sfinventory.ui.theme.Black
import com.kosiso.sfinventory.ui.theme.Green
import com.kosiso.sfinventory.ui.theme.LightPink
import com.kosiso.sfinventory.ui.theme.LighterPink
import com.kosiso.sfinventory.ui.theme.LightestPink
import com.kosiso.sfinventory.ui.theme.Orange
import com.kosiso.sfinventory.ui.theme.Pink
import com.kosiso.sfinventory.ui.theme.Red
import com.kosiso.sfinventory.ui.theme.White
import com.kosiso.sfinventory.ui.theme.Yellow
import com.kosiso.sfinventory.ui.theme.onest
import com.kosiso.sfinventory.ui.uiModel.PieChartData
import com.kosiso.sfinventory.ui.viewmodel.MainViewModel
import java.text.NumberFormat
import java.util.Locale

@Preview
@Composable
private fun Preview(){




}

@Composable
fun ReportScreen(mainViewModel: MainViewModel, ){
    var productList = remember { mutableStateOf(emptyList<Product>()) }
    val getAllProducts = mainViewModel.getProductListFromLocaldb()
    val allProduct = mainViewModel.productsList.collectAsState().value.apply {
        onSuccess {
            productList.value = it
        }
        onFailure {
            Log.i("get all products list report", "failed: ${it.message}")
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
            text = "Report",
            style = TextStyle(
                color = Black,
                fontFamily = onest,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 25.sp
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        ReportSection(productList.value)

        Spacer(modifier = Modifier.height(15.dp))

        ReportChartBox(productList.value)
    }
}


@Composable
private fun ReportSection(productList: List<Product>){
    val totalStock = productList.map { it.quantity }.sum()
    val rawTotalPrice = productList.map { it.price }.sum()
    val totalCategory = productList.map { it.category }.distinct().count()
    val totalSupplier = productList.map { it.supplier }.distinct().count()
    val totalPrice = NumberFormat.getNumberInstance(Locale.US).format(rawTotalPrice)


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




    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            ReportBox(
                modifier = Modifier
                    .weight(1f),
                valueColor = Black,
                valueText = numberOfProductOutOfStock,
                boxTitle = "Out Of Stock",
                icon = R.drawable.ic_out_of_stock,
                iconColor = Red,
            )

            Spacer(modifier = Modifier.width(15.dp))

            ReportBox(
                modifier = Modifier
                    .weight(1f),
                valueColor = Black,
                valueText = numberOfProductLowStock,
                boxTitle = "Low Stock",
                icon = R.drawable.ic_low_stock,
                iconColor = Yellow,
            )
        }

        Spacer(modifier = Modifier.width(15.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            ReportBox(
                modifier = Modifier
                    .weight(1f),
                valueColor = Black,
                valueText = numberOfProductNearLowStock,
                boxTitle = "Near Low",
                icon = R.drawable.ic_near_low_stock,
                iconColor = Orange,
            )

            Spacer(modifier = Modifier.width(15.dp))

            ReportBox(
                modifier = Modifier
                    .weight(1f),
                valueColor = Black,
                valueText = numberOfProductInStock,
                boxTitle = "In Stock",
                icon = R.drawable.ic_dashboard,
                iconColor = Green,
            )
        }
    }
}

private fun floatPercentageValue(value: Int, totalProductItem: Int): Float{
    return if(value == 0) 0f else ((value.toFloat()/totalProductItem)*100)
}

@Composable
private fun ReportChartBox(productList: List<Product>) {
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

    val barChartData = listOf(
        PieChartData(inStockPercentage, Green),
        PieChartData(nearLowStockPercentage, Orange),
        PieChartData(lowStockPercentage, Yellow),
        PieChartData(outOfStockPercentage, Red)
    )
    Log.i("bar chart", barChartData.toString())

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
                text = "Stock Report",
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
            ){

                BarChart(
                    data = barChartData,
                    barWidth = 50.dp,
                    maxBarHeight = 200.dp
                )
            }
        }
    }
}

@Composable
private fun ReportBox(
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
            .clip(RoundedCornerShape(15.dp))
            .background(White)
            .padding(10.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .fillMaxSize(),
                painter = painterResource(icon),
                contentDescription = "",
                tint = iconColor
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = "${animatedCount.value}",
                    style = TextStyle(
                        color = valueColor,
                        fontFamily = onest,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = boxTitle,
                    style = TextStyle(
                        color = Black,
                        fontFamily = onest,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                )
            }

        }
    }
}


@Composable
fun BarChart(
    data: List<PieChartData>,
    modifier: Modifier = Modifier,
    barWidth: Dp = 40.dp,
    maxBarHeight: Dp = 200.dp,
    maxValue: Float = 100f,
    animationDuration: Int = 800
) {
    val textMeasurer = rememberTextMeasurer()

    // Animation
    var animationPlayed = remember { mutableStateOf(false) }
    val animationProgress = animateFloatAsState(
        targetValue = if (animationPlayed.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = LinearOutSlowInEasing
        ),
        label = "BarAnimation"
    )

    LaunchedEffect(Unit) {
        animationPlayed.value = true
    }

    Canvas(
        modifier = modifier.size(
            width = (barWidth * data.size) + (barWidth * (data.size - 1) * 0.5f),
            height = maxBarHeight + 60.dp
        )
    ) {
        val barSpacing = barWidth.toPx() / 2
        val maxHeightPx = maxBarHeight.toPx()
        val labels = listOf("In Stock", "Near Low", "Low Stock", "Out of Stock")

        // Calculate total width to center the bars
        val totalBarsWidth = (barWidth.toPx() * data.size) + (barSpacing * (data.size - 1))
        val startX = (size.width - totalBarsWidth) / 2

        // Draw bars
        data.forEachIndexed { index, segment ->
            val barHeight = (segment.value / maxValue) * maxHeightPx * animationProgress.value
            val leftOffset = startX + (index * (barWidth.toPx() + barSpacing))

            drawRect(
                color = segment.color,
                topLeft = Offset(
                    x = leftOffset,
                    y = maxHeightPx - barHeight
                ),
                size = Size(
                    width = barWidth.toPx(),
                    height = barHeight
                )
            )

            // Draw labels below bars
            val labelTextLayout = textMeasurer.measure(
                text = labels[index],
                style = TextStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            )

            drawText(
                textLayoutResult = labelTextLayout,
                topLeft = Offset(
                    x = leftOffset + (barWidth.toPx() - labelTextLayout.size.width) / 2,
                    y = maxHeightPx + 10.dp.toPx()
                )
            )

            // Draw value above bars
            val valueTextLayout = textMeasurer.measure(
                text = "${(segment.value * animationProgress.value).toInt()}",
                style = TextStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            )

            drawText(
                textLayoutResult = valueTextLayout,
                topLeft = Offset(
                    x = leftOffset + (barWidth.toPx() - valueTextLayout.size.width) / 2,
                    y = maxHeightPx - barHeight - valueTextLayout.size.height - 5.dp.toPx()
                )
            )
        }
    }
}