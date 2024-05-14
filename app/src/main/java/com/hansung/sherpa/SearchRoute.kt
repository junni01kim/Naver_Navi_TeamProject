package com.hansung.sherpa

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hansung.sherpa.convert.Convert
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.convert.PathType
import com.hansung.sherpa.deviation.RouteControl
import com.hansung.sherpa.deviation.StrengthLocation
import com.hansung.sherpa.deviation.Section
import com.hansung.sherpa.geocoding.GeocodingAPI
import com.hansung.sherpa.transit.TransitManager
import com.hansung.sherpa.transit.TransitRouteRequest
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay

// 텍스트뷰에 값이 그래도 유지되고 있게하기 위한 뷰모델이다.
// searchRouteViewModel의 이름과 위치는 바꿀 필요가 있을듯
class SearchRouteViewModel:ViewModel() {
    val destinationText = MutableLiveData<String>()
}

class SearchRoute(val naverMap: NaverMap, val context: Context, val lifecycle: MainActivity) {
    // 출발지(내 위치)
    lateinit var departureLatLng:LatLng
    // 목적지는 값이 없을 수도 있다.
    var destinationLatLng:LatLng? = null

    // GeocodingAPI 클래스를 이용하여 원하는 주소의 좌표를 받아오는 함수이다.
    fun searchLatLng(destination:String): LatLng {
        val geoCodingApi = GeocodingAPI()
        geoCodingApi.request(destination,"목적지")

        // 여기서 무한루프 발생!!
        while(true) {
            if(geoCodingApi.getFlag("목적지")==false){
                break;
            }
            Log.d("getLatLng", "탐색중")
        }

        val coordinate = geoCodingApi.getCoordinate("목적지")
        //val latLng = LatLng(coordinate!!.latitude, coordinate.longitude)
        val latLng = LatLng(0.0, 0.0)
        return latLng
    }

    // 대중교통 이동 루트 검색 함수
    fun searchRoute() {
        val viewModel = SearchRouteViewModel()

        //val destinationText = viewModel.destinationText.value
        val destinationText = "서울특별시 성북구 삼선교로16길 116"

        destinationLatLng = searchLatLng(destinationText) // 오류 원인!!

        drawRoute()
    }

    // 정해진 루트에 선을 그리는 함수
    fun drawRoute() {
        lateinit var routeControl:RouteControl
        // 출발지점은 현재 자신의 좌표값을 받아서 설정한다.
        departureLatLng = naverMap.locationOverlay.position

        Log.d("getLatLng", departureLatLng.longitude.toString())

        // 요청 param setting
//        val routeRequest = TransitRouteRequest(
//            //startX = "126.926493082645",
//            startX = departureLatLng.longitude.toString(),
//            //startY = "37.6134436427887",
//            startY = departureLatLng.latitude.toString(),
//            endX = "127.126936754911",
//            //endX = destinationLatLng!!.longitude.toString(),
//            endY = "37.5004198786564",
//            //endY = destinationLatLng!!.latitude.toString(),
//            lang = 0,
//            format = "json",
//            count = 1
//        )

        // Testcase - 1
//        val routeRequest = TransitRouteRequest(
//            //startX = "126.926493082645",
//            startX = "126.835895",
//            //startY = "37.6134436427887",
//            startY = "37.642631",
//            endX = "126.831978",
//            //endX = destinationLatLng!!.longitude.toString(),
//            endY = "37.634765",
//            //endY = destinationLatLng!!.latitude.toString(),
//            lang = 0,
//            format = "json",
//            count = 1
//        )

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
        TransitManager(this.context).getTransitRoutes(routeRequest).observe(this.lifecycle) { transitRouteResponse ->
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

            routeControl = RouteControl(this.naverMap,COORDSES)

            for (i in COORDSES){
                i.first.forEach{
                    var marker = Marker()
                    marker.position = it
                    marker.map = this.naverMap
                }
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
                    it.passedColor = Color.LTGRAY
                    it.progress = 0.3
                    it.map = this.naverMap
                }
            }
        }

        var pathOverlaypre: PathOverlay? = null
        var pathOverlaycurr: PathOverlay? = null

        naverMap.addOnLocationChangeListener {location->
            if(routeControl!=null){
                var section = routeControl.checkingSection(StrengthLocation("Strong",LatLng(location.latitude,location.longitude)))
                Log.d("현재구역","현재위치: " + location.longitude+", "+location.latitude)
                Log.d("현재구역","시작: "+section.Start.longitude +", "+ section.Start.latitude+" 끝: " + section.End.longitude + ", " + section.End.latitude)
                if(pathOverlaycurr==null){
                    pathOverlaycurr = routeControl.drawProgressLine(section)
                    pathOverlaycurr!!.map = this.naverMap
                }
                else{
                    pathOverlaypre = pathOverlaycurr
                    pathOverlaycurr = routeControl.drawProgressLine(section)
                    pathOverlaypre?.map = null
                    pathOverlaycurr!!.map = this.naverMap
                }
                var isOut = routeControl.detectOutRoute(section, LatLng(location.latitude,location.longitude))
                if(isOut){
                    Log.d("이탈","이탈됨")
                    RouteControl.AlterToast.createToast(context)?.show()
                }

            }



        }

    }

    fun searchRoute(routeRequest: TransitRouteRequest) {
        val viewModel = SearchRouteViewModel()

        //val destinationText = viewModel.destinationText.value
        val destinationText = "서울특별시 성북구 삼선교로16길 116"

        destinationLatLng = searchLatLng(destinationText) // 오류 원인!!

        drawRoute(routeRequest)
    }

    fun drawRoute(routeRequest: TransitRouteRequest) {

        // 출발지점은 현재 자신의 좌표값을 받아서 설정한다.
        departureLatLng = naverMap.locationOverlay.position

        var transitRoutes: MutableList<MutableList<LegRoute>>

        // 관찰 변수 변경 시 콜백
        TransitManager(this.context).getTransitRoutes(routeRequest).observe(this.lifecycle) { transitRouteResponse ->
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
                    it.map = this.naverMap
                }
            }
        }
    }
}