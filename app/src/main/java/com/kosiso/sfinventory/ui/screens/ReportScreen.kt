package com.kosiso.sfinventory.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun ReportScreen(mainViewModel: MainViewModel, ){
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
                text = "Report Screen",
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