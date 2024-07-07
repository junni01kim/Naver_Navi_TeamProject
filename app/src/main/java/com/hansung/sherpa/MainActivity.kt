package com.hansung.sherpa

import android.graphics.PointF
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.hansung.sherpa.deviation.RouteControl
import com.hansung.sherpa.deviation.StrengthLocation
import com.hansung.sherpa.gps.GPSDatas
import com.hansung.sherpa.navigation.Navigation
import com.hansung.sherpa.gps.GpsLocationSource
import com.hansung.sherpa.navigation.MyOnLocationChangeListener
import com.hansung.sherpa.navigation.OnLocationChangeManager
import com.hansung.sherpa.ui.main.FlipperEvent
import com.hansung.sherpa.ui.main.FloatIconEvent
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private lateinit var locationSource: FusedLocationSource
    private lateinit var destinationTextView: EditText // 목적지 textview
    private lateinit var searchButton: ImageButton // 검색 버튼
    private val markerIcon = OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_location_overlay_icon)

    // Float Icons
    private lateinit var medicalIconEvent: ExtendedFloatingActionButton
    private lateinit var manIconEvent: ExtendedFloatingActionButton
    private lateinit var womanIconEvent: ExtendedFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.CLIENT_ID)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)

        destinationTextView = findViewById(R.id.destination_editText)
        searchButton = findViewById(R.id.search_button)

//        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        locationSource = GpsLocationSource.createInstance(this)

        // Float Icons Events
        val fIEvent = FloatIconEvent()
        medicalIconEvent = findViewById(R.id.floating_action_button_medical)
        manIconEvent = findViewById(R.id.floating_action_button_man)
        womanIconEvent = findViewById(R.id.floating_action_button_woman)
        fIEvent.setOnClick(medicalIconEvent)
        fIEvent.setOnClick(manIconEvent)
        fIEvent.setOnClick(womanIconEvent)

        // 목적지, 내 위치 Flipper Event
        FlipperEvent().onFlip(this)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onMapReady(p0: NaverMap) {
        this.naverMap = p0
        StaticValue.naverMap = naverMap // todo: 임시 바로 삭제할 것(김명준)

        // LocationOverlay 설정
        val locationOverlay = naverMap.locationOverlay
        locationOverlay.icon = markerIcon
        locationOverlay.isVisible = true

        //좌측하단 Tracking Mode 변환 버튼
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationSource = locationSource

        // 움직이는 사용자 마커 따라 그리기
        onChangeUserMarker()

        // 검색어 키워드
        val startKeyword = "반드시 지우시오!" // TODO : SearchLocation.kt 완성 시 반드시 수정할 것 !!!
        val endKeyword = destinationTextView.text.toString()

        // 검색 필요 클래스 초기화
        val routeControl = RouteControl() // 사용자 위치 확인
        val gpsData = GPSDatas(this) // gps 위치
        val navigation = Navigation() // 경로 그리기 & 탐색
        navigation.naverMap = naverMap
        navigation.mainActivity = this
        navigation.routeControl = routeControl

        // 검색 버튼 클릭 리스너 (출발지, 도착지 검색시 경로 그리기)
        searchButton.setOnClickListener {
            navigation.getTransitRoutes(startKeyword, endKeyword)
        }

        // ----- 사용자 위치 변경시 경로 이탈 확인 로직 -----
        val i = object : MyOnLocationChangeListener {
            override fun callback(location: Location) {
                val nowLocation = LatLng(location.latitude, location.longitude)

                if (routeControl.detectOutRoute(nowLocation)) {// 경로이탈 탐지
                    navigation.redrawRoute(nowLocation, navigation.tempEndLatLng)
                    //navigation.redrawRoute(LatLng(126.8328164,37.6409022), navigation.tempEndLatLng)
                }
            }
        }

        val OLCM = OnLocationChangeManager
        OLCM.naverMap = naverMap
        OLCM.addMyOnLocationChangeListener(i)
        // ----- 경로 이탈 확인 로직 끝 -----

        // 검색하기 전까지 값을 저장해두기 위한 viewModel이다. searchRoute.kt에 저장되어있다.
        val viewModel = ViewModelProvider(this)[SearchRouteViewModel::class.java]

        viewModel.destinationText.observe(this) {
            viewModel.destinationText.value = destinationTextView.text.toString()
        }
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

class SearchRouteViewModel: ViewModel() {
    val destinationText = MutableLiveData<String>()
}

