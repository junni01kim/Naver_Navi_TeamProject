package com.hansung.sherpa.ui.specificroute


import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.compose.transit.TransitManager
import com.hansung.sherpa.deviation.RouteControl
import com.hansung.sherpa.dialog.SherpaDialog
import com.hansung.sherpa.itemsetting.RouteFilterMapper
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.transit.PedestrianRouteRequest
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
@OptIn(ExperimentalFoundationApi::class, ExperimentalNaverMapApi::class)
@Composable
fun SpecificRouteScreen(response:TransportRoute){
    val context = LocalContext.current

    val totalTime by remember { mutableIntStateOf(response.info.totalTime ?: 0) }

    val density = LocalDensity.current // 화면 밀도
    val screenHeightSizeDp = LocalConfiguration.current.screenHeightDp.dp // 현재 화면 높이 DpSize
    val screenSizePx = with(density) {screenHeightSizeDp.toPx()} // 화면 밀도에 따른 화면 크기 PxSize
    val anchors = remember {
        DraggableAnchors{
            DragValue.Start at 0f
            DragValue.End at 300f
        }
    }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Start,
            anchors = anchors,  // 생성자에 anchors 전달
            positionalThreshold = { distance: Float -> distance * 0.1f },
            velocityThreshold = { with(density) { 10.dp.toPx() } },
            snapAnimationSpec = tween(),  // snapAnimationSpec로 지정
            decayAnimationSpec = exponentialDecay(),  // decayAnimationSpec 추가
            confirmValueChange = { true }
        )
    }
    
    // TODO: 김명준이 코드 추가한 부분 시작 ----------
    val coordParts = remember { mutableStateOf(setCoordParts(response)) }
    val colorParts = remember { mutableStateOf(setColerParts(response)) }
    val routeControl = RouteControl()

    val dialogToggle = remember { mutableStateOf(true) }
    if(dialogToggle.value) {
        SherpaDialog(title = "안내 시작", message = listOf("사용자 경로 안내를\n시작하시겠습니까?"), confirmButtonText = "안내", dismissButtonText = "취소") {
            // TODO: 사용자에게 경로값 전송
            //StaticValue.transportRoute 이걸로
            dialogToggle.value = false
        }
    }

    routeControl.initializeRoute()

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
            routeControl.detectOutRoute(myPos)
            val nowSubpath = routeControl.nowSubpath
            val nowSection = routeControl.nowSection

            // 경로를 이탈을 했을 경우에 실행
            if (routeControl.detectOutRoute(myPos)) {
                if(response.subPath[nowSubpath].trafficType == 3) {
                    //Toast.makeText(context, "경로를 이탈하였습니다.\n경로를 재설정합니다.", Toast.LENGTH_SHORT).show()

                    Log.d("explain", "경로이탈: 경로 다시 그려져야 됨")

                    val lastSectionIndex = coordParts.value[nowSubpath].lastIndex
                    val toLatLng = coordParts.value[nowSubpath][lastSectionIndex]

                    val pedestrianRouteRequest = PedestrianRouteRequest(
                        startX = myPos.longitude.toFloat(),
                        startY = myPos.latitude.toFloat(),
                        endX = toLatLng.longitude.toFloat(),
                        endY = toLatLng.latitude.toFloat()
                    )

                    val pedestrianResponse = TransitManager().getPedestrianRoute(pedestrianRouteRequest)

                    val newTransportRoute = RouteFilterMapper().pedstrianResponseToRouteList(pedestrianResponse)
                    coordParts.value[nowSubpath] = newTransportRoute

                    routeControl.nowSection = 0

                    // TODO: 해야할 추가 로직
//                  routeControl.delRouteToIndex(shortestRouteIndex)
//                  navigation.redrawRoute(nowLocation, toLatLng)
                }
                else {
                    Log.d("explain","경로이탈: 경로 안내 종료")
                }
            }
        }
    ){
        DrawPathOverlay(coordParts, colorParts)
    }
    // TODO: 김명준이 코드 추가한 부분 끝 ----------

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier
            .heightIn(min = screenHeightSizeDp - 305.dp, max = screenHeightSizeDp - 305.dp + 227.dp)
            .height(screenHeightSizeDp - 305.dp + state.requireOffset().dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(305.dp)
                .background(Color.Transparent)
                .anchoredDraggable(state, Orientation.Vertical),
            colors = cardColors(
                containerColor = Color.White  // 카드의 배경 색상 설정
            ),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
            border = BorderStroke(2.dp, Color.LightGray)
        ){
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                SpecificPreview(response) // 경로에 대한 프로그래스바 및 총 걸리는 시간 표시 (Card의 최 상단 부분)
                SpecificList(response) // 각 이동 수단에 대한 도착지, 출발지, 시간을 표시 (여기서 Expand 수행)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun SpecificRoutePreview(){

    //SpecificRouteScreen()
}