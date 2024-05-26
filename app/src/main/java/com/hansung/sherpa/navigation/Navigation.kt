package com.hansung.sherpa.navigation

import android.graphics.Color
import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.convert.Convert
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.convert.PathType
import com.hansung.sherpa.deviation.RouteControl
import com.hansung.sherpa.deviation.StrengthLocation
import com.hansung.sherpa.gps.GPSDatas
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
class Navigation() {

    @RequiresApi(VERSION_CODES.R)
    val gpsDatas = GPSDatas(StaticValue.mainActivity)

    lateinit var routeControl:RouteControl

    // 출발지(내 위치)
    lateinit var departureLatLng:LatLng

    // 목적지는 값이 없을 수도 있다.
    var destinationLatLng:LatLng? = null
    var pathOverlayList:MutableList<PathOverlay> = mutableListOf<PathOverlay>()

    // 경로와 안내는 순서대로 진행된다.
    @RequiresApi(VERSION_CODES.R)
    fun run() {
        // 0. 사용자가 가고 싶은 출발지 목적지를 구하는 클래스
        //TODO("장소 찾기 SearchLocation 클래스 활용(출발지)")
        //TODO("장소 찾기 SearchLocation 클래스 활용(목적지)")

        // 1. 사용자가 가고 싶은 출발지 목적지의 전체 경로를 그리는 함수
        val route = drawRoute()

        // 2. 원하는 경로를 사용자 위치를 기반으로 안내하는 클래스 (새로운 스레드로 빼낸다.)
        Navigate(route).run()
    }

    // 1. 사용자가 가고 싶은 출발지 목적지의 전체 경로를 그리는 함수
    // 반환 값은 사용자가 확정한 경로이다.
    @RequiresApi(VERSION_CODES.R)
    fun drawRoute() : MutableList<LegRoute> {
        departureLatLng = StaticValue.naverMap.locationOverlay.position

        Log.d("getLatLng", departureLatLng.longitude.toString())

        // Testcase - 2
        val routeRequest = TransitRouteRequest(
            //startX = "126.926493082645",
            startX = "126.833416",
            //startY = "37.6134436427887",
            startY = "37.642396",
            endX = "126.829695",
            //endX = destinationLatLng!!.longitude.toString(),
            endY = "37.627448",
            //endY = destinationLatLng!!.latitude.toString(),
            lang = 0,
            format = "json",
            count = 1
        )

        var transitRoutes: MutableList<MutableList<LegRoute>>

        // 관찰 변수 변경 시 콜백
        val transitRouteResponse = TransitManager(StaticValue.mainActivity).getTransitRoutes2(routeRequest)
        transitRoutes = Convert().convertToRouteMutableLists(transitRouteResponse)
        val transitRoute = transitRoutes[0]

        routeControl = RouteControl(StaticValue.naverMap,transitRoutes[0],this)

        for (i in transitRoute){

            // pathOverlay는 네이버에서 제공하는 선 그리기 함수이며, 거기에 각 속성을 더 추가하여 색상을 칠했다.
            val pathOverlay = PathOverlay().also {
                it.coords = Convert().convertCoordinateToLatLng(i.coordinates)
                it.width = 10
                when(i.pathType){
                    PathType.WALK -> it.color = Color.BLUE
                    PathType.BUS -> it.color = Color.DKGRAY
                    PathType.EXPRESSBUS -> it.color = Color.RED
                    PathType.SUBWAY -> it.color = Color.GREEN
                    PathType.TRAIN -> it.color = Color.MAGENTA
                    else -> it.color = Color.YELLOW
                }
                it.passedColor = Color.LTGRAY
                it.progress = 0.3
                it.map = StaticValue.naverMap
            }
            pathOverlayList.add(pathOverlay)
        }


        var pathOverlaypre: PathOverlay? = null
        var pathOverlaycurr: PathOverlay? = null

        StaticValue.naverMap.addOnLocationChangeListener { location->
            if(routeControl!=null){
                var section = routeControl.checkingSection(StrengthLocation(gpsDatas.getGpsSignalAccuracy().Strength,LatLng(location.latitude,location.longitude)))
                Log.d("현재구역","현재위치: " + location.longitude+", "+location.latitude)
                Log.d("현재구역","시작: "+section.Start.longitude +", "+ section.Start.latitude+" 끝: " + section.End.longitude + ", " + section.End.latitude)
                if(pathOverlaycurr==null){
                    pathOverlaycurr = routeControl.drawProgressLine(section)
                    pathOverlaycurr!!.map = StaticValue.naverMap
                }
                else{
                    pathOverlaypre = pathOverlaycurr
                    pathOverlaycurr = routeControl.drawProgressLine(section)
                    pathOverlaypre?.map = null
                    pathOverlaycurr!!.map = StaticValue.naverMap
                }
                var isOut = routeControl.detectOutRoute(section, LatLng(location.latitude,location.longitude))
                if(isOut){
                    Log.d("이탈","이탈됨")
                    RouteControl.AlterToast.createToast(StaticValue.mainActivity)?.show()
//                    naverMap.map = null
                    for(i in this.pathOverlayList){
                        i.map = null
                    }
//                    section.End = LatLng(routeRequest.endY.toDouble(), routeRequest.endX.toDouble())
//                    section.End = LatLng(37.612746,127.834092) // 원당 좌표
                    section.End = LatLng(37.583145,127.011046) // 원당 좌표
                    routeControl.redrawDeviationRoute(section)
                }

            }

        }
        return transitRoute
    }

