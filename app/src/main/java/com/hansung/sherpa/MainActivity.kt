package com.hansung.sherpa

import android.graphics.PointF
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hansung.sherpa.deviation.RouteControl
import com.hansung.sherpa.gps.GPSDatas
import com.hansung.sherpa.navigation.Navigation
import com.hansung.sherpa.gps.GpsLocationSource
import com.hansung.sherpa.navigation.MyOnLocationChangeListener
import com.hansung.sherpa.navigation.OnLocationChangeManager
import com.hansung.sherpa.ui.theme.SherpaTheme
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource

class MainActivity : ComponentActivity(), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private lateinit var locationSource: FusedLocationSource
    private lateinit var destinationTextView: EditText // 목적지 textview
    private lateinit var searchButton: ImageButton // 검색 버튼
    private val markerIcon = OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_location_overlay_icon)

    // 내비게이션 안내 값을 전송하기 위함
    lateinit var navigation:Navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.CLIENT_ID)

        locationSource = GpsLocationSource.createInstance(this)

        setContent {
            SherpaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeCompose(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onMapReady(p0: NaverMap) {
        this.naverMap = p0

        // LocationOverlay 설정
        val locationOverlay = naverMap.locationOverlay
        locationOverlay.icon = markerIcon
        locationOverlay.isVisible = true

        //좌측하단 Tracking Mode 변환 버튼
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationSource = locationSource

        // 움직이는 사용자 마커 따라 그리기
        onChangeUserMarker()

        // 검색 필요 클래스 초기화
        val routeControl = RouteControl() // 사용자 위치 확인
        val gpsData = GPSDatas(this) // gps 위치
        navigation = Navigation() // 경로 그리기 & 탐색
        navigation.naverMap = naverMap
        navigation.mainActivity = this
        navigation.routeControl = routeControl

        // ---------- 삭제할 것 ----------
        StaticValue.navigation = navigation
        // ---------- 여기까지 ----------

        // 검색 버튼 클릭 리스너 (출발지, 도착지 검색시 경로 그리기)
        searchButton.setOnClickListener {
            //navigation.getTransitRoutes(startKeyword, endKeyword) // 프로젝트 1 진행 샘플 코드
            navigation.getTransitRoutes("", "")
        }

        // ----- 사용자 위치 변경시 경로 이탈 확인 로직 -----
        val i = object : MyOnLocationChangeListener {
            override fun callback(location: Location) {
                val nowLocation = LatLng(location.latitude, location.longitude)

                if (routeControl.detectOutRoute(nowLocation)) {// 경로이탈 탐지
                    navigation.redrawRoute(nowLocation, navigation.tempEndLatLng)
                }
            }
        }

        val OLCM = OnLocationChangeManager
        OLCM.naverMap = naverMap
        OLCM.addMyOnLocationChangeListener(i)
    }

    // 사용자 마커 표시
    private fun onChangeUserMarker() {
        val currMarker = Marker()
        naverMap.addOnLocationChangeListener { location ->
            currMarker.map = null
            setUserMarkerPosition(currMarker, LatLng(location.latitude, location.longitude))
        }
    }

    // 사용자 마커 위치 지정하여 재정의
    private fun setUserMarkerPosition(marker: Marker, latLng: LatLng) {
        marker.icon = markerIcon
        marker.position = latLng
        marker.anchor = PointF(0.5f, 0.5f)
        marker.map = naverMap
    }

    // 권한 확인
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun HomeCompose(modifier: Modifier = Modifier) {
    val markerIcon = OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_location_overlay_icon)
    // Jetpack Compose
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoom = 10.0, minZoom = 1.0)
        )
    }

    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isLocationButtonEnabled = false)
        )
    }

    val seoul = LatLng(37.532600, 127.024612)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition(seoul, 11.0)
    }

    var loc = remember {
        mutableStateOf(LatLng(37.532600, 127.024612))
    }

    Box(Modifier.fillMaxSize()) {
        NaverMap(locationSource = rememberFusedLocationSource(isCompassEnabled = true),
            properties = MapProperties(
                locationTrackingMode = com.naver.maps.map.compose.LocationTrackingMode.Follow,
            ),
            uiSettings = MapUiSettings(
                isLocationButtonEnabled = true,
            ),
            onLocationChange = { loc.value = LatLng(it.latitude, it.longitude) }) {
            MarkerComponent(loc.value, markerIcon)
        }

        Column {
            // 검색 텍스트필드 및 검색 버튼을 위한 행
            Row(horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                DestinationTextField()
                SearchButton()
            }
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MarkerComponent(loc: LatLng, markerIcon: OverlayImage) {
    Marker(state = MarkerState(position = loc), markerIcon)
}

/**
 * 목적지를 점색할 내용을 저장하는 Composable
 * SearchButton을 클릭 시 해당 정보가 함께 이동한다.
 */
@Composable
fun DestinationTextField(){
    var value by remember { mutableStateOf("") }

    TextField(
        value = value,
        onValueChange = { value = it },
        modifier = Modifier.fillMaxWidth(0.8f).height(55.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        placeholder = { Text("검색어를 입력하세요") }
    )
}

@Composable
fun SearchButton() {
    Button(
        modifier = Modifier
            .width(66.dp)
            .height(55.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(Color.Black),
        onClick = { /*TODO*/ }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.route_list_submit),
            contentDescription = "홈 화면에서 사용하는 검색 버튼"
        )
    }
}

@Preview
@Composable
fun HomePreview(){
    Box(Modifier.fillMaxSize()) {
        //TODO("
        // -네이버 맵은 프리뷰에서 생략-
        // 네이버 맵  Composable이 있는 영역.
        // 네이버 맵이 키값을 이용한 통신을 해야되는 관계로 프리뷰가 적용되지 않는다.
        // ")

        Column {
            // 검색 텍스트필드 및 검색 버튼을 위한 행
            Row(horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                DestinationTextField()
                SearchButton()
            }
        }
    }
}