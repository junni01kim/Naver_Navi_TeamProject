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
 * @property receiveManager 메모리 관리를 위한 단일 ReceiveManager 생성 
 * @property coordParts 사용자가 경로 안내를 진행하는 경로 리스트
 * @property colorPart 사용자가 이동하는 경로들의 이동수단 색상 (도보는 black)
 * @property passedRoute 사용자의 경로 이동 진척도
 */
class CaregiverViewModel : ViewModel()  {
    private val receiveManager = ReceiveManager()
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
     * 예외처리: [Sherpa 내부 서버]에서 transportRoute가 조회 완료된 경우에만 화면 이동
     */
    fun startNavigation():TransportRoute? {
        // 이용자가 수정한 transportRoute를 조회한다.
        val response = receiveManager.getTransportRoute()

        // TODO: 예외 추가할 것 (알림 달기)
        if(response.code == 200) {
            return receiveManager.fromTransportRouteJson(response.data!!)
        }
        return null
    }

    /**
     * 사용자가 서버에 저장한 ReceiveResponse json 객체를 파싱 후 갱신하는 함수
     * ※ 새로운 경로가 그려지게 된다.
     *
     * 해당 함수는 사용자가 경로 재탐색을 시작했다는 알림 받는 함수로, 해당 알림이 오면 [Sherpa 내부 서버]의 navigation.transport_route를 가져온다.
     */
    fun devateRoute() {
        // 사용자가 수정한 transportRoute를 조회한다.
        val response = receiveManager.getRoute()

        // 예외처리: [Sherpa 내부 서버]에서 ReceiveRouteResponse가 조회 완료된 경우에만 화면 이동
        // TODO: 예외 추가할 것 (알림 달기)
        if(response.code == 200) {
            val receiveRouteResponse = receiveManager.fromReceiveRouteResponse(response.data!!)
            _coordParts.postValue(receiveRouteResponse.coordParts)
            _colorParts.postValue(receiveRouteResponse.colorParts)
        }
    }
}
