package com.kosiso.sfinventory.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kosiso.sfinventory.R
import com.kosiso.sfinventory.ui.theme.BackgroundColor
import com.kosiso.sfinventory.ui.theme.Black
import com.kosiso.sfinventory.ui.theme.Green
import com.kosiso.sfinventory.ui.theme.Pink
import com.kosiso.sfinventory.ui.theme.Red
import com.kosiso.sfinventory.ui.theme.White
import com.kosiso.sfinventory.ui.theme.Yellow
import com.kosiso.sfinventory.ui.theme.onest
import com.kosiso.sfinventory.ui.viewmodel.MainViewModel
import java.text.NumberFormat
import java.util.Locale

@Preview
@Composable
private fun Preview(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(White)

    ){
        Column(
            modifier = Modifier
                .padding(20.dp)

        ){
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Price
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
                val formattedPrice = NumberFormat.getNumberInstance(Locale.US).format(79000)
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

            Spacer(modifier = Modifier.height(10.dp))

            // name
            Text(
                text = "Pump Gauge",
                style = TextStyle(
                    color = Black,
                    fontFamily = onest,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // description
            Text(
                text = "Monitors fuel levels, volume, water level, and provides alarms for tanks. Web-enabled for remote monitoring and control.",
                style = TextStyle(
                    color = Black,
                    fontFamily = onest,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Quantity
            Text(
                text = "Stock: 33",
                style = TextStyle(
                    color = Red,
                    fontFamily = onest,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // supplier
            Text(
                text = "SmartFlow Technologies",
                style = TextStyle(
                    color = Black,
                    fontFamily = onest,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            )

//            if(product.quantity == 0){
//                // out of stock
//                Text(
//                    text = "Stock: ${product.quantity}",
//                    style = TextStyle(
//                        color = Red,
//                        fontFamily = onest,
//                        fontWeight = FontWeight.Medium,
//                        fontSize = 15.sp
//                    )
//                )
//            }
//            if(product.quantity <= 5 && product.quantity > 0){
//                // low stock
//                Text(
//                    text = "Stock: ${product.quantity}",
//                    style = TextStyle(
//                        color = Yellow,
//                        fontFamily = onest,
//                        fontWeight = FontWeight.Medium,
//                        fontSize = 15.sp
//                    )
//                )
//            }
//            if(product.quantity > 5){
//                // low stock
//                Text(
//                    text = "Stock: ${product.quantity}",
//                    style = TextStyle(
//                        color = Green,
//                        fontFamily = onest,
//                        fontWeight = FontWeight.Medium,
//                        fontSize = 15.sp
//                    )
//                )
//            }

        }
    }
}

@Composable
fun ProductDetailsScreen(
    productId: Int,
    mainViewModel: MainViewModel,
    onBackClick: () -> Unit,
    onNavigateToAddProductScreen: () -> Unit,
    onNavigateToUpdateProductScreen: () -> Unit){

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 15.dp)
            .padding(bottom = 20.dp)
    ){
        Column{

            Spacer(modifier = Modifier.height(20.dp))

            ProductDetailsSection(
                mainViewModel,
                productId
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Button(
                    onClick = {
                        Log.i("add product btn", "pressed")
                        onNavigateToAddProductScreen()
                    },
                    modifier = Modifier
                        .weight(0.4f)
                        .height(50.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        bottomStart = 12.dp,
                        topEnd = 12.dp,
                        bottomEnd = 12.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Pink
                    )
                ) {
                    Text(
                        text = "Add",
                        style = TextStyle(
                            color = White,
                            fontFamily = onest,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        Log.i("update product btn", "pressed")
                        onNavigateToUpdateProductScreen()
                    },
                    modifier = Modifier
                        .weight(0.4f)
                        .height(50.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        bottomStart = 12.dp,
                        topEnd = 12.dp,
                        bottomEnd = 12.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Pink
                    )
                ) {
                    Text(
                        text = "Edit",
                        style = TextStyle(
                            color = White,
                            fontFamily = onest,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }
            }

        }
    }
}

@Composable
private fun ProductDetailsSection(mainViewModel: MainViewModel, productId: Int){
    mainViewModel.getProductFromLocaldb(productId)
    val product = mainViewModel.product.collectAsState().value
    Log.i("product details", "product: $product")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(White)

    ){
        Column(
            modifier = Modifier
                .padding(20.dp)

        ){
            product.apply {
                onSuccess {product ->

                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    // Price
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

                    Spacer(modifier = Modifier.height(10.dp))

                    // name
                    Text(
                        text = "${product.name}",
                        style = TextStyle(
                            color = Black,
                            fontFamily = onest,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // description
                    Text(
                        text = "${product.description}",
                        style = TextStyle(
                            color = Black,
                            fontFamily = onest,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quantity
                    if(product.quantity == 0){
                        // out of stock
                        Text(
                            text = "Stock: ${product.quantity}",
                            style = TextStyle(
                                color = Red,
                                fontFamily = onest,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
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
                                fontSize = 15.sp
                            )
                        )
                    }
                    if(product.quantity > 5){
                        // low stock
                        Text(
                            text = "Stock: ${product.quantity}",
                            style = TextStyle(
                                color = Green,
                                fontFamily = onest,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // supplier
                    Text(
                        text = "${product.supplier}",
                        style = TextStyle(
                            color = Black,
                            fontFamily = onest,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    )

                }
                onFailure {
                    Log.i("product details", "failed: ${it.message}")
                }
            }

        }
    }
}