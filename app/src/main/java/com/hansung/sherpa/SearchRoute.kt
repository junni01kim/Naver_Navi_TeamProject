package com.hansung.sherpa

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

// TemporaryClass는 임시 클래스라는 의미로 LatLng라는 클래스와 type를 값으로 가진다.
// path의 두가지 Pair은 출발지와 도착지의 lon lag를 의미한다.(실제론 LanLng를 의미) type는 대중교통의 종류를 의미한다.
// path:출발도착묶음Pair<출발지LanLng,도착지LanLng>

fun List<SearchRouteCoordinate>.getFirstList():List<LatLng>{
    val returnList = mutableListOf<LatLng>()
    for (i in this){
        returnList.add(i.latLng)
    }

    return returnList
}

class SearchRouteViewModel:ViewModel() {
    val departureText = MutableLiveData<String>()
}

// 선 그리는 함수
fun drawRoute(p0: NaverMap, context: Context, lifecycle: MainActivity, temporayClassList: List<SearchRouteCoordinate>){
    // 네이버 예제 참조
    val polyline = PolylineOverlay()

    polyline.coords = temporayClassList.getFirstList()

    // 요청 param setting
    val routeRequest = TransitRouteRequest(
        startX = "126.926493082645",
        startY = "37.6134436427887",
        endX = "127.126936754911",
        endY = "37.5004198786564",
        lang = 0,
        format = "json",
        count = 10
    )
    var transitRoutes: MutableList<MutableList<LegRoute>>
    // 관찰 변수 변경 시 콜백
    TransitManager(context).getTransitRoutes(routeRequest).observe(lifecycle) { transitRouteResponse ->
        transitRoutes = Convert().convertToRouteMutableLists(transitRouteResponse)
        val tt = Convert().convertToSearchRouteDataClass(transitRoutes[0])
        drawRouteColorful(p0, tt)
    }


    // 해결해야 한다.
    //path.map = naverMap
}

// 대중교통마다 다른 색의 선을 그리는 함수
fun drawRouteColorful(p0: NaverMap,temporayClassList: MutableList<SearchRouteCoordinate>) {
    val COORDS_1 = temporayClassList.map { it -> it.latLng }

    val pathOverlay = PathOverlay().also {
        it.coords = COORDS_1
        it.width = 5
        it.color = Color.BLUE
        it.outlineColor = Color.WHITE
        it.passedColor = Color.RED
        it.passedOutlineColor = Color.WHITE
        it.progress = 0.3
        it.map = p0
    }
}

// 텍스트뷰에 값이 그래도 유지되고 있게하기 위한 뷰모델이다.
// searchRouteViewModel의 이름과 위치는 바꿀 필요가 있을듯
class searchRouteViewModel:ViewModel() {
    val departureText = MutableLiveData<String>()
}
fun SearchRoute(departure:String) {
    val geocodingAPI = GeocodingAPI(departure)
}

fun searchRoute(p0: NaverMap, context: Context, lifecycle: MainActivity, departure:String) {
    // 단순히 geocoding이 되나 확인하기 위한 코드(삭제해도 됨)
    val geocodingAPI = GeocodingAPI(departure)

    // TODO(geocoding한 값으로 출발 도착지 값 받아오기)
    // TODO(값을 temporaryClassArray에 geocoding한 값 넣기 or 넣어져 있을 수도 있다.)

    drawRoute(p0, context, lifecycle, listOf(
        SearchRouteCoordinate(LatLng(37.57152, 126.97714),PathType.WALK)
        ,SearchRouteCoordinate(LatLng(37.56607, 126.98268),PathType.BUS)))
}