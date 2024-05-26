package com.hansung.sherpa.location

import android.util.Log
import com.hansung.sherpa.geocoding.GeocodingAPI
import com.naver.maps.geometry.LatLng

class SearchLocation {
    // GeocodingAPI 클래스를 이용하여 원하는 주소의 좌표를 받아오는 함수이다.
    fun searchLatLng(destination:String): LatLng {
        val geoCodingApi = GeocodingAPI()
        geoCodingApi.request(destination,"목적지")

        // 여기서 무한루프 발생!!
        while(true) {
            if(geoCodingApi.getFlag("목적지")==false){
                break;
            }
            Log.d("getLatLng", "탐색중")
        }

        val coordinate = geoCodingApi.getCoordinate("목적지")
        //val latLng = LatLng(coordinate!!.latitude, coordinate.longitude)
        val latLng = LatLng(0.0, 0.0)
        return latLng
    }
}