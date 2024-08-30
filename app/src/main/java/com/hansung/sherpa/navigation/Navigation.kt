package com.hansung.sherpa.navigation

import android.util.Log
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.itemsetting.RouteFilterMapper
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.transit.TransitManager
import com.hansung.sherpa.transit.odsay.ODsayTransitRouteRequest
import com.naver.maps.geometry.LatLng

/**
 * SearchScreen을 위한 임시 클래스 변형 -2024-08-03
 */
class Navigation {
    val API_LIMIT = 1
    /**
     * getDetailTransitRoutes 참조해서 제작
     *
     */
    fun getDetailTransitRoutes(startLatLng: LatLng, endLatLng: LatLng, depart: String = "", destination: String = ""): List<TransportRoute> {
        val TM = TransitManager()

        // [API] 대중교통+도보 길찾기
        val routeRequest =  setODsayRouteRequest(startLatLng, endLatLng)
        //val routeRequest =  setODsayRouteRequest(tempStartLatLng, tempEndLatLng)
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

        transportRouteList?.mapIndexed { index, transportRoute ->
            // API 횟수 제한
            if(index <= API_LIMIT) {
                // [API] 각 경로에 대한 보행자 경로 리턴
                val pedestrianRouteList = TM.requestCoordinateForRoute(
                    startLatLng, endLatLng, ODsayTransitRouteResponse.result?.path?.get(index)
                )
                Log.i("API", pedestrianRouteList.toString())
                // [MAPPING] 선택한 경로에 대한 데이터를 사용할 클래스 객체에 넣어준다.
                try {
                    RouteFilterMapper().mappingPedestrianRouteToTransportRoute(
                        transportRouteList[index], pedestrianRouteList, depart, destination)
                    Log.i("MAPPER", transportRoute.toString())
                } catch (e : Exception) {
                    Log.e("MAPPER", e.toString())
                }
            } else {
                transportRoute
            }
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