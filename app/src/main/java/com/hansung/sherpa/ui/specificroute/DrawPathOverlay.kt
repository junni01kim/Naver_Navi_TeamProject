package com.hansung.sherpa.ui.specificroute

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.compose.chart.typeOfColor
import com.hansung.sherpa.itemsetting.TransportRoute
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ColorPart
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MultipartPathOverlay
import com.naver.maps.map.compose.PathOverlay

/**
 * NaverMap에 대중교통 경로를 그리는 Compose
 * ※ 무조건 NaverMap compose에서만 사용해야 한다.
 * 
 * @param transportRoute SearchScreen에서 가져온 대중교통 경로
 */
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun DrawPathOverlay(coordParts:MutableState<MutableList<MutableList<LatLng>>>, colorParts: MutableState<MutableList<ColorPart>>) {
    MultipartPathOverlay(
        coordParts = coordParts.value,
        colorParts = colorParts.value,
        width = 5.dp,
    )
}

fun setCoordParts(transportRoute: TransportRoute): MutableList<MutableList<LatLng>> {
    val coordParts = mutableListOf<MutableList<LatLng>>()

    for(subPath in transportRoute.subPath) coordParts.add(subPath.sectionRoute.routeList)

    return coordParts
}

fun setColerParts(transportRoute: TransportRoute): MutableList<ColorPart> {
    val colorParts = mutableListOf<ColorPart>()

    for(subPath in transportRoute.subPath) {
        colorParts.add(ColorPart(
            color = typeOfColor(subPath),
            outlineColor = Color.Transparent,
            passedColor = Color.Transparent,
            passedOutlineColor = Color.Transparent
        ))
    }

    return colorParts
}