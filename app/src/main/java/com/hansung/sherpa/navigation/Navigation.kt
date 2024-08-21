package com.hansung.sherpa.navigation

import android.util.Log
import androidx.core.content.ContextCompat
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.MainActivity
import com.hansung.sherpa.R
import com.hansung.sherpa.convert.Convert
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.convert.PathType
import com.hansung.sherpa.deviation.RouteControl
import com.hansung.sherpa.itemsetting.RouteDetailMapper
import com.hansung.sherpa.itemsetting.RouteFilterMapper
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.transit.ODsayTransitRouteRequest
import com.hansung.sherpa.transit.PedestrianRouteRequest
import com.hansung.sherpa.transit.TmapTransitRouteRequest
import com.hansung.sherpa.transit.TransitManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PathOverlay

class Navigation {
    private var startLatLng: LatLng = LatLng(0.0, 0.0)
    var endLatLng: LatLng = LatLng(0.0, 0.0)
    private var routeRequest: TmapTransitRouteRequest = setRouteRequest(startLatLng, endLatLng)
    private var pathOverlayList:MutableList<PathOverlay> = mutableListOf()
    lateinit var naverMap: NaverMap
    lateinit var mainActivity: MainActivity
    //lateinit var routeControl: RouteControl

    // 경로 요청 값 만들기
    private fun setRouteRequest(startLatLng: LatLng, endLatLng: LatLng):TmapTransitRouteRequest {
        return TmapTransitRouteRequest(
            startX = startLatLng.longitude.toString(),
            startY = startLatLng.latitude.toString(),
            endX = endLatLng.longitude.toString(),
            endY = endLatLng.latitude.toString(),
            lang = 0,
            format = "json",
            count = 10
        )
    }
}

