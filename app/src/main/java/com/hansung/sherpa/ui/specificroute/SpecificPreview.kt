package com.hansung.sherpa.ui.specificroute

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.filament.Filament.init
import com.hansung.sherpa.compose.chart.Chart
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.transit.TmapTransitRouteResponse
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SpecificPreview(response: TransportRoute, totalTime:Int=0){


    Log.d("totaltime",totalTime.toString())

    val current = LocalDateTime.now()

    val futureTime = current.plusMinutes(totalTime.toLong())

    var futureHour = futureTime.hour
    var futureMin = futureTime.minute

    if(response==null){
        futureHour = 0
        futureMin = 0
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 24.dp, top = 16.dp, bottom = 16.dp, end = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = "${(totalTime)}", fontSize = 20.sp, fontWeight = FontWeight.Bold) // 전체 걸리는 시간
            Text(text = "분", fontWeight = FontWeight.Bold)
            Text(text = "${futureHour}시 ${futureMin}분", modifier = Modifier.padding(start = 16.dp), fontSize = 13.sp) // 전체 걸리는 시간을 통한 도착시간
            Text(text = "도착", fontSize = 13.sp)
        }
        Chart(transportRoute = response)
    }
}

@Preview(showBackground = true)
@Composable
fun pre(){
    val progress by remember { mutableStateOf(0.5f) }
    //SpecificPreview(progressPercentage = progress)
}
