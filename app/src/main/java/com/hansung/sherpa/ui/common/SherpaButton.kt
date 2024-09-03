package com.hansung.sherpa.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SherpaButton1(dismissButtonText:String, onDismissRequest: () -> Unit) {
    Button(
        onClick = onDismissRequest,
        modifier = Modifier.fillMaxWidth(0.4f).height(45.dp),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonColors(backgroundCardColor, sherpaThemeColor, backgroundCardColor, sherpaThemeColor),
        border = BorderStroke(1.dp, sherpaThemeColor)
    ){
        Text(
            text = dismissButtonText,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SherpaButton2(confirmButtonText:String, onConfirmation: ()->Unit) {
    Button(
        onClick = onConfirmation,
        modifier = Modifier.fillMaxWidth(0.7f).height(45.dp),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonColors(sherpaThemeColor, backgroundCardColor, sherpaThemeColor, backgroundCardColor)
    ){
        Text(
            text = confirmButtonText,
            fontWeight = FontWeight.Bold
        )
    }
}