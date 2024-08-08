package com.hansung.sherpa.ui.specificroute

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hansung.sherpa.compose.chart.typeOfColor
import com.hansung.sherpa.itemsetting.TransportRoute
import com.naver.maps.map.compose.PathOverlay

// 경로를 그리는 함수
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
    }
}