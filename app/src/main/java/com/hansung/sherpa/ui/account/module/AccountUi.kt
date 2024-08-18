package com.hansung.sherpa.ui.account.module

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.sherpares.BmHanna

@Composable
fun AccountTextField(value:String, onValueChange: (String) -> Unit, modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.border(1.dp, Color.Black, RoundedCornerShape(10.dp))
    ){
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = modifier
                .wrapContentHeight()
                .padding(horizontal = 10.dp)
        )
    }
}


@Composable
fun AccountButton(buttonText:String, buttonColor: Color, modifier: Modifier, onClick: () -> Unit){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .background(buttonColor)
            .clickable { onClick() }
    ){
        Text(
            text = buttonText,
            fontSize = 10.sp,
            fontFamily = BmHanna
        )
    }
}