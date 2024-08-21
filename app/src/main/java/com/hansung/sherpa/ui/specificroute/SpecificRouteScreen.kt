package com.hansung.sherpa.ui.specificroute


import android.os.Build
import android.util.Log
import android.widget.Toast
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
import com.hansung.sherpa.compose.transit.TransitManager
import com.hansung.sherpa.deviation.RouteDivation
import com.hansung.sherpa.dialog.SherpaDialog
import com.hansung.sherpa.itemsetting.RouteFilterMapper
import com.hansung.sherpa.itemsetting.TransportRoute
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
fun SpecificRouteScreen(response:TransportRoute){
    val context = LocalContext.current
    val totalTime by remember { mutableIntStateOf(response.info.totalTime ?: 0) }

    val density = LocalDensity.current // 화면 밀도
    val screenHeightSizeDp = LocalConfiguration.current.screenHeightDp.dp // 현재 화면 높이 DpSize
    val screenSizePx = with(density) {screenHeightSizeDp.toPx()} // 화면 밀도에 따른 화면 크기 PxSize

    // TODO: 김명준이 코드 추가한 부분 시작 ----------
    /**
     * @author 김명준
     *
     * @property coordParts response에서 routeList만 도출한 값
     * @property colorParts PathOverlay를 그릴 때 이용할 색상 값 모음
     * @property passedRoute 이미 이동된 경로를 지우기 위한 진행도 비율
     * @property routeControl 경로 이탈 알고리즘 객체
     */
    val coordParts = remember { setCoordParts(response) }
    val colorParts = setColerParts(response)
    val passedRoute = remember { SnapshotStateList<Double>().apply { repeat(coordParts.size) { add(0.0) } } }
    val routeDivation = RouteDivation(coordParts, passedRoute)

    /**
     * 보호자일 경우 사용자에게 검색한 경로를 전송할지 묻는 다이얼로그
     *
     * @property dialogFlag 경로 전송 다이얼로그가 화면에 나올지 말지 판단하는 flag
     *
     * TODO: 취소 후 다시 경로를 전송하고 싶을 때, 화면을 띄울 버튼이 필요하다.
     */
    val dialogFlag = remember { mutableStateOf(true) }
    if(dialogFlag.value) {
        SherpaDialog(title = "안내 시작", message = listOf("사용자 경로 안내를","시작하시겠습니까?"), confirmButtonText = "안내", dismissButtonText = "취소") {
            // TODO: 사용자에게 경로값 전송
            //StaticValue.transportRoute 이걸로
            dialogFlag.value = false
        }
    }

    /**
     * 네이버 map 화면
     *
     * @property myPos 현재 내 위치를 알려주는 함수
     * TODO: 의미없는 변수인거 같기도 해서 테스트 후 삭제해도 될 듯
     */
    var myPos by remember { mutableStateOf(LatLng(37.532600, 127.024612)) }
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

            routeDivation.detectOutRoute(myPos)
            val nowSubPath = routeDivation.nowSubpath

            /**
             * 경로 이탈 시 실행되는 함수
             *
             * detectOuteRoute() 반환 값 마다 동작이 다르다.
             *          -1 인 경우: 경로 안내 종료
             *           0 인 경우: 정상 이동
             *           1 인 경우: 경로 이탈이 된 경우
             */
            when(routeDivation.detectOutRoute(myPos)) {
                -1 -> {
                    Toast.makeText(context, "목적지에 도착하였습니다.\n경로 안내를 종료합니다.", Toast.LENGTH_SHORT).show()
                }
                0 -> {
                    routeDivation.calcProcess(myPos)
                }
                1 -> {
                    /**
                     * 도보의 경우 경로가 재설정 된다.
                     * 다른 타입(대중교통)의 경우 대중교통을 잘못 탑승했다고 판단해 경로 안내를 종료하고 다시 요청받도록 한다.
                     */
                    if(response.subPath[nowSubPath].trafficType == 3) {
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

                        val pedestrianResponse = TransitManager().getPedestrianRoute(pedestrianRouteRequest)

                        val newTransportRoute = RouteFilterMapper().pedstrianResponseToRouteList(pedestrianResponse)
                        coordParts[nowSubPath] = newTransportRoute

                        routeDivation.nowSection = 0
                        passedRoute[nowSubPath] = 0.0
                    }
                    else {
                        Toast.makeText(context, "잘못된 탑승!\n다음역에서 하차하세요.", Toast.LENGTH_SHORT).show()
                        // TODO: 경로 안내 종료 및 SpecificRouteScreen 나가기
                    }
                }
            }
        }
    ){
        DrawPathOverlay(coordParts, colorParts, passedRoute)
    }
    // TODO: 김명준이 코드 추가한 부분 끝 ----------

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

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


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun SpecificRoutePreview(){
    //SpecificRouteScreen()
}