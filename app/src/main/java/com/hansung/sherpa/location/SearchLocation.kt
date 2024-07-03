package com.hansung.sherpa.location

import android.util.Log
import com.hansung.sherpa.convert.Coordinate
import com.hansung.sherpa.geocoding.GeocodingAPI
import com.hansung.sherpa.geocoding.GeocodingAPICallBack
import com.naver.maps.geometry.LatLng

class SearchLocation {
    // GeocodingAPI 클래스를 이용하여 원하는 주소의 좌표를 받아오는 함수이다.
    fun searchLatLng(destination:String): LatLng {
        val geoCodingApi = GeocodingAPI()
        val coordinate : Coordinate
        //geoCodingApi.request(destination,"목적지")
        
        
        geoCodingApi.request(destination, object : GeocodingAPICallBack{
            override fun onSuccess(coord: List<Coordinate>) {
                // TODO: Coordinate 처리
            }

            override fun onFailure(message: String) {
                // TODO: 실패 시 처리 
            }
        })
        
        
        // 여기서 무한루프 발생!!
        //val latLng = LatLng(coordinate!!.latitude, coordinate.longitude)
        val latLng = LatLng(0.0, 0.0)
        return latLng
    }
}