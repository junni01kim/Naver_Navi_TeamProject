package com.hansung.sherpa.navigation

import android.graphics.Color
import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hansung.sherpa.R
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.convert.Convert
import com.hansung.sherpa.convert.PathType
import com.hansung.sherpa.deviation.RouteControl
import com.hansung.sherpa.transit.TransitManager
import com.hansung.sherpa.transit.TransitRouteRequest
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.PathOverlay

// 텍스트뷰에 값이 그래도 유지되고 있게하기 위한 뷰모델이다.
// searchRouteViewModel의 이름과 위치는 바꿀 필요가 있을듯
class SearchRouteViewModel:ViewModel() {
    val destinationText = MutableLiveData<String>()
}

// 내비게이션 클래스는 내비게이션 기능을 전반을 가지고 있다.

// 내비게이션 클래스는 다음과 같은 하위 기능을 가진다.
// 1. 사용자가 가고 싶은 출발지 목적지의 전체 경로를 그리는 함수
// 2. 원하는 경로를 사용자 위치를 기반으로 안내하는 클래스
class Navigation {
    private lateinit var routeControl:RouteControl

    // 출발지(내 위치)
    private lateinit var departureLatLng:LatLng

    // 목적지는 값이 없을 수도 있다.
    private var pathOverlayList:MutableList<PathOverlay> = mutableListOf()

    // 경로와 안내는 순서대로 진행된다.
    fun process() {
        // 0. 사용자가 가고 싶은 출발지 목적지를 구하는 클래스
        //TODO("장소 찾기 SearchLocation 클래스 활용(출발지)")
        //TODO("장소 찾기 SearchLocation 클래스 활용(목적지)")

        val routeRequest = getRouteRequest()

        // 1. 사용자가 가고 싶은 출발지 목적지의 전체 경로를 그리는 함수
        val route = drawRoute(routeRequest)

        // 2. 원하는 경로를 사용자 위치를 기반으로 안내하는 클래스 (새로운 스레드로 빼낸다.)
        //Navigate(route).run()
    }

    fun process(routeRequest: TransitRouteRequest) {
        // 1. 사용자가 가고 싶은 출발지 목적지의 전체 경로를 그리는 함수
        val route = drawRoute(routeRequest)

        // 2. 원하는 경로를 사용자 위치를 기반으로 안내하는 클래스 (새로운 스레드로 빼낸다.)
        //Navigate(route).run()
    }

    fun getRouteRequest():TransitRouteRequest {
        departureLatLng = StaticValue.naverMap.locationOverlay.position

        Log.d("getLatLng", departureLatLng.longitude.toString())

        return TransitRouteRequest(
            startX = departureLatLng.longitude.toString(),
            startY = departureLatLng.latitude.toString(),
            //출발지 샘플
            //startX = "126.833416",
            //startY = "37.642396",
            endX = "126.829695",
            endY = "37.627448",
            lang = 0,
            format = "json",
            count = 1
        )
    }

    // 1. 사용자가 가고 싶은 출발지 목적지의 전체 경로를 그리는 함수
    // 반환 값은 사용자가 확정한 경로이다.
    private fun drawRoute(routeRequest: TransitRouteRequest) : MutableList<PathOverlay> {
        // 관찰 변수 변경 시 콜백
        val transitRouteResponse = TransitManager(StaticValue.mainActivity).getTransitRoutes2(routeRequest)
        val transitRoutes = Convert().convertToRouteMutableLists(transitRouteResponse)
        val transitRoute = transitRoutes[0]

        routeControl = RouteControl(StaticValue.naverMap,Convert().convertLegRouteToLatLng(transitRoute),this)

        for (i in transitRoute){
            // pathOverlay는 네이버에서 제공하는 선 그리기 함수이며, 거기에 각 속성을 더 추가하여 색상을 칠했다.
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
                it.map = StaticValue.naverMap
            }
            pathOverlayList.add(pathOverlay)
        }
        return pathOverlayList
    }
}

fun convertIntToStr(color:Int) : Int {
    return ContextCompat.getColor(StaticValue.mainActivity, color)
}