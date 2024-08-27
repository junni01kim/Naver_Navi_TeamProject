package com.hansung.sherpa.sendInfo.receive

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.user.table.User1
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ColorPart

data class CommonResponse(
    var code: Int? = null,
    var message: String? = null,
    var `data`: String? = null
)

data class ReceiveRouteResponse(
    val coordParts:SnapshotStateList<MutableList<LatLng>>,
    val colorParts: MutableList<ColorPart>
)

data class ReceivePos(
    val pos: LatLng,
    val passedRoute:SnapshotStateList<Double>
)