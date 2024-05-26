package com.hansung.sherpa

import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.hansung.sherpa.navigation.Navigation
import com.hansung.sherpa.navigation.SearchRouteViewModel
import com.hansung.sherpa.gps.GpsLocationSource
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

    private lateinit var naverMap:NaverMap

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private lateinit var locationSource: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StaticValue.mainActivity = this
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.CLIENT_ID) // 본인 api key


        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)

//        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        locationSource = GpsLocationSource.getInstance(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // naverMap객체가 준비됨
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onMapReady(p0: NaverMap){
        this.naverMap = p0
        StaticValue.naverMap = naverMap

        naverMap.locationSource = locationSource

        //좌측하단 Tracking Mode 변환 버튼
        naverMap.uiSettings.setLocationButtonEnabled(true)

        // LocationOverlay 설정
        val  locationOverlay = naverMap.locationOverlay
        locationOverlay.icon = OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_location_overlay_icon)
        locationOverlay.isVisible = true

        var preMarker:Marker? = null
        var currMarker:Marker? = null

        naverMap.addOnLocationChangeListener { location->
            if(preMarker==null && currMarker==null){ //초기 사용자 Marker 표시 및 생성
                currMarker = Marker()
                currMarker!!.icon = OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_location_overlay_icon)
                currMarker!!.position = LatLng(location)
                currMarker!!.anchor = PointF(0.5f, 0.5f)
                currMarker!!.map = naverMap
            }
            else{ // 사용자 위치 마커 최신화
                preMarker = currMarker
                preMarker?.map = null
                currMarker  = Marker()
                currMarker!!.icon = OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_location_overlay_icon)
                currMarker!!.position = LatLng(location)
                currMarker!!.anchor = PointF(0.5f, 0.5f)
                currMarker!!.map = naverMap
            }
        }

        // 검색하기 전까지 값을 저장해두기 위한 viewModel이다. searchRoute.kt에 저장되어있다.
        val viewModel = ViewModelProvider(this)[SearchRouteViewModel::class.java]

        // 출발지점을 작성하는 textView, 출발지점 작성 후 전송하기위한 button
        val destinationTextView = findViewById<EditText>(R.id.destination_editText)
        val searchButton = findViewById<ImageButton>(R.id.search_button)

        // 버튼 클릭 리스너
        searchButton.setOnClickListener{
            Navigation().run()
        }

        viewModel.destinationText.observe(this){
            viewModel.destinationText.value = destinationTextView.text.toString()
        }
    }
}