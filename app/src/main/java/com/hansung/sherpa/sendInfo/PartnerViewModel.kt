package com.hansung.sherpa.sendInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng

/**
 * 전체 이용자가 FCM을 통해 '전달받은' 정보들을 관리하는 ViewModel
 * 
 * @property latLng 상대방의 위치
 */
class PartnerViewModel : ViewModel()  {
    private val _latLng = MutableLiveData(LatLng(0.0,0.0))
    val latLng: LiveData<LatLng> get() = _latLng

    /**
     * 상대방 위치를 갱신하는 함수
     *
     * @param latLng 상대방의 위치
     */
    fun updateLatLng(latLng: LatLng) {
        _latLng.postValue(latLng)
    }

    /**
     * 문자열로 받은 좌표를 역직렬화 후
     * [updateLatLng] 에 전달
     */
    fun getLatLng(coordinate: String) {
        val caretakerLatLng = Gson().fromJson(coordinate, LatLng::class.java)
        updateLatLng(caretakerLatLng)
    }
}