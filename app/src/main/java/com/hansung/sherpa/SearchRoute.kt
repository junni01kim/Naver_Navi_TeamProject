package com.hansung.sherpa

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hansung.sherpa.geocoding.GeocodingAPI
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.PolylineOverlay

// TemporaryClass는 임시 클래스라는 의미로 LatLng라는 클래스와 type를 값으로 가진다.
// path의 두가지 Pair은 출발지와 도착지의 lon lag를 의미한다.(실제론 LanLng를 의미) type는 대중교통의 종류를 의미한다.
// path:출발도착묶음Pair<출발지LanLng,도착지LanLng>
data class TemporaryClass(val latLng:LatLng, val type:String)

fun List<TemporaryClass>.getFirstList():List<LatLng>{
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
fun drawRoute(temporayClassList: List<TemporaryClass>){
    // 네이버 예제 참조
    val polyline = PolylineOverlay()

    polyline.coords = temporayClassList.getFirstList()

    // 해결해야 한다.
    //path.map = naverMap
}

// 대중교통마다 다른 색의 선을 그리는 함수
fun drawRouteColorful(temporayClassList: List<TemporaryClass>){

    // type마다 그려지는 값들이 달라져야 하기 때문에 여러개의 polyLine을 그려서 루프 돌릴 예정. 좀 비효율 적이다.
    // 대중교통은 환승 수를 최소로 만들기 때문에 그려지는 시간이 오래 걸리진 않을 것이라고 생각한다.

    // 한줄짜리 polyLineList를 생성하고, 리스트에 모두 넣는다.
    val polyLineList = mutableListOf<PolylineOverlay>()
    for (i in 0..temporayClassList.size-2){
        polyLineList.add(PolylineOverlay())
        polyLineList[i].coords = listOf(temporayClassList[i].latLng,temporayClassList[i+1].latLng)
    }

    // polyLineList에 있는 모든 polyLine들을 루프를 돌면서 그린다.
    for(i in 0..polyLineList.size-2) {
        // 각 값들은 출발지점에서 데이터로 받는 대중교통 type을 따라 색상을 변경 한다.
        when(temporayClassList[i].type){
            "walk" -> {
                // TODO(그리기)
                polyLineList[i].color = Color.YELLOW
                Log.d("move", "걷기")
            }
            "bus" -> {
                // TODO(그리기)
                polyLineList[i].color = Color.GREEN
                Log.d("move", "버스")
            }
            "subway" -> {
                // TODO(그리기)
                polyLineList[i].color = Color.BLUE
                Log.d("move", "지하철")
            }
            else -> {
                // TODO(그리기)
                polyLineList[i].color = Color.BLACK
                Log.d("move", "오류")
            }
        }
    }

    // 해결해야 한다.
    //path.map = naverMap
}

fun searchRoute(departure:String) {
    // 단순히 geocoding이 되나 확인하기 위한 코드(삭제해도 됨)
    val geocodingAPI = GeocodingAPI(departure)

    // TODO(geocoding한 값으로 출발 도착지 값 받아오기)
    // TODO(값을 temporaryClassArray에 geocoding한 값 넣기 or 넣어져 있을 수도 있다.)

    drawRoute(listOf(TemporaryClass(LatLng(37.57152, 126.97714),"walk"),TemporaryClass(LatLng(37.56607, 126.98268),"bus")))
}