package com.hansung.sherpa.sendInfo

import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.navigation.Navigation
import com.naver.maps.geometry.LatLng

class ReceiveManager {
    /**
     * 일정 시간이 되었을 때 실행 될 함수
     */
    fun scheduleStart(title: String, body: String) {
        // TODO: 1. body 데이터 파싱
        // TODO: 2. 일정 정보 다이얼로그 띄우기
        // TODO: 3. 휴대폰 팝업 메세지 띄우기
    }

    /**
     * 경로 안내 시간이 되었을 때 실행 될 함수
     */
    fun navigationStart(title: String, body: String) {
        // TODO: 1. body에서 목적지 받아오기
        val destinationLatLng = LatLng(37.1115, 127.0106) // 임시
        StaticValue.transportRoute = Navigation().getDetailTransitRoutes(LatLng(0.0,0.0),destinationLatLng)[0]
        // TODO: 2. 경로 안내 시작에 대한 다이얼로그 띄우기

        // TODO: 3. 화면 이동
        //navController?.navigate(SherpaScreen.SpecificRoute.name)
    }
}