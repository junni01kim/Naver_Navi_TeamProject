package com.hansung.sherpa.sendInfo

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.itemsetting.SubPath
import com.hansung.sherpa.itemsetting.SubPathAdapter
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.sendInfo.receive.ReceiveManager
import com.hansung.sherpa.sendInfo.receive.ReceiveRouteResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ColorPart
import com.owlike.genson.GensonBuilder
import java.nio.ByteBuffer

/**
 * 보호자가 FCM을 통해 '전달받은' 정보들을 관리하는 ViewModel
 *
 * @property coordParts 사용자가 경로 안내를 진행하는 경로 리스트
 * @property colorPart 사용자가 이동하는 경로들의 이동수단 색상 (도보는 black)
 * @property passedRoute 사용자의 경로 이동 진척도
 */
class CaregiverViewModel : ViewModel()  {
    private val _coordParts = MutableLiveData(mutableStateListOf(mutableListOf(LatLng(0.0,0.0), LatLng(0.0,0.0))))
    val coordParts: LiveData<SnapshotStateList<MutableList<LatLng>>> get() = _coordParts

    private val _colorParts = MutableLiveData(mutableListOf(ColorPart()))
    val colorPart: LiveData<MutableList<ColorPart>> get() = _colorParts

    private val _passedRoute = MutableLiveData(mutableStateListOf(0.0))
    val passedRoute: LiveData<SnapshotStateList<Double>> get() = _passedRoute

    /**
     * 경로 이동 진척도를 갱신하는 함수
     * 
     * @param passedRoute 수정할 경로 이동 진척도 
     */
    fun updatePassedRoute(passedRoute:SnapshotStateList<Double>) {
        _passedRoute.postValue(passedRoute)
    }

    /**
     * 사용자가 서버에 저장한 transportRoute json 객체를 파싱하고, StaticValue에 저장하는 함수
     * ※ 이후 SpecificRouteScreen으로 화면을 전환한다.
     *
     * 해당 함수는 사용자가 경로 안내를 시작했다는 알림 받는 함수로, 해당 알림이 오면 [Sherpa 내부 서버]의 navigation.transport_route를 가져온다.
     */
    fun startNavigation() {
        // 이용자가 수정한 transportRoute를 조회한다.
        val responseJson = ReceiveManager().getTransportRoute().data!!

        // 디코딩 작업 (transportRoute는 디코딩되어 전달된다.)
        val json = responseJson
            .substring(1, responseJson.length - 1)
            .chunked(2)
            .map { Integer.parseInt(it, 16).toByte() }
            .toByteArray()
            .toString(Charsets.UTF_8)

        // transportRoute는 interface로 제작되어 있어 추가적인 타입 변환 어댑터가 필요하다.
        val gson = GsonBuilder()
            .registerTypeAdapter(SubPath::class.java, SubPathAdapter())
            .create()

        // 정상화된 json 객체 파싱 진행
        Log.d("API Log", "반환: ${json}")
        val response = gson.fromJson(json, TransportRoute::class.java)

        StaticValue.transportRoute = response
    }

    /**
     * 사용자가 서버에 저장한 ReceiveResponse json 객체를 파싱 후 갱신하는 함수
     * ※ 새로운 경로가 그려지게 된다.
     *
     * 해당 함수는 사용자가 경로 재탐색을 시작했다는 알림 받는 함수로, 해당 알림이 오면 [Sherpa 내부 서버]의 navigation.transport_route를 가져온다.
     */
    fun devateRoute() {
        // 사용자가 수정한 transportRoute를 조회한다.
        val responseJson = ReceiveManager().getRoute().data!!

        // 디코딩 작업 (ReceiveResponse는 디코딩되어 전달된다.)
        val json = responseJson
            .substring(1, responseJson.length - 1)
            .chunked(2)
            .map { Integer.parseInt(it, 16).toByte() }
            .toByteArray()
            .toString(Charsets.UTF_8)

        // 정상화된 json 객체 파싱 진행
        Log.d("API Log", "반환: ${json}")
        val response = Gson().fromJson(json, ReceiveRouteResponse::class.java)

        _coordParts.postValue(response.coordParts)
        _colorParts.postValue(response.colorParts)
    }
}
