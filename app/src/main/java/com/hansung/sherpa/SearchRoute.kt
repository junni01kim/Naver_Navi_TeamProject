package com.hansung.sherpa

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hansung.sherpa.convert.Convert
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.convert.PathType
import com.hansung.sherpa.convert.SearchRouteCoordinate
import com.hansung.sherpa.geocoding.GeocodingAPI
import com.hansung.sherpa.transit.TransitManager
import com.hansung.sherpa.transit.TransitRouteRequest
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.overlay.PolylineOverlay

// 텍스트뷰에 값이 그래도 유지되고 있게하기 위한 뷰모델이다.
// searchRouteViewModel의 이름과 위치는 바꿀 필요가 있을듯
class SearchRouteViewModel:ViewModel() {
    val destinationText = MutableLiveData<String>()
}

// 선 그리는 함수
//fun drawRoute(naverMap: NaverMap, context: Context, lifecycle: MainActivity, departure:지오코딩, destination:지오코딩){
fun drawRoute(naverMap: NaverMap, context: Context, lifecycle: MainActivity){
    // 임시 파라미터이다.
    // 요청 param setting
    val routeRequest = TransitRouteRequest(
        startX = "126.926493082645", // startX = departure.lat.toString(),
        startY = "37.6134436427887", // startY = departure.lon.toString(),
        endX = "127.126936754911", // endX = destination.lat.toString(),
        endY = "37.5004198786564", // endY = destination.lon.toString(),
        lang = 0,
        format = "json",
        count = 10
    )

    var transitRoutes: MutableList<MutableList<LegRoute>>

    // 관찰 변수 변경 시 콜백
    TransitManager(context).getTransitRoutes(routeRequest).observe(lifecycle) { transitRouteResponse ->
        transitRoutes = Convert().convertToRouteMutableLists(transitRouteResponse)
        val tt = Convert().convertToSearchRouteDataClass(transitRoutes[0])

        val COORDS_1 = tt.map { it -> it.latLng }

        val pathOverlay = PathOverlay().also {
            it.coords = COORDS_1
            it.width = 10
            it.color = Color.BLUE
            it.outlineColor = Color.WHITE
            it.passedColor = Color.RED
            it.passedOutlineColor = Color.WHITE
            it.progress = 0.3
            it.map = naverMap
        }
    }
}

fun SearchRoute(destination:String) {
    val geocodingAPI = GeocodingAPI(destination)
}

fun searchRoute(naverMap: NaverMap, context: Context, lifecycle: MainActivity) {
    val viewModel = SearchRouteViewModel()

    // TODO(geocoding한 값으로 출발 도착지 값 받아오기)
    val departureText:String? = null // 출발내 위치 받아오셈
    val destinationText = viewModel.destinationText.value

    // TODO(값을 temporaryClassArray에 geocoding한 값 넣기 or 넣어져 있을 수도 있다.)
    // val departure = 지오코딩(departure)
    // val destination = 지오코딩(destination)

    drawRoute(naverMap, context, lifecycle)
    //drawRoute(naverMap, context, lifecycle, departure, destination)
}