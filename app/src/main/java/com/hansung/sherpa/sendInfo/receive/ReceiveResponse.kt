package com.hansung.sherpa.sendInfo.receive

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.hansung.sherpa.itemsetting.TransportRoute
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ColorPart

data class StartNavigationResponse(
    val transportRoute: TransportRoute,
    val coordParts: SnapshotStateList<MutableList<LatLng>>,
    val colorParts: MutableList<ColorPart>
)

data class ReceiveRouteResponse(
    val coordParts:SnapshotStateList<MutableList<LatLng>>,
    val colorParts: MutableList<ColorPart>
)

data class ReceivePos(
    val pos: LatLng,
    val passedRoute:SnapshotStateList<Double>
)