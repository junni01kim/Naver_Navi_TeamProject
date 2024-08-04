package com.hansung.sherpa.compose.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.itemsetting.SubPath
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.searchscreen.typeOfColor

/**
 * 대중교통의 정보를 요약해서 표현하기 위한 차트이다.
 *
 * @param routeList 차트를 작성할 경로 정보
 * @param fullTime 전체 경로 소요시간 기반으로 비율 측정
 */
@Composable
fun Chart(routeList:List<SubPath>, fullTime:Int) {
    // 차트의 너비
    val width = 400.dp
    
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
        ) {
            routeList.forEachIndexed { index, it ->
                Box(modifier = Modifier
                    .width((width.value * it.sectionInfo.sectionTime!! / fullTime).dp)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(typeOfColor(it)),
                    contentAlignment = Alignment.Center) {
                    Text(
                        text = "${it.sectionInfo.sectionTime}분",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * 대중교통의 정보를 요약해서 표현하기 위한 차트이다.
 * 전체 경로 소요시간 기반으로 비율을 측정한다.
 *
 * @param transportRoute 차트를 작성할 전체 경로 정보
 */
@Composable
fun Chart(transportRoute: TransportRoute) {
    // 차트 너비
    val width = 400.dp

    val routeList = transportRoute.subPath
    val fullTime = transportRoute.info.totalTime

    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
        ) {
            routeList.forEachIndexed { index, it ->
                Box(modifier = Modifier
                    .width((width.value * it.sectionInfo.sectionTime!! / fullTime).dp)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(typeOfColor(it)),
                    contentAlignment = Alignment.Center) {
                    Text(
                        text = "${it.sectionInfo.sectionTime}분",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
