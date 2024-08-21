package com.hansung.sherpa.convert

import android.util.Log
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.transit.Leg
import com.hansung.sherpa.transit.ODsayTransitRouteRequest
import com.hansung.sherpa.transit.TmapTransitRouteRequest
import com.hansung.sherpa.transit.TmapTransitRouteResponse
import com.hansung.sherpa.transit.PedestrianResponse
import com.hansung.sherpa.transit.ShortWalkResponse
import com.hansung.sherpa.transit.Steps
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

    fun convertTmapToODsayRequest(t: TmapTransitRouteRequest): ODsayTransitRouteRequest{
        return ODsayTransitRouteRequest(SX = t.startX, SY = t.startY, EX = t.endX, EY = t.endY, apiKey = BuildConfig.ODSAY_APP_KEY)
    }

    // ShortWalkResponse 데이터 클래스를 PedestrianResponse로 변환하는 함수 TODO 임시로 바꿔줌
    fun convertToPedestrianResponse(shortWalkResponse: ShortWalkResponse): PedestrianResponse {
        // Convert Routes to Feature
        val features = shortWalkResponse.routes[0].legs.flatMap { leg ->
                leg.steps.mapIndexed { index, step ->
                    PedestrianResponse.Feature(
                        geometry = PedestrianResponse.Feature.Geometry(
                            coordinates = step.geometry?.coordinates?.map { coordinatePair ->
                                coordinatePair // Ensure this matches the expected type (List<Any>)
                            } ?: emptyList(),
                            type = step.geometry?.type ?: ""
                        ),
                        properties = PedestrianResponse.Feature.Properties(
                            categoryRoadType = 0, // Example value, adjust as needed
                            description = convertStepFieldsToDescription(step) ?: "",
                            direction = step.maneuver?.type ?: "",
                            distance = leg.distance?.toInt() ?: 0,
                            facilityName = step.name ?: "", // Example value, adjust as needed
                            facilityType = "", // Example value, adjust as needed
                            index = index,
                            intersectionName = "", // Example value, adjust as needed
                            lineIndex = 0, // Example value, adjust as needed
                            name = step.name ?: "",
                            nearPoiName = "", // Example value, adjust as needed
                            nearPoiX = "", // Example value, adjust as needed
                            nearPoiY = "", // Example value, adjust as needed
                            pointIndex = index,
                            pointType = "", // Example value, adjust as needed
                            roadType = 0, // Example value, adjust as needed
                            time = step.duration?.toInt() ?: 0,
                            totalDistance = leg.distance?.toInt() ?: 0,
                            totalTime = leg.duration?.toInt() ?: 0,
                            turnType = 0
                        ),
                        type = "Feature"
                    )
                }
        }
        // Create PedestrianResponse
        return PedestrianResponse(
            features = features,
            type = "FeatureCollection" // Adjust the type as needed
        )
    }

    /**
     * 보행자 안내 문구 만들어 주는 함수 
     *
     * @param step
     * @return
     */
    fun convertStepFieldsToDescription(step: Steps): String {
        val maneuver = step.maneuver
        val distance = step.distance?.toInt() ?: 0
        val roadName = step.name ?: "도로"

        val maneuverType = maneuver?.type ?: ""
        val bearingBefore = maneuver?.bearingBefore ?: 0
        val bearingAfter = maneuver?.bearingAfter ?: 0
        val modifier = maneuver?.modifier ?: ""
        val direction = when (modifier) {
            "uturn" -> "U턴해서 "
            "sharp right" -> "급하게 우측으로 "
            "right" -> "우측으로 "
            "slight right" -> "조금 우측으로 "
            "straight" -> "직진으로 "
            "slight left" -> "조금 좌측으로 "
            "left" -> "좌측으로 "
            "sharp left" -> "급하게 좌측으로 "
            else -> ""
        }

        val directionDescription = when (maneuverType) {
            "depart" -> "출발지에서 "
            "arrive" -> "목적지까지 "
            "merge" -> "도로에 합류하여 "
            "on ramp" -> "램프를 타고 고속도로로 들어가 "
            "off ramp" -> "램프를 타고 고속도로에서 나가 "
            "fork" -> "갈림길에서 "
            "end of road" -> "도로 끝에서 "
            "use lane" -> "차선을 이용하여 "
            "continue" -> "계속 직진해서 "
            "roundabout turn" -> "회전교차로에서 돌아서 "
            "notification" -> "[변경 사항]"
            else -> "현재 위치에서 "
        }

        val instructionText =  if (distance > 0) {
            "${directionDescription}${direction}${distance}미터 이동합니다."
        } else {
            "${directionDescription}${direction}이동합니다."
        }
        Log.i("API", instructionText)
        return instructionText
    }
}