package com.hansung.sherpa.ui.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import com.hansung.sherpa.itemsetting.BusLane
import com.hansung.sherpa.itemsetting.BusSectionInfo
import com.hansung.sherpa.itemsetting.SubPath
import com.hansung.sherpa.itemsetting.SubwayLane
import com.hansung.sherpa.itemsetting.SubwaySectionInfo
import com.hansung.sherpa.itemsetting.TransportRoute

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

/**
 * 대중교통의 정보를 요약해서 표현하기 위한 차트이다.
 * 전체 경로 소요시간 기반으로 비율을 측정한다.
 *
 * @param transportRoute 차트를 작성할 전체 경로 정보
 */
val searchScreenBackgroundColor = Color(0xFFF2F3F4)
val textColor = Color(0xFF424242)
@Composable
fun ThickChart(transportRoute: TransportRoute) {
    // 차트 너비
    val width = 350.dp

    val routeList = transportRoute.subPath
    val fullTime = transportRoute.info.totalTime

    Row(modifier = Modifier
        .size(width, 20.dp)
        .clip(CircleShape)
        .background(searchScreenBackgroundColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        routeList.forEach {
            val boxWidth = (width.value * it.sectionInfo.sectionTime!! / fullTime).dp
            Box(modifier = Modifier
                .width(boxWidth)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(typeOfColor(it)),
                contentAlignment = Alignment.Center) {
                if(boxWidth >= 20.dp){
                    Text(
                        text = "${it.sectionInfo.sectionTime}분",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Center),
                        color = if (it.trafficType == 3) Color(0xFF8B8B8B) else Color.White,
                        lineHeight = 10.sp,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

/**
 * 대중교통 종류에 맞는 색상을 반환한다.
 *
 * @param subPath 대중교통
 * // TODO: 준희 테마색으로 변경
 */
fun typeOfColor(subPath: SubPath):Color {
    var color:Color? = null
    when(subPath.trafficType){
        // 지하철
        1 -> {
            val subway = subPath.sectionInfo as SubwaySectionInfo
            val subwayLane = subway.lane[0] as SubwayLane
            color = when(subwayLane.subwayCode){
                // 1호선
                1 -> Color(0xFF0052A4)
                // 2호선
                2 -> Color(0xFF00A84D)
                // 3호선
                3 -> Color(0xFFEF7C1C)
                // 4호선
                4 -> Color(0xFF00A5DE)
                // 5호선
                5 -> Color(0xFF996CAC)
                // 6호선
                6 -> Color(0xFFCD7C2F)
                // 7호선
                7 -> Color(0xFF747F00)
                // 8호선
                8 -> Color(0xFFE6186C)
                else -> Color.DarkGray
            }
        }

        // 버스 <- 지역마다 색상이 달라서, 경기 서울 기준으로 색 부여
        // https://librewiki.net/wiki/%ED%8B%80:%EB%B2%84%EC%8A%A4_%EB%85%B8%EC%84%A0%EC%83%89
        2 -> {
            val bus = subPath.sectionInfo as BusSectionInfo
            val busLane = bus.lane[0] as BusLane
            color = when(busLane.type){
                // 일반 (경기 시내일반: 일반)
                1 -> Color(0xFF33CC99)
                // 좌석 (경기 일반좌석: 좌석)
                2 -> Color(0xFF0068b7)
                // 마을
                3 -> Color(0xFF53b332)
                // 직행 좌석
                4 -> Color(0xFFe60012)
                // 공항 버스 (시외버스 공항)
                5 -> Color(0x00a0e9)
                // 간선 급행 (경기 간선급행)
                6 -> Color(0xFFe60012)
                // 외곽 (대전 외곽)
                10 -> Color(0xFF53b332)
                // 간선
                11 -> Color(0xFF0068b7)
                // 지선
                12 -> Color(0xFF53b332)
                // 순환
                13 -> Color(0xFFf2b70a)
                // 광역
                14 -> Color(0xFFe60012)
                // 급행 (부산 급행 ※ 급행은 모두 색상이 다양해서 부산으로 함)
                15 -> Color(0xFFff3300)
                // 관광 버스 (색상 존재 X)
                16 -> Color.DarkGray
                // 농어촌 버스 (색상 존재 X)
                20 -> Color.DarkGray
                // 경기도 시외형버스
                22 -> Color.DarkGray
                // 급행 간선 (인천 급행 간선)
                26 -> Color(0xFF5112ab)
                else -> Color.DarkGray
            }
        }
        // 도보
        3 -> color = searchScreenBackgroundColor
    }
    return color!!
}