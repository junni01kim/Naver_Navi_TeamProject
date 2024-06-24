package com.hansung.sherpa.navigation


import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hansung.sherpa.R
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.convert.Convert
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.convert.PathType
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
    
    // 빈 값 초기화
    init {
        startLatLng = LatLng(0.0, 0.0)
        endLatLng =  LatLng(0.0, 0.0)
        routeRequest = setRouteRequest(startLatLng, endLatLng)
    }

    // 좌표 찾기 대신 넣는 임시 값
    val tempStartLatLng = LatLng(37.5004198786564, 127.126936754911)
    val tempEndLatLng = LatLng(37.6134436427887, 126.926493082645)

    fun getTransitRoutes(start: String, end: String) {
        // 검색어 기반 좌표 검색
        /**
         * 미완성이라 주석처리
         * val SL = SearchLocation()
         * startLatLng = SL.searchLatLng(start)
         * endLatLng = SL.searchLatLng(end)
        **/
        
        // 좌표 기반 경로 검색
        routeRequest = setRouteRequest()
        val transitRouteResponse = TransitManager(StaticValue.mainActivity).getTransitRoutes2(routeRequest)
        val transitRoutes = convert.convertToRouteMutableLists(transitRouteResponse)
        val transitRoute = transitRoutes[0]
        
        // 경로 기반 그리기
        drawRoute(transitRoute)
        
        // 기타
        StaticValue.routeControl.route = convert.convertLegRouteToLatLng(transitRoute)
    }

    // 경로 요청 값 만들기
    private fun setRouteRequest(startLatLng: LatLng = tempStartLatLng, endLatLng: LatLng = tempEndLatLng):TransitRouteRequest {
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

    // 경로를 그리는 함수
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

    // 경로를 지우는 함수
    private fun clearRoute() {
        pathOverlayList.forEach { it.map = null }
        pathOverlayList = mutableListOf<PathOverlay>()
    }

    fun redrawRoute(routeRequest: TransitRouteRequest) {
        // 경로 초기화
        clearRoute()

        // 요청 좌표 기반 경로 검색
        val transitRouteResponse = TransitManager(StaticValue.mainActivity).getTransitRoutes2(routeRequest)
        val transitRoutes = convert.convertToRouteMutableLists(transitRouteResponse)
        val transitRoute = transitRoutes[0]

        // 경로 기반 그리기
        drawRoute(transitRoute)

        // 기타
        StaticValue.routeControl.route = convert.convertLegRouteToLatLng(transitRoute)
    }
}

fun convertIntToStr(color:Int) : Int {
    return ContextCompat.getColor(StaticValue.mainActivity, color)
}