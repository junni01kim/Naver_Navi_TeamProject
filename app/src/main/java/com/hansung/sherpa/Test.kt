package com.hansung.sherpa

import com.hansung.sherpa.transit.PedestrianRouteRequest
import com.hansung.sherpa.transit.TransitManager
import com.naver.maps.geometry.LatLng

fun main(){
    val tempStartLatLng = LatLng(37.642743, 126.835375)
    val tempEndLatLng = LatLng(37.627444, 126.829600)

    val tm = TransitManager(StaticValue.mainActivity)

    var res = tm.getPedestrianRoute(
        PedestrianRouteRequest(startX = tempStartLatLng.latitude.toFloat(), startY = tempStartLatLng.longitude.toFloat(), endX = tempEndLatLng.latitude.toFloat(), endY =  tempEndLatLng.longitude.toFloat())
    )

    println(res)
}