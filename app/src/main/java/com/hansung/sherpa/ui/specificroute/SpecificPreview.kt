package com.hansung.sherpa.ui.specificroute

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.ui.chart.Chart
import com.hansung.sherpa.itemsetting.TransportRoute
import java.time.LocalDateTime

/**
 * 경로의 전체적인 요약본
 *
 * 전체 이동 시간 적인 내용을 프로그래스바로 표현
 * 
 * (총 이동 시간, 지하철 몇분 이동, 도보 몇분 이동...)
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SpecificPreview(response: TransportRoute){
    val totalTime = response.info.totalTime
    val current = LocalDateTime.now()

    val futureTime = current.plusMinutes(totalTime.toLong()) // 도착 예정 시간

    val futureHour = futureTime.hour // 도착 예정 시
    val futureMin = futureTime.minute // 도착 예정 분

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 24.dp, top = 16.dp, bottom = 16.dp, end = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = "${(totalTime)}분", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp)) // 전체 걸리는 시간
            Text(text = "${futureHour}시 ${futureMin}분 도착", modifier = Modifier.padding(start = 16.dp, bottom = 8.dp), fontSize = 13.sp) // 전체 걸리는 시간을 통한 도착 예상 시간
        }
        Chart(transportRoute = response)// 프로그래스바
    }
}

@Preview(showBackground = true)
@Composable
fun pre(){
    val progress by remember { mutableStateOf(0.5f) }
    //SpecificPreview(progressPercentage = progress)
}
