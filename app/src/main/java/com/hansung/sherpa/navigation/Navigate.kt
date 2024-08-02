package com.hansung.sherpa.navigation

import com.hansung.sherpa.deviation.RouteControl
import com.naver.maps.map.overlay.PathOverlay

class Navigate(val pathOverlayList:MutableList<PathOverlay>, val routeControl: RouteControl) {
    fun run(){
        redrawRoute()
    }
    fun redrawRoute() {
    }
}