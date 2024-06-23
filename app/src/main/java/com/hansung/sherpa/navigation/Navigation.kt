package com.hansung.sherpa.navigation


import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hansung.sherpa.R
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.convert.Convert
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.convert.PathType
import com.hansung.sherpa.deviation.RouteControl
import com.hansung.sherpa.location.SearchLocation
import com.hansung.sherpa.transit.TransitManager
import com.hansung.sherpa.transit.TransitRouteRequest
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.PathOverlay

class SearchRouteViewModel:ViewModel() {
    val destinationText = MutableLiveData<String>()
}
class Navigation {
    private val convert = Convert()
    private var startLatLng: LatLng
    private var endLatLng: LatLng
    private var routeRequest: TransitRouteRequest
    private var pathOverlayList:MutableList<PathOverlay> = mutableListOf()
    init {
        startLatLng = LatLng(0.0, 0.0)
        endLatLng =  LatLng(0.0, 0.0)
        routeRequest = getRouteRequest(startLatLng, endLatLng)
    }

    @RequiresApi(VERSION_CODES.R)
    fun getTransitRoutes(start: String, end: String) {
        // 검색어 기반 좌표 검색
        val SL = SearchLocation()
        //startLatLng = SL.searchLatLng(start)
        //endLatLng = SL.searchLatLng(end)

        // 좌표 기반 경로 검색
        routeRequest = getRouteRequest()
        val transitRouteResponse = TransitManager(StaticValue.mainActivity).getTransitRoutes2(routeRequest)
        val transitRoutes = convert.convertToRouteMutableLists(transitRouteResponse)
        val transitRoute = transitRoutes[0]
        
        // 경로 기반 그리기
        drawRoute(transitRoute)
        
        // 기타
        StaticValue.routeControl = RouteControl(this)
        StaticValue.routeControl.route = convert.convertLegRouteToLatLng(transitRoute)
    }

    fun getRouteRequest(startLatLng: LatLng = LatLng(37.6134436427887, 126.926493082645)
                        , endLatLng: LatLng = LatLng(37.5004198786564, 127.126936754911)):TransitRouteRequest {
        Log.d("getRouteRequest", "startLatLng: $startLatLng")
        Log.d("getRouteRequest", "endLatLng: $endLatLng")

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

    // 1. 사용자가 가고 싶은 출발지 목적지의 전체 경로를 그리는 함수
    // 반환 값은 사용자가 확정한 경로이다.
    private fun drawRoute(transitRoute: MutableList<LegRoute>) : MutableList<PathOverlay> {
        for (i in transitRoute){
            // pathOverlay는 네이버에서 제공하는 선 그리기 함수이며, 거기에 각 속성을 더 추가하여 색상을 칠했다.
            val pathOverlay = PathOverlay().also {
                it.coords = convert.convertCoordinateToLatLng(i.coordinates)
                it.width = 10
                when(i.pathType){
                    PathType.WALK -> it.color = convertIntToStr(R.color.WALK)
                    PathType.BUS -> it.color = convertIntToStr(R.color.BUS)
                    PathType.EXPRESSBUS -> it.color = convertIntToStr(R.color.EXPRESSBUS)
                    PathType.SUBWAY -> it.color = convertIntToStr(R.color.SUBWAY)
                    PathType.TRAIN -> it.color = convertIntToStr(R.color.TRAIN)
                }
                it.map = StaticValue.naverMap
            }
            pathOverlayList.add(pathOverlay)
        }
        return pathOverlayList
    }

    private fun clearRoute(pathOverlayList: MutableList<PathOverlay>) {
        pathOverlayList.forEach { it.map = null }
    }

    @RequiresApi(VERSION_CODES.R)
    fun redrawRoute(routeRequest: TransitRouteRequest) {
        // 경로 초기화
        clearRoute(pathOverlayList)

        // 요청 좌표 기반 경로 검색
        val transitRouteResponse = TransitManager(StaticValue.mainActivity).getTransitRoutes2(routeRequest)
        val transitRoutes = convert.convertToRouteMutableLists(transitRouteResponse)
        val transitRoute = transitRoutes[0]

        // 경로 기반 그리기
        drawRoute(transitRoute)
        // 기타
        StaticValue.routeControl = RouteControl(this)
        StaticValue.routeControl.route = convert.convertLegRouteToLatLng(transitRoute)
    }
}

fun convertIntToStr(color:Int) : Int {
    return ContextCompat.getColor(StaticValue.mainActivity, color)
}