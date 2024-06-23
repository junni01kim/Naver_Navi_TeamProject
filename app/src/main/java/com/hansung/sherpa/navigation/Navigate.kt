package com.hansung.sherpa.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.deviation.RouteControl
import com.hansung.sherpa.deviation.StrengthLocation
import com.hansung.sherpa.gps.GPSDatas
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.PathOverlay

class Navigate(val pathOverlayList:MutableList<PathOverlay>, val routeControl: RouteControl) {

    @RequiresApi(Build.VERSION_CODES.R)
    val gpsDatas = GPSDatas(StaticValue.mainActivity)

    @RequiresApi(Build.VERSION_CODES.R)
    fun run(){
        redrawRoute()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun redrawRoute() {
    }
}