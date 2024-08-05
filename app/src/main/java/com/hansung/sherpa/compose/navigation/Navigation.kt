package com.hansung.sherpa.compose.navigation

import android.util.Log
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.itemsetting.RouteFilterMapper
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.compose.transit.TransitManager
import com.hansung.sherpa.transit.ODsayTransitRouteRequest
import com.naver.maps.geometry.LatLng

/**
 * SearchScreen을 위한 임시 클래스 변형 -2024-08-03
 */
class Navigation {
    // 반드시 지울 것!! 좌표 찾기 대신 넣는 임시 값
    // [개발]: 시작, 도착 좌표
    var tempStartLatLng = LatLng(37.642743, 126.835375)
    val tempEndLatLng = LatLng(37.627444, 126.829600)

    /**
     * getDetailTransitRoutes 참조해서 제작
     *
     */
    fun getDetailTransitRoutes(startLatLng: LatLng, endLatLng: LatLng): List<TransportRoute> {
        val TM = TransitManager()

        // [API] 대중교통+도보 길찾기
        val routeRequest =  setODsayRouteRequest(startLatLng, endLatLng)
        val ODsayTransitRouteResponse = TM.getODsayTransitRoute(routeRequest)
        Log.i("API", ODsayTransitRouteResponse.toString())

        // [API] 노선 그래픽 : 대중교통 경로 좌표 찾기
        val routeGraphicList = TM.requestCoordinateForMapObject(ODsayTransitRouteResponse!!)
        Log.i("API", routeGraphicList.toString())

        // [MAPPING] 하드코딩 매핑 TransportRouteList
        var transportRouteList: List<TransportRoute>? = emptyList()
        try {
            transportRouteList = RouteFilterMapper().mappingODsayResponseToTransportRouteList(ODsayTransitRouteResponse, routeGraphicList)
            Log.i("MAPPER", transportRouteList.toString())
        } catch (e: Exception) {
            Log.e("MAPPER", e.toString())
        }

        return transportRouteList!!
    }

    private fun setODsayRouteRequest(startLatLng: LatLng, endLatLng: LatLng): ODsayTransitRouteRequest {
        return ODsayTransitRouteRequest(
            apiKey = BuildConfig.ODSAY_APP_KEY,
            SX = startLatLng.longitude.toString(),
            SY = startLatLng.latitude.toString(),
            EX = endLatLng.longitude.toString(),
            EY = endLatLng.latitude.toString()
        )
    }
}