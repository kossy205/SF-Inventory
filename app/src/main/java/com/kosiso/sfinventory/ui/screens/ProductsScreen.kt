package com.kosiso.sfinventory.ui.screens

import android.text.format.DateUtils
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.kosiso.sfinventory.data.model.Product
import com.kosiso.sfinventory.ui.theme.BackgroundColor
import com.kosiso.sfinventory.ui.theme.Black
import com.kosiso.sfinventory.ui.theme.White
import com.kosiso.sfinventory.ui.theme.onest
import com.kosiso.sfinventory.ui.viewmodel.MainViewModel
import java.sql.Timestamp
import com.kosiso.sfinventory.R
import com.kosiso.sfinventory.ui.theme.Green
import com.kosiso.sfinventory.ui.theme.Orange
import com.kosiso.sfinventory.ui.theme.Pink
import com.kosiso.sfinventory.ui.theme.Red
import com.kosiso.sfinventory.ui.theme.Yellow
import java.util.Date
import java.text.NumberFormat
import java.util.Locale

@Preview
@Composable
private fun Preview(){
    val product = Product(
        id = "0",
        name = "Pump guage",
        description = "",
        image = "",
        quantity = 5,
        price = 56700,
        supplier = "SSS Tech",
        category = "",
        lastUpdated = Timestamp(System.currentTimeMillis())
    )
    ProductItem(product)
}

@Composable
fun ProductsScreen(
    mainViewModel: MainViewModel,
    onNavigateToDetailsScreen: (Product) -> Unit,
    onNavigateToAddProductScreen: () -> Unit){

    val productList = mainViewModel.productsList.collectAsState().value
    Log.i("products list screen", productList.toString())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 15.dp)
            .padding(bottom = 65.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ){

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Products",
                style = TextStyle(
                    color = Black,
                    fontFamily = onest,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.sp
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            ProductListSection(mainViewModel, onNavigateToDetailsScreen)
        }

        FloatingActionButton(
            onClick = { onNavigateToAddProductScreen() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Pink,
            contentColor = White
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Product"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductListSection(mainViewModel: MainViewModel, onNavigateToDetailsScreen: (Product) -> Unit){

    val productList = mainViewModel.productsList.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(White)

    ){
        productList.apply {
            onSuccess {productList->
                if (productList.isEmpty()) {
                    Log.i("show product list 1", "$productList")
                    Text("No Products available",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center))
                } else {
                    Log.i("show product list 2", "$productList")
                    LazyColumn {
                        items(
                            items = productList,
                            key = { it.id }
                        ) { product->

                            SwipeToDelete(
                                onDelete = {
                                    mainViewModel.deleteProduct(product.id)
                                },
                                productItem = {
                                    // Product Item with clickable modifier
                                    Box(
                                        modifier = Modifier
                                            .clickable{
                                                onNavigateToDetailsScreen(product)
                                            }
                                    ) {
                                        ProductItem(product)
                                    }
                                }
                            )
                        }
                    }
                }
                Log.i("products list screen", productList.toString())
            }
            onFailure {
                Log.i("products list screen", it.toString())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDelete(
    onDelete:() -> Unit,
    productItem: @Composable () -> Unit
){
    val dismissState = rememberSwipeToDismissBoxState()

    if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
        LaunchedEffect(Unit) {
            onDelete()
            dismissState.reset()
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true
    ) {
        productItem()
    }
}

@Composable
private fun ProductItem(product: Product){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(White)
    ){
        Box(
            modifier = Modifier
                .weight(0.3f),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(65.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        Box(
            modifier = Modifier
                .weight(0.7f)
                .padding(end = 16.dp)
        ){
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Text(
                            text = if (product.name.length > 15) product.name.take(15) + "..." else product.name,
                            style = TextStyle(
                                color = Black,
                                fontFamily = onest,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "₦",
                                style = TextStyle(
                                    color = Black,
                                    fontFamily = onest,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            )
                            val formattedPrice = NumberFormat.getNumberInstance(Locale.US).format(product.price)
                            Text(
                                text = formattedPrice,
                                style = TextStyle(
                                    color = Black,
                                    fontFamily = onest,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 19.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Text(
                            text = formatTimeAgo(product.lastUpdated),
                            style = TextStyle(
                                color = Black.copy(alpha = 0.4f),
                                fontFamily = onest,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        )

                        if(product.quantity == 0){
                            // out of stock
                            Text(
                                text = "Stock: ${product.quantity}",
                                style = TextStyle(
                                    color = Red,
                                    fontFamily = onest,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            )
                        }
                        if(product.quantity <= 5 && product.quantity > 0){
                            // low stock
                            Text(
                                text = "Stock: ${product.quantity}",
                                style = TextStyle(
                                    color = Yellow,
                                    fontFamily = onest,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            )
                        }
                        if(product.quantity > 5 && product.quantity <= 10){
                            // near low stock
                            Text(
                                text = "Stock: ${product.quantity}",
                                style = TextStyle(
                                    color = Orange,
                                    fontFamily = onest,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            )
                        }
                        if(product.quantity > 10){
                            // in stock
                            Text(
                                text = "Stock: ${product.quantity}",
                                style = TextStyle(
                                    color = Green,
                                    fontFamily = onest,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatTimeAgo(timestamp: Timestamp): String {
    val now = Date()
    val createdAt = timestamp.time

    // Use DateUtils to format the duration
    return DateUtils.getRelativeTimeSpanString(
        createdAt,
        now.time,
        DateUtils.MINUTE_IN_MILLIS
    ).toString()
}