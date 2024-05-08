package com.hansung.sherpa

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.GeometryUtils
import com.skt.tmap.TMapData
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapPolyLine
import com.skt.tmap.poi.TMapPOIItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 미완성 코드 Merge 금지 !!!!
 * Written By JNYLee
 */
class MainActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var tMapView: TMapView
    private lateinit var start: EditText
    private lateinit var end: EditText
    private lateinit var search: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.CLIENT_ID) // 본인 api key

    }

    private fun routeApi(start: TMapPOIItem, end: TMapPOIItem, naverMap: NaverMap) {
        val transitManager = TransitManager(this@MainActivity)

        // 네이버 경로선 스타일 setting
        val width = resources.getDimensionPixelSize(R.dimen.path_overlay_width)
        val outlineWidth = resources.getDimensionPixelSize(R.dimen.path_overlay_outline_width)

        // POST로 요청하는 request properties
        val routeRequest = RouteRequest(
            startX = start.frontLon,
            startY = start.frontLat,
            endX = end.frontLon,
            endY = end.frontLat,
            lang = 0,
            format = "json",
            count = 10
        )

        // 경로 요청 후 실시간으로 감시해서 값을 가져오면 실행하는 콜백함수
        transitManager.getRoutes(routeRequest).observe(this@MainActivity) { routeResponse ->
            val lines = RouteLine(routeResponse).drawRoute()

            for (tLine in lines) {
                // Tmap 좌표 -> Naver 좌표로 변환
                val coord = mutableListOf<LatLng>()
                for (point in tLine.linePointList) {
                    coord.add(LatLng(point.latitude, point.longitude))
                }

                // naver 경로
                val pathOverlay = PathOverlay().also {
                    it.coords = coord
                    it.width = width
                    it.outlineWidth = outlineWidth
                    it.color = ResourcesCompat.getColor(resources, R.color.primary, theme)
                    it.outlineColor = Color.WHITE
                    it.passedColor = ResourcesCompat.getColor(resources, R.color.gray, theme)
                    it.passedOutlineColor = Color.WHITE
                    it.progress = 0.3
                    it.map = naverMap
                }
            }
        }

    }

    // 지도가 로딩 되면 실행되는 콜백 함수
    override fun onMapReady(naverMap: NaverMap) {
            var a = TMapPOIItem() // xml 에서 가져오는 출발지 POI
            var b = TMapPOIItem() // xml 에서 가져오는 출발지 POI
        
        // 코루틴으로 가져오는 POI 값들
            runBlocking {
                launch(Dispatchers.IO) {
                    a = TMapData().findAllPOI("홍대 토즈", 1)[0] // 1개를 가져오고 배열의 0번째 선택
                }
                launch(Dispatchers.IO) {
                    b = TMapData().findAllPOI("한성대학교", 1)[0]
                }
            }

            routeApi(a, b, naverMap) // 경로 데이터 가져오고 그리기
        }

}