package com.hansung.sherpa.sendInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.itemsetting.Info
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.navigation.Navigation

class CaretakerViewModel : ViewModel() {
    private val _transportRoute = MutableLiveData(TransportRoute(Info(0.0,0,0,0), listOf()))
    val transportRoute: LiveData<TransportRoute> get() = _transportRoute

    fun updateTransportRoute(transportRoute: TransportRoute) {
        _transportRoute.postValue(transportRoute)
        //navigate()
    }

    fun receivePos(title: String, body: String) {
        if(StaticValue.myPos != null){
            val transportRoutes = Navigation().getDetailTransitRoutes(
                com.naver.maps.geometry.LatLng(StaticValue.myPos!!.latitude, StaticValue.myPos!!.longitude),
                com.naver.maps.geometry.LatLng(0.0,0.0),
                "", "")
            StaticValue.transportRoute = transportRoutes[0]

        }
        // TODO: 출 -> 목 경로 탐색
        /*
            1. 첫 번째 인덱스 경로 가져옴
            2. StaticValue 경로 값에 저장
            3. 경로 안내 팝업 띄우기
            4. Navigation specific route로 옮기기
        */
    }
}
