package com.hansung.sherpa.navigation

import android.util.Log
import androidx.core.content.ContextCompat
import com.hansung.sherpa.MainActivity
import com.hansung.sherpa.R
import com.hansung.sherpa.convert.Convert
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.convert.PathType
import com.hansung.sherpa.deviation.RouteControl
import com.hansung.sherpa.deviation.Section
import com.hansung.sherpa.transit.TransitManager
import com.hansung.sherpa.transit.TransitRouteRequest
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Utmk
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PathOverlay
import kotlinx.coroutines.delay

class Navigation {
    private var startLatLng: LatLng = LatLng(0.0, 0.0)
    var endLatLng: LatLng = LatLng(0.0, 0.0)
    private var routeRequest: TransitRouteRequest = setRouteRequest(startLatLng, endLatLng)
    private var pathOverlayList:MutableList<PathOverlay> = mutableListOf()
    lateinit var naverMap: NaverMap
    lateinit var mainActivity: MainActivity
    lateinit var routeControl: RouteControl

    // 반드시 지울 것!! 좌표 찾기 대신 넣는 임시 값
    // [개발]: 시작, 도착 좌표
    //private val tempStartLatLng = LatLng(37.5004198786564, 127.126936754911) // 인천공항 버스 정류소(오금동)
    //val tempEndLatLng = LatLng(37.6134436427887, 126.926493082645) // 은평청여울수영장

    // 재호 경로
    private val tempStartLatLng = LatLng(37.6417, 126.8364)
    val tempEndLatLng = LatLng(37.6274, 126.829613)
    // 반드시 지울 것!!

    // 경로 탐색
    fun getTransitRoutesMJ(start: String, end: String): MutableList<MutableList<LegRoute>> {
        // 검색어 기반 좌표 검색
        /**
         * 미완성이라 주석처리
         * val SL = SearchLocation()
         * startLatLng = SL.searchLatLng(start)
         * endLatLng = SL.searchLatLng(end)
         **/

        // 좌표 기반 경로 검색
        routeRequest = setRouteRequest(tempStartLatLng, tempEndLatLng)
        val transitRouteResponse = TransitManager(mainActivity).getTransitRoutes2(routeRequest)
        val transitRoutes = Convert().convertToRouteMutableLists(transitRouteResponse)

        return transitRoutes
    }

    // 경로 탐색
    fun getTransitRoutes(start: String, end: String){
        // 검색어 기반 좌표 검색
        /**
         * 미완성이라 주석처리
         * val SL = SearchLocation()
         * startLatLng = SL.searchLatLng(start)
         * endLatLng = SL.searchLatLng(end)
        **/
        
        // 좌표 기반 경로 검색
        routeRequest = setRouteRequest(tempStartLatLng, tempEndLatLng)
        val transitRouteResponse = TransitManager(mainActivity).getTransitRoutes2(routeRequest)
        val transitRoutes = Convert().convertToRouteMutableLists(transitRouteResponse)
        val transitRoute = transitRoutes[0]
        
        // 경로 그리기
        drawRoute(transitRoute)

        // 기타
        routeControl.route = Convert().convertLegRouteToLatLng(transitRoute)
        routeControl.initializeRoute()
    }

    // 경로 요청 값 만들기
    private fun setRouteRequest(startLatLng: LatLng, endLatLng: LatLng):TransitRouteRequest {
        return TransitRouteRequest(
            startX = startLatLng.longitude.toString(),
            startY = startLatLng.latitude.toString(),
            endX = endLatLng.longitude.toString(),
            endY = endLatLng.latitude.toString(),
            lang = 0,
            format = "json",
            count = 1
        )
    }

    // 경로를 지우는 함수
    private fun clearRoute() {
        pathOverlayList.forEach { it.map = null }
        pathOverlayList = mutableListOf()
    }

    var count = 0;
    // 재탐색 후 경로를 그리는 함수
    fun redrawRoute(location:LatLng, endLatLng: LatLng) {
        // 경로 초기화
        clearRoute()

        // 좌표 기반 경로 검색
        routeRequest = setRouteRequest(location, endLatLng)

        // 요청 좌표 기반 경로 검색
        val transitRouteResponse = TransitManager(mainActivity).getTransitRoutes2(routeRequest)

        val transitRoutes = Convert().convertToRouteMutableLists(transitRouteResponse)

        val transitRoute = transitRoutes[0]

        // 경로 그리기
        drawRoute(transitRoute)

        // 기타
        routeControl.route = Convert().convertLegRouteToLatLng(transitRoute)
        routeControl.initializeRoute()
    }
    
    // 경로를 그리는 함수
    private fun drawRoute(transitRoute: MutableList<LegRoute>) {
        for (i in transitRoute){
            val pathOverlay = PathOverlay().also {
                it.coords = Convert().convertCoordinateToLatLng(i.coordinates)
                it.width = 10
                when(i.pathType){
                    PathType.WALK -> it.color = convertIntToStr(R.color.WALK)
                    PathType.BUS -> it.color = convertIntToStr(R.color.BUS)
                    PathType.EXPRESSBUS -> it.color = convertIntToStr(R.color.EXPRESSBUS)
                    PathType.SUBWAY -> it.color = convertIntToStr(R.color.SUBWAY)
                    PathType.TRAIN -> it.color = convertIntToStr(R.color.TRAIN)
                }
                it.map = naverMap
            }
            pathOverlayList.add(pathOverlay)
        }
    }
    
    // 색상 값 변환 HEXACODE(#ffffff) -> INT(@ColorInt)
    private fun convertIntToStr(color:Int) : Int {
        return ContextCompat.getColor(mainActivity, color)
    }
}

