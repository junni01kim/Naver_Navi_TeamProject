package com.hansung.sherpa

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
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
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.CLIENT_ID) // 본인 api key

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)


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
    override fun onMapReady(p0: NaverMap){
        this.naverMap = p0

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

    }
}
