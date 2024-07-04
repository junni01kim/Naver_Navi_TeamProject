package com.hansung.sherpa.convert

import android.util.Log
import com.hansung.sherpa.transit.Leg
import com.hansung.sherpa.transit.TmapTransitRouteResponse
import com.hansung.sherpa.transit.PedestrianResponse
import com.naver.maps.geometry.LatLng

/**
 * API로 받아온 데이터 객체를 사용할 객체로 변환해주는 클래스
 *
 * @since 2024-05-27
 * @author HS-JNYLee
 *
 * @property routeMutableLists 여러 경로의 데이터를 가지고 있는 프로퍼티
 */
class Convert {
    private val routeMutableLists = mutableListOf<MutableList<LegRoute>>()

    /**
     * API reponse 값을 경로 리스트로 바꿔주는 함수
     * @param response API로 받아온 데이터 객체
     *
     * @return  MutableList<MutableList<Route>>
     */
    fun convertToRouteMutableLists(response: TmapTransitRouteResponse): MutableList<MutableList<LegRoute>> {
        response.metaData?.plan?.itineraries?.forEach {
            val legRouteList = mutableListOf<LegRoute>()                         // 1개의 경로를 담는 리스트
            it.legs?.forEach { leg -> legRouteList.add(navigateRouteType(leg)) } // 구간별 값 추가
            routeMutableLists.add(legRouteList) // 완성된 경로 저장
        }
        return routeMutableLists
    }

    /**
     * 각 구간별 이동수단 타입에 따라 구분해주는 함수
     * @param leg 구간의 정보
     *
     * @return LegRoute
     */
    private fun navigateRouteType(leg: Leg): LegRoute {
        return when (leg.mode) {
            "WALK" -> getWalkRoute(leg)
            "SUBWAY" -> getSubwayRoute(leg)
            "BUS" -> getBusRoute(leg)
            "EXPRESSBUS" -> getExpressBusRoute(leg)
            "TRAIN" -> getTrainRoute(leg)
            else -> LegRoute()
        }
    }

    /**
     * 문자열 좌표 값을 좌표 객체로 바꾸는 함수
     * @param lineString 해당 구간의 경로 좌표 값
     *
     * @return MutableList
     */
    private fun convertToCoordsList(lineString: String): MutableList<Coordinate> {
        val coordinateList = mutableListOf<Coordinate>()
        lineString.split(" ")               // "111,222 333,444" -> ["111,222", "333,444"]
            .map { it.split(",") }          // ["111,222", "333,444"] -> ["111", "222"], ["333", "444"]
            .map { (lon, lat) -> lat to lon }   // ["111", "222"], ["333", "444"] -> [("111", "222"), ("333", "444")]
            .forEach {
                coordinateList.add(
                    Coordinate(
                        it.first.toDouble(),             // latitude
                        it.second.toDouble()            // longitude
                    )
                )
            }
        return coordinateList
    }

    /**
     * 도보 경로를 저장하는 함수
     * @param leg 구간의 정보
     *
     * @return Route
     */
    private fun getWalkRoute(leg: Leg): LegRoute {
        val legRoute = LegRoute()
        legRoute.coordinates.add(Coordinate(leg.start.lat, leg.start.lon))
        if (leg.steps != null) {
            legRoute.pathType = PathType.WALK
            leg.steps.forEach { step ->
                legRoute.coordinates.addAll(convertToCoordsList(step.linestring))
            }
        }
        legRoute.coordinates.add(Coordinate(leg.end.lat, leg.end.lon))
        return legRoute
    }


    /**
     * 지하철, 버스, 고속버스, 기차의 경로 그리는 공통 함수
     * @param leg 구간의 정보
     *
     * @return Route
     */
    private fun getTransportationRoute(leg: Leg): LegRoute {
        val legRoute = LegRoute()
        legRoute.coordinates.add(Coordinate(leg.start.lat, leg.start.lon))
        if (leg.passShape != null) {
            legRoute.coordinates.addAll(convertToCoordsList(leg.passShape.linestring!!))
        }
        legRoute.coordinates.add(Coordinate(leg.end.lat, leg.end.lon))
        return legRoute
    }

    /**
     * 버스 경로를 저장하는 함수
     * @param leg 구간의 정보
     *
     * @return Route
     */
    private fun getBusRoute(leg: Leg): LegRoute {
        return getTransportationRoute(leg).apply { pathType = PathType.BUS }
    }

    /**
     * 고속버스 경로를 저장하는 함수
     * @param leg 구간의 정보
     *
     * @return Route
     */
    private fun getExpressBusRoute(leg: Leg): LegRoute {
        return getTransportationRoute(leg).apply { pathType = PathType.EXPRESSBUS }
    }

    /**
     * 지하철 경로를 저장하는 함수
     * @param leg 구간의 정보
     *
     * @return Route
     */
    private fun getSubwayRoute(leg: Leg): LegRoute {
        return getTransportationRoute(leg).apply { pathType = PathType.SUBWAY }
    }

    /**
     * 기차 경로를 저장하는 함수
     * @param leg 구간의 정보
     *
     * @return Route
     */
    private fun getTrainRoute(leg: Leg): LegRoute {
        return getTransportationRoute(leg).apply { pathType = PathType.TRAIN }
    }

    /**
     * 정제된 경로 데이터 중 1개의 경로를 SearchRoute의 네이버 data class로 바꿔주는 함수
     *  LatLng, PathType으로 구성됨.
     *
     *  @param routeMutableList 1개의 경로 데이터
     *  @return MutableList
     */
    fun convertLegRouteToLatLng(routeMutableList: MutableList<LegRoute>): MutableList<LatLng> {
        val searchRouteMutableList = mutableListOf<LatLng>()
        routeMutableList.forEach { legRoute ->              // 구간별 경로 추출
            legRoute.coordinates.forEach { coordinate ->    // 경로의 좌표 값 추출
                searchRouteMutableList.add(
                        LatLng(
                            coordinate.latitude,            // 위도
                            coordinate.longitude            // 경도
                        )
                )
            }
        }
        Log.d("testPrint","temporateClass로 변경 성공!")
        return searchRouteMutableList
    }

    fun convertPedestrianRouteToLatLng(response:PedestrianResponse, from:LatLng, to:LatLng):MutableList<LatLng>{
        var rres = mutableListOf<LatLng>()

        response.features?.forEach {feature->
            if(feature.geometry.type=="LineString"){
                feature.geometry.coordinates.forEach {coordinates->
                    var tmp = coordinates.toString().split(", ")
                    rres.add(LatLng(tmp[1].replace("]","").toDouble(), tmp[0].replace("[","").toDouble()))
                }
            }
        }

        rres = rres.distinct().toMutableList()

        rres.add(0,from)
        rres.add(to)

        return rres
    }

    /**
     *  Coordinate 리스트를 LatLng 리스트로 변환하는 함수
     *
     *  @param coordinates 공용 좌표 타입
     *  @return List
     */
    fun convertCoordinateToLatLng(coordinates: MutableList<Coordinate>): List<LatLng> {
        return coordinates.map { LatLng(it.latitude, it.longitude) }
    }



}