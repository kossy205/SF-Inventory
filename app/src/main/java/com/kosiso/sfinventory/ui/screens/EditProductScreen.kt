package com.kosiso.sfinventory.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kosiso.sfinventory.data.model.Product
import com.kosiso.sfinventory.ui.theme.BackgroundColor
import com.kosiso.sfinventory.ui.theme.Black
import com.kosiso.sfinventory.ui.theme.Pink
import com.kosiso.sfinventory.ui.theme.White
import com.kosiso.sfinventory.ui.theme.onest
import com.kosiso.sfinventory.ui.viewmodel.MainViewModel
import java.util.UUID
import kotlin.text.isBlank
import kotlin.text.isNotBlank
import kotlin.text.toIntOrNull
import kotlin.text.toLongOrNull

@Preview(backgroundColor = 0xFFFFFFFF)
@Composable
private fun Preview(){

    Text(
        text = "Name",
        style = TextStyle(
            color = Black.copy(alpha = 0.4f),
            fontFamily = onest,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        )
    )
    OutlinedTextField(
        value = "Text value",
        onValueChange = {  },
        placeholder = {
            Text(
                text = "this is placeholder",
                style = TextStyle(
                    color = Black.copy(alpha = 0.4f),
                    fontFamily = onest,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                )
            )
        },
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 15.sp,
            fontFamily = onest,
            fontWeight = FontWeight.Normal
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Black.copy(alpha = 0.2f),
            focusedBorderColor = Pink,
        ),
        shape = RoundedCornerShape(12.dp)
    )
}



@Composable
fun EditProductScreen(
    productId: String,
    mainViewModel: MainViewModel,
    onBackClick: () -> Unit){

    var updatedProduct by remember { mutableStateOf<Product?>(null) }
    val context = LocalContext.current

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

            EditProductDetailsSection(
                mainViewModel,
                productId,
                updateProduct = {
                    updatedProduct = it
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween){
                Button(
                    onClick = {
                        Log.i("cancel edit product btn", "pressed")
                        onBackClick()
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
                        containerColor = Black
                    )
                ) {
                    Text(
                        text = "Cancel",
                        style = TextStyle(
                            color = White,
                            fontFamily = onest,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = {
                        Log.i("save product btn", "pressed")
                        updatedProduct?.let {updatedProduct->
                            validateForm(
                                updatedProduct = updatedProduct,
                                onValid = {
                                    mainViewModel.updateProduct(updatedProduct)
                                    onBackClick()
                                },
                                context = context
                            )
                        }
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
                        text = "Save",
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
private fun EditProductDetailsSection(
    mainViewModel: MainViewModel,
    productId: String,
    updateProduct:(Product) -> Unit,
){

    mainViewModel.getProductFromLocaldb(productId)
    val product = mainViewModel.product.collectAsState().value
    Log.i("edit product details", "product: $product")


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(White)

    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState())

        ) {

            product.apply {
                onSuccess {product ->

                    var productName by remember { mutableStateOf(product.name) }
                    MyTextField(
                        fieldTitle = "Name",
                        textInput = productName,
                        onTextInputChange = { productName = it },
                        placeholder = product.name
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    var productDescription by remember { mutableStateOf(product.description) }
                    MyTextField(
                        fieldTitle = "Description",
                        textInput = productDescription,
                        onTextInputChange = { productDescription = it },
                        placeholder = product.description
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    var productQuantity by remember { mutableStateOf(product.quantity.toString()) }
                    MyNumberField(
                        fieldTitle = "Quantity",
                        textInput = productQuantity,
                        onTextInputChange = {
                            productQuantity = it
                        },
                        placeholder = product.quantity.toString()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    var productPrice by remember { mutableStateOf(product.price.toString()) }
                    MyNumberField(
                        fieldTitle = "Price",
                        textInput = productPrice,
                        onTextInputChange = {
                            productPrice = it
                        },
                        placeholder = product.price.toString()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    var productCategory by remember { mutableStateOf(product.category) }
                    MyTextField(
                        fieldTitle = "Category",
                        textInput = productCategory,
                        onTextInputChange = { productCategory = it },
                        placeholder = product.category
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    var productSupplier by remember { mutableStateOf(product.supplier) }
                    MyTextField(
                        fieldTitle = "Supplier Info",
                        textInput = productSupplier,
                        onTextInputChange = { productSupplier = it },
                        placeholder = product.supplier
                    )

                    LaunchedEffect(
                        productName,
                        productDescription,
                        productQuantity,
                        productPrice,
                        productCategory,
                        productSupplier) {

                        val quantityInt = productQuantity.toString().toIntOrNull() ?: product.quantity
                        val priceLong = productPrice.toString().toLongOrNull() ?: product.price

                        val updatedProduct = Product(
                            id = product.id,
                            name = productName,
                            description = productDescription,
                            image = product.image,
                            quantity = quantityInt,
                            price = priceLong,
                            category = productCategory,
                            supplier = productSupplier
                        )
                        updateProduct(updatedProduct)
                    }

                }
                onFailure {
                    Log.i("edit product details", "failed: ${it.message}")
                }
            }

        }
    }
}

@Composable
private fun MyNumberField(
    fieldTitle: String,
    textInput: String,
    onTextInputChange: (String) -> Unit,
    placeholder: String
){
    Text(
        text = fieldTitle,
        style = TextStyle(
            color = Black.copy(alpha = 0.4f),
            fontFamily = onest,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        )
    )
    OutlinedTextField(
        value = textInput,
//        onValueChange = onTextInputChange,
        onValueChange = { newValue ->
            // Filter to allow only digits
            val filteredValue = newValue.filter { it.isDigit() }
            onTextInputChange(filteredValue)
        },
        placeholder = {
            Text(
                text = placeholder,
                style = TextStyle(
                    color = Black.copy(alpha = 0.4f),
                    fontFamily = onest,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                )
            )
        },
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 15.sp,
            fontFamily = onest,
            fontWeight = FontWeight.Normal
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Black.copy(alpha = 0.2f),
            focusedBorderColor = Pink,
        ),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )
}


private fun validateForm(updatedProduct: Product, onValid:()-> Unit, context: Context){
    updatedProduct?.let { product ->
        // Validate fields
        when {
            product.name.isBlank() -> {
                Toast.makeText(context, "Product name cannot be empty", Toast.LENGTH_SHORT).show()
            }
            product.quantity < 0 -> {
                Toast.makeText(context, "Quantity cannot be negative", Toast.LENGTH_SHORT).show()
            }
            product.price <= 0 -> {
                Toast.makeText(context, "Price must be greater than 0", Toast.LENGTH_SHORT).show()
            }
            product.category.isBlank() -> {
                Toast.makeText(context, "Category cannot be empty", Toast.LENGTH_SHORT).show()
            }
            product.supplier.isBlank() -> {
                Toast.makeText(context, "Supplier cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else -> {
                onValid()
            }
        }
    } ?: run {
        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
    }
}

@Composable
private fun MyTextField(
    fieldTitle: String,
    textInput: String,
    onTextInputChange: (String) -> Unit,
    placeholder: String
){
    Text(
        text = fieldTitle,
        style = TextStyle(
            color = Black.copy(alpha = 0.4f),
            fontFamily = onest,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        )
    )
    OutlinedTextField(
        value = textInput,
        onValueChange = onTextInputChange,
        placeholder = {
            Text(
                text = placeholder,
                style = TextStyle(
                    color = Black.copy(alpha = 0.4f),
                    fontFamily = onest,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                )
            )
        },
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 15.sp,
            fontFamily = onest,
            fontWeight = FontWeight.Normal
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Black.copy(alpha = 0.2f),
            focusedBorderColor = Pink,
        ),
        shape = RoundedCornerShape(12.dp)
    )
}