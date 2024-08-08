package com.hansung.sherpa.ui.preference

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScheduleBottomSheetHeader(
    onClosedButtonClick : () -> Unit
){
    val textStyle : TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(245,238,248))
            .clip(RoundedCornerShape(12.dp)),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 8.dp
                )
        ) {
            TextButton(
                modifier = Modifier
                    .align(Alignment.TopStart),
                onClick = { onClosedButtonClick() }
            ) {
                Text(
                    text = "취소",
                    color = Color(219, 0, 35),
                    style = textStyle
                )
            }
            Text(
                text = "새로운 일정",
                modifier = Modifier
                    .padding(top = (12).dp)
                    .align(Alignment.TopCenter),
                style = textStyle
            )
            TextButton(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                onClick = { onClosedButtonClick() }
            ) {
                Text(
                    text = "추가",
                    color = Color(24, 109, 234),
                    style = textStyle
                )
            }
        }
    }
}