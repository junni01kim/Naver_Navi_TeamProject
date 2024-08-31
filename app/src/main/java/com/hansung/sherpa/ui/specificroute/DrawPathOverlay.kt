package com.hansung.sherpa.ui.specificroute

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hansung.sherpa.ui.chart.typeOfColor
import com.hansung.sherpa.itemsetting.TransportRoute
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ColorPart
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.overlay.OverlayImage

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
            width = 5.dp
        )
    }

}

fun setCoordParts(transportRoute: TransportRoute): SnapshotStateList<MutableList<LatLng>> {
    val coordParts = mutableStateListOf<MutableList<LatLng>>()

    transportRoute.subPath.forEachIndexed {
        index, subPath ->
        coordParts.add(subPath.sectionRoute.routeList)
        if(index != transportRoute.subPath.size - 1) {
            val next = transportRoute.subPath[index + 1].sectionRoute.routeList
            val curr = transportRoute.subPath[index].sectionRoute.routeList
            val list = listOf<LatLng>(curr[curr.size-1], next[0])
            coordParts.add(list.toMutableList())
        }
    }

    return coordParts
}

fun setColerParts(transportRoute: TransportRoute): MutableList<ColorPart> {
    val colorParts = mutableListOf<ColorPart>()

    transportRoute.subPath.forEachIndexed {
        index, subPath ->
        colorParts.add(ColorPart(
            color = if(subPath.trafficType == 3) Color.Gray else typeOfColor(subPath),
            outlineColor = Color.Transparent,
            passedColor = Color.Transparent,
            passedOutlineColor = Color.Transparent
        ))
        if(index != transportRoute.subPath.size - 1) {
            colorParts.add(ColorPart(
                color = Color.Gray,
                outlineColor = Color.Transparent,
                passedColor = Color.Transparent,
                passedOutlineColor = Color.Transparent
            ))
        }

    }

    return colorParts
}