    fun searchRoute(routeRequest: TransitRouteRequest) {
        val viewModel = SearchRouteViewModel()

        //val destinationText = viewModel.destinationText.value
        val destinationText = "서울특별시 성북구 삼선교로16길 116"

        //////////////////////////////////////////////////
//        destinationLatLng = searchLatLng(destinationText) // 오류 원인!!

        drawRoute(routeRequest)
    }

    fun drawRoute(routeRequest: TransitRouteRequest) {

        // 출발지점은 현재 자신의 좌표값을 받아서 설정한다.
        departureLatLng = StaticValue.naverMap.locationOverlay.position

        var transitRoutes: MutableList<MutableList<LegRoute>>

        pathOverlayList = mutableListOf<PathOverlay>()

        // 관찰 변수 변경 시 콜백
        TransitManager(StaticValue.mainActivity).getTransitRoutes(routeRequest).observe(StaticValue.mainActivity) { transitRouteResponse ->
            transitRoutes = Convert().convertToRouteMutableLists(transitRouteResponse)
            val tt = Convert().convertToSearchRouteDataClass(transitRoutes[0])

            val COORDSES = mutableListOf<Pair<MutableList<LatLng>,String>>()

            // 임시 코드 각 값들 분리시키기 위함
            COORDSES.add(Pair(mutableListOf(),tt[0].type.toString()))
            for (i in 0..tt.size-2){
                if(tt[i].type!=tt[i+1].type){
                    COORDSES.add(Pair(mutableListOf(),tt[i+1].type.toString()))
                }
                COORDSES[COORDSES.size-1].first.add(tt[i].latLng)
            }
            COORDSES[COORDSES.size-1].first.add(tt[tt.size-1].latLng)

            this.routeControl.upDateRouteEnum(COORDSES)

            for (i in COORDSES){
                // pathOverlay는 네이버에서 제공하는 선 그리기 함수이며, 거기에 각 속성을 더 추가하여 색상을 칠했다.
                val pathOverlay = PathOverlay().also {
                    it.coords = i.first
                    it.width = 10
                    when(i.second){
                        PathType.WALK.toString() -> it.color = Color.BLUE
                        PathType.BUS.toString() -> it.color = Color.DKGRAY
                        PathType.EXPRESSBUS.toString() -> it.color = Color.RED
                        PathType.SUBWAY.toString() -> it.color = Color.GREEN
                        PathType.TRAIN.toString() -> it.color = Color.MAGENTA
                        else -> it.color = Color.YELLOW
                    }
                    it.passedColor = Color.YELLOW
                    it.progress = 0.3
                    it.map = StaticValue.naverMap
                }
                pathOverlayList.add(pathOverlay)
            }
        }
    }
}