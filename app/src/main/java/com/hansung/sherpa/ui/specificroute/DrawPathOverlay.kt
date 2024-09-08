package com.hansung.sherpa.ui.specificroute

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hansung.sherpa.R
import com.hansung.sherpa.ui.chart.typeOfColor
import com.hansung.sherpa.itemsetting.TransportRoute
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ColorPart
import com.naver.maps.map.compose.PathOverlay

/**
 * NaverMap에 대중교통 경로를 그리는 Compose
 * ※ 무조건 NaverMap compose에서만 사용해야 한다.
 *
 * @param transportRoute SearchScreen에서 가져온 대중교통 경로
 */
@Composable
fun DrawPathOverlay(
    coordParts: SnapshotStateList<MutableList<LatLng>>,
    colorParts: MutableList<ColorPart>,
    passedRoute: SnapshotStateList<Double>
) {
    coordParts.forEachIndexed { index, coords ->
        PathOverlay(
            coords = coords,
            progress = passedRoute[index],
            color = colorParts[index].color,
            outlineColor = colorParts[index].outlineColor,
            passedColor = colorParts[index].passedColor,
            passedOutlineColor = colorParts[index].passedOutlineColor,
            width = 5.dp,
            patternInterval = if(colorParts[index].color == Color.Transparent) 15.dp else 0.dp,
            patternImage = if(colorParts[index].color == Color.Transparent) OverlayImage.fromResource(R.drawable.pedestrian_dash) else null
        )
    }
}

fun setCoordParts(transportRoute: TransportRoute): SnapshotStateList<MutableList<LatLng>> {
    val coordParts = mutableStateListOf<MutableList<LatLng>>()

    for(subPath in transportRoute.subPath) coordParts.add(subPath.sectionRoute.routeList)

    return coordParts
}

fun setColorParts(transportRoute: TransportRoute): MutableList<ColorPart> {
    val colorParts = mutableListOf<ColorPart>()

    for(subPath in transportRoute.subPath) {
        colorParts.add(ColorPart(
            color = if(subPath.trafficType == 3) Color.Transparent else typeOfColor(subPath),
            outlineColor = Color.Transparent,
            passedColor = Color.Transparent,
            passedOutlineColor = Color.Transparent
        ))
    }

    return colorParts
}