package com.hansung.sherpa.ui.specificroute

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hansung.sherpa.compose.chart.typeOfColor
import com.hansung.sherpa.itemsetting.TransportRoute
import com.naver.maps.map.compose.Align
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.PathOverlay

/**
 * NaverMap에 대중교통 경로를 그리는 Compose
 * ※ 무조건 NaverMap compose에서만 사용해야 한다.
 * 
 * @param transportRoute SearchScreen에서 가져온 대중교통 경로
 */
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun DrawPathOverlay(transportRoute: TransportRoute) {
    for(subPath in transportRoute.subPath){
        if(subPath.trafficType != 3) {
            PathOverlay(
                coords = subPath.sectionRoute.routeList,
                width = 5.dp,
                color = typeOfColor(subPath),
                outlineColor = Color.Transparent
            )
        }
        if(subPath.trafficType == 3) {
            PathOverlay(
                coords = subPath.sectionRoute.routeList,
                width = 5.dp,
                color = Color.Black,
                outlineColor = Color.Transparent
            )
        }
    }
}