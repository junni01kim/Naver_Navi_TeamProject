package com.hansung.sherpa.ui.specificroute


import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.compose.transit.TransitManager
import com.hansung.sherpa.deviation.RouteDeviation
import com.hansung.sherpa.dialog.SherpaDialog
import com.hansung.sherpa.itemsetting.RouteFilterMapper
import com.hansung.sherpa.itemsetting.SubPath
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.subwayelevator.ElevatorLocResponse
import com.hansung.sherpa.subwayelevator.getSubwayElevLocation
import com.hansung.sherpa.transit.pedestrian.PedestrianRouteRequest
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberFusedLocationSource

enum class DragValue { Start, Center, End }

/**
 * 경로의 세부 경로들 몇번 버스 이용, 어디서 내리기, 몇m 이동 등등의
 * 세부 정부 표현 화면
 *
 * (해당 Composable에서 UI조합 시작함)
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalNaverMapApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun SpecificRouteScreen(response:TransportRoute, goBack:()->Unit){
    val context = LocalContext.current
    val totalTime by remember { mutableIntStateOf(response.info.totalTime ?: 0) }

    val density = LocalDensity.current // 화면 밀도
    val screenHeightSizeDp = LocalConfiguration.current.screenHeightDp.dp // 현재 화면 높이 DpSize
    val screenSizePx = with(density) {screenHeightSizeDp.toPx()} // 화면 밀도에 따른 화면 크기 PxSize


    //TODO 좌표 삽입 reponse 업데이트
    // Goal -  보행자 끝점을 찾는다 이후 해당부분의 끝에 엘리베이터 좌표 삽입
    // 1. 어느 부분에 넣어야 하는가?
    // 1-1) 보행자 -> 지하철의 보행자 끝부분
    // 1-2) 지하철 -> 보행자의 보행자 앞부분
    // 2. 엘리베이터의 위치는?
    // 2-1) 엘리베이터의 역이름 조회 : subPath.traffictype이 지하철 and sectionInfo.startName사용

    var elevatorLocation:ElevatorLocResponse
    var pedestrianLoc:LatLng
    var pedestrianPart:SubPath
    var minElevLoc:LatLng


    for(i in response.subPath){
        try {
            if(i.trafficType==1){
                var index = response.subPath.indexOf(i)

                if(index<=0 && index >= response.subPath.size-1) {
                    continue
                }

                // 보행자 -> 지하철의 경우
                if(response.subPath[index-1].trafficType==3){
                    pedestrianPart = response.subPath[index-1]

                    elevatorLocation = getSubwayElevLocation(i.sectionInfo.startName!!)

                    pedestrianLoc = LatLng(pedestrianPart.sectionInfo.endY!!, pedestrianPart.sectionInfo.endX!!)

                    minElevLoc = findMinDistanceLatLng(elevatorLocation, pedestrianLoc)

                    pedestrianPart.sectionRoute.routeList.add(minElevLoc)
                }//지하철->보행자의 경우
                else if(response.subPath[index+1].trafficType==3){
                    pedestrianPart = response.subPath[index+1]

                    elevatorLocation = getSubwayElevLocation(i.sectionInfo.endName!!)

                    pedestrianLoc = LatLng(pedestrianPart.sectionInfo.startY!!, pedestrianPart.sectionInfo.startX!!)

                    minElevLoc = findMinDistanceLatLng(elevatorLocation, pedestrianLoc)

                    pedestrianPart.sectionRoute.routeList.add(0, minElevLoc)
                }
                else{
                    continue
                }
            }
        }catch (e:Exception){// 찾는 지하철이 DB에 없는 경우
            Log.d("ElevatorError", e.message!!)
            continue
        }
    }


    // TODO: 김명준이 코드 추가한 부분 시작 ----------
    /**
     * @author 김명준
     *
     * @property myPos 현재 내 위치를 알려주는 함수
     * @property coordParts response에서 routeList만 도출한 값
     * @property colorParts PathOverlay를 그릴 때 이용할 색상 값 모음
     * @property passedRoute 이미 이동된 경로를 지우기 위한 진행도 비율
     * @property routeControl 경로 이탈 알고리즘 객체
     */
    var myPos by remember { mutableStateOf(LatLng(37.532600, 127.024612)) }
    val coordParts = remember { setCoordParts(response) }
    val colorParts = setColerParts(response)
    val passedRoute = remember { SnapshotStateList<Double>().apply { repeat(coordParts.size) { add(0.0) } } }
    val routeDivation = RouteDeviation(coordParts, passedRoute)
    var startNavigation by remember { mutableStateOf(false)}

    /**
     * 보호자일 경우 사용자에게 검색한 경로를 전송할지 묻는 다이얼로그
     *
     * @property dialogFlag 경로 전송 다이얼로그가 화면에 나올지 말지 판단하는 flag
     *
     */
    var dialogFlag by remember { mutableStateOf(false) }
    if(dialogFlag) {
        SherpaDialog(title = "안내 시작", message = listOf("사용자 경로 안내를","시작하시겠습니까?"), confirmButtonText = "안내", dismissButtonText = "취소") {
            if(StaticValue.userInfo.role1 == "CAREGIVER"){
                // TODO: 사용자에게 경로값 전송
                //StaticValue.transportRoute 이걸로
                dialogFlag = false
            }
            else {
                val toLatLng = coordParts[1][0]

                val pedestrianRouteRequest = PedestrianRouteRequest(
                    startX = myPos.longitude.toFloat(),
                    startY = myPos.latitude.toFloat(),
                    endX = toLatLng.longitude.toFloat(),
                    endY = toLatLng.latitude.toFloat()
                )

                val pedestrianResponse =
                    TransitManager().getPedestrianRoute(pedestrianRouteRequest)

                val newTransportRoute =
                    RouteFilterMapper().pedstrianResponseToRouteList(pedestrianResponse)
                coordParts[0] = newTransportRoute

                // 경로 재 요청으로 기존 진행 값 초기화
                routeDivation.nowSection = 0
                passedRoute[0] = 0.0
                routeDivation.renewWholeDistance(coordParts[0])

                startNavigation = true
                dialogFlag = false
            }
        }
    }

    NaverMap(
        locationSource = rememberFusedLocationSource(isCompassEnabled = true),
        properties = MapProperties(
            locationTrackingMode = com.naver.maps.map.compose.LocationTrackingMode.Follow,
        ),
        uiSettings = MapUiSettings(
            isLocationButtonEnabled = true,
        ),
        onLocationChange = {
            myPos = LatLng(it.latitude, it.longitude)

            /**
             * 경로 이탈 시 실행되는 함수
             *
             * detectOuteRoute() 반환 값 마다 동작이 다르다.
             *          -1 인 경우: 경로 안내 종료
             *           0 인 경우: 정상 이동
             *           1 인 경우: 경로 이탈이 된 경우
             */
            if(startNavigation) {
            //if(false) {
                when (routeDivation.detectOutRoute(myPos)) {
                    -1 -> {
                        Toast.makeText(context, "목적지 인근에 도착하였습니다.\n경로 안내를 종료합니다.", Toast.LENGTH_SHORT).show()
                        for (i in passedRoute.indices) {
                            passedRoute[i] = 1.0
                        }
                        startNavigation = false
                    }
                    0 -> {
                        routeDivation.renewProcess(myPos)
                    }
                    1 -> {
                        /**
                         * 도보의 경우 경로가 재설정 된다.
                         * 다른 타입(대중교통)의 경우 대중교통을 잘못 탑승했다고 판단해 경로 안내를 종료하고 다시 요청받도록 한다.
                         */
                        val nowSubPath = routeDivation.nowSubpath
                        if (response.subPath[nowSubPath].trafficType == 3) {
                            Log.d("RouteControl", "경로이탈")
                            // 너무 많이나와서 잠궈 둠
                            //Toast.makeText(context, "경로를 이탈하였습니다.\n경로를 재설정합니다.", Toast.LENGTH_SHORT).show()

                            val lastSectionIndex = coordParts[nowSubPath].lastIndex
                            val toLatLng = coordParts[nowSubPath][lastSectionIndex]

                            val pedestrianRouteRequest = PedestrianRouteRequest(
                                startX = myPos.longitude.toFloat(),
                                startY = myPos.latitude.toFloat(),
                                endX = toLatLng.longitude.toFloat(),
                                endY = toLatLng.latitude.toFloat()
                            )

                            val pedestrianResponse =
                                TransitManager().getOsrmPedestrianRoute(pedestrianRouteRequest)

                            val newTransportRoute =
                                RouteFilterMapper().pedstrianResponseToRouteList(pedestrianResponse)
                            coordParts[nowSubPath] = newTransportRoute

                            // 경로 재 요청으로 기존 진행 값 초기화
                            routeDivation.nowSection = 0
                            passedRoute[nowSubPath] = 0.0
                            routeDivation.renewWholeDistance(coordParts[nowSubPath])
                        } else {
                            Toast.makeText(context, "잘못된 탑승!\n다음역에서 하차하세요.", Toast.LENGTH_SHORT)
                                .show()
                            // TODO: 경로 안내 종료 및 SpecificRouteScreen 나가기
                        }
                    }
                }
            }
        }
    ){
        DrawPathOverlay(coordParts, colorParts, passedRoute)
    }
    // TODO: 김명준이 코드 추가한 부분 끝 ----------

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    // 핸드폰의 뒤로가기 버튼 누를시 화면 종료(메인 화면으로 이동)
    BackHandler {
        goBack()
    }

    BottomSheetScaffold(
        sheetDragHandle = {},
        sheetContainerColor = Color.White,
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
            topStart = 20.dp,
            topEnd = 20.dp
        ),
        sheetContent = {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                SpecificPreview(response) // 경로에 대한 프로그래스바 및 총 걸리는 시간 표시 (Card의 최 상단 부분)
                SpecificList(response) // 각 이동 수단에 대한 도착지, 출발지, 시간을 표시 (여기서 Expand 수행)
            }
        },
        // 해당 부분은 초기 높이임
        sheetPeekHeight = 85.dp
    ) {

    }
}

fun findMinDistanceLatLng(elevatorLocation: ElevatorLocResponse, pedestrianLoc: LatLng):LatLng{
    var target:LatLng
    lateinit var ret:LatLng
    var mindist = 100000000000.0

    for (i in elevatorLocation.data){
        var slicing = i.location.split(",")
        var lat = slicing[0].toDouble()
        var lon = slicing[1].toDouble()

        target = LatLng(lat, lon)

        if(target.distanceTo(pedestrianLoc)<mindist){
            mindist = target.distanceTo(pedestrianLoc)
            ret = target
        }
    }

    return ret
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun SpecificRoutePreview(){
    //SpecificRouteScreen()
}