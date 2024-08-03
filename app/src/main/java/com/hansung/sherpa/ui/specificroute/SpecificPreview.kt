package com.hansung.sherpa.ui.specificroute

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @param progressPercentage 경로의 비율 (도보 : 0.2f, 버스 : 0.6f, 도보 : 0.2f )
 */

@Composable
fun SpecificPreview(progressPercentage:Float){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 24.dp, top = 16.dp, bottom = 16.dp, end = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = "12", fontSize = 20.sp, fontWeight = FontWeight.Bold) // 전체 걸리는 시간
            Text(text = "분", fontWeight = FontWeight.Bold)
            Text(text = "8시 30분", modifier = Modifier.padding(start = 16.dp), fontSize = 13.sp) // 전체 걸리는 시간을 통한 도착시간
            Text(text = "도착", fontSize = 13.sp)
        }
        LinearProgressIndicator(
            progress = progressPercentage,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .fillMaxWidth(),
            trackColor = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun pre(){
    val progress by remember { mutableStateOf(0.5f) }
    SpecificPreview(progressPercentage = progress)
}
