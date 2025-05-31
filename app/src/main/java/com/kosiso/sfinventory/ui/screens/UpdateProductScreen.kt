package com.kosiso.sfinventory.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kosiso.sfinventory.ui.theme.BackgroundColor
import com.kosiso.sfinventory.ui.theme.Black
import com.kosiso.sfinventory.ui.theme.onest
import com.kosiso.sfinventory.ui.viewmodel.MainViewModel


@Composable
fun UpdateProductScreen(mainViewModel: MainViewModel, onBackClick: () -> Unit){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 15.dp)
            .padding(bottom = 65.dp)
    ){
        Column{

            Text(
                text = "Update Screen",
                style = TextStyle(
                    color = Black,
                    fontFamily = onest,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 35.sp
                )
            )
        }
    }
}