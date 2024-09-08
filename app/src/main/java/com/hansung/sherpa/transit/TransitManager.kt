package com.hansung.sherpa.transit

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.convert.Convert
import com.hansung.sherpa.subwayelevator.ElevatorException
import com.hansung.sherpa.subwayelevator.ElevatorLocResponse
import com.hansung.sherpa.subwayelevator.getSubwayElevLocation
import com.hansung.sherpa.transit.odsay.ODsayPath
import com.hansung.sherpa.transit.odsay.ODsayTransitRouteRequest
import com.hansung.sherpa.transit.odsay.ODsayTransitRouteResponse
import com.hansung.sherpa.transit.pedestrian.PedestrianResponse
import com.hansung.sherpa.transit.pedestrian.PedestrianRouteRequest
import com.hansung.sherpa.transit.pedestrian.PedestrianRouteService
import com.hansung.sherpa.transit.osrm.ShortWalkResponse
import com.hansung.sherpa.transit.routegraphic.ODsayGraphicRequest
import com.hansung.sherpa.transit.routegraphic.ODsayMapObject
import com.hansung.sherpa.transit.routegraphic.RouteGraphicResponse
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class TransitManager {
    fun getODsayTransitRoute(routeRequest: ODsayTransitRouteRequest): ODsayTransitRouteResponse? {
        var rr: ODsayTransitRouteResponse? = null
        runBlocking<Job> {
            launch(Dispatchers.IO) {
                try {
                    val response = Retrofit.Builder()
                        .baseUrl("https://api.odsay.com/v1/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create<TransitRouteService?>(TransitRouteService::class.java)
                        .getODsayTransitRoutes(setODsayRequestToMap(routeRequest)).execute()
                    rr = Gson().fromJson(response.body()!!.string(), ODsayTransitRouteResponse::class.java)
                } catch (e: IOException) {
                    Log.e("API Log: IOException", "Transit API Exception ${rr}")
                }
            }
        }
        return rr
    }

    /**
     * 보행자 API를 사용해 경로 데이터를 가져와 역직렬화하는 함수
     *
     * @param routeRequest:PedestrianRouteRequest 요청할 정보 객체
     * @return PedestrianResponse
     */
    fun getPedestrianRoute(routeRequest: PedestrianRouteRequest): PedestrianResponse {
        val appKey = BuildConfig.TMAP_APP_KEY // 앱 키
        lateinit var rr: PedestrianResponse
        runBlocking<Job> {
            launch(Dispatchers.IO) {
                try {
                    Log.d("reqlocation","" + routeRequest.startY +", "+ routeRequest.startX+"    "+routeRequest.endY+", "+routeRequest.endX)
                    val response = Retrofit.Builder()
                        .baseUrl("https://apis.openapi.sk.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create<PedestrianRouteService?>(PedestrianRouteService::class.java)
                        .postPedestrianRoutes(appKey, routeRequest).execute() // API 호출
                    Log.i("API Log: Tmap", "Tmap response: ${response.toString()}")
                    rr = Gson().fromJson(
                        response.body()!!.string(),
                        PedestrianResponse::class.java
                    )
                } catch (e: Exception) {
                    Log.i("API Log: Exception", "postPedestrianRoutes: API Exception(OSRM API 호출)")
                    launch(Dispatchers.IO) {
                        val rQ = routeRequest // 축약
                        try {
                            val options = setOSRMRequestToMap()
                            val response = Retrofit.Builder()
                                .baseUrl("https://routing.openstreetmap.de/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                                .create<TransitRouteService?>(TransitRouteService::class.java)
                                .getOSRMWalk(rQ.startX.toString(),
                                    rQ.startY.toString(), rQ.endX.toString(),
                                    rQ.endY.toString(), options).execute() // API 호출
                            Log.i("API Log: OSRM", "OSRM response: ${response.toString()}")
                            val sW = Gson().fromJson(response.body()!!.string(), ShortWalkResponse::class.java)
                            Log.i("API Log: OSRM", "OSRM item: ${sW.toString()}")
                            rr = Convert().convertToPedestrianResponse(sW)
                        } catch (e: IOException) {
                            Log.e("API Log: IOException", "OSRM API Exception")
                        }
                    }
                }
            }
        }
        return rr
    }

    fun getOsrmPedestrianRoute(routeRequest: PedestrianRouteRequest): PedestrianResponse {
        lateinit var rr: PedestrianResponse
        runBlocking<Job> {
            launch(Dispatchers.IO) {
                val rQ = routeRequest // 축약
                try {
                    val options = setOSRMRequestToMap()
                    val response = Retrofit.Builder()
                        .baseUrl("https://routing.openstreetmap.de/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create<TransitRouteService?>(TransitRouteService::class.java)
                        .getOSRMWalk(rQ.startX.toString(),
                            rQ.startY.toString(), rQ.endX.toString(),
                            rQ.endY.toString(), options).execute() // API 호출
                    Log.i("API Log: OSRM", "OSRM response: ${response.toString()}")
                    val sW = Gson().fromJson(response.body()!!.string(), ShortWalkResponse::class.java)
                    Log.i("API Log: OSRM", "OSRM item: ${sW.toString()}")
                    rr = Convert().convertToPedestrianResponse(sW)
                } catch (e: IOException) {
                    Log.e("API Log: IOException", "OSRM API Exception")
                }
            }
        }
        return rr
    }

    /**
     * 노선 그래픽 데이터를 리턴하는 함수
     *
     * @param routeRequest : ODsayGraphicRequest mapObject를 요청한다.
     * @return ODsayGraphicRequest
     */
    fun getODsayGraphicRoute(request: ODsayGraphicRequest): RouteGraphicResponse? {
        var result: RouteGraphicResponse? = null
        runBlocking {
            launch(Dispatchers.IO) {
                try {
                    val response = Retrofit.Builder()
                        .baseUrl("https://api.odsay.com/v1/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(TransitRouteService::class.java)
                        .getGraphicRoute(request.getQuery()).execute()
                    result = Gson().fromJson(response.body()!!.string(), RouteGraphicResponse::class.java)
                } catch (e: IOException) {
                    Log.e("API Log: IOException", "getODsayGraphicRoute: ${e.message}(e.message)")
                }
            }
        }
        return result
    }

    /**
     * ODsay 대중교통 길찾기 후 대중교통 구간에 대한 좌표 값 받아오는 함수
     *
     * @param response
     * @return
     */
    fun requestCoordinateForMapObject(response: ODsayTransitRouteResponse): List<RouteGraphicResponse> {
        if (response.result?.path == null) return emptyList()
        val mapObjectList: List<String> = response.result.path.map { it.info.mapObj ?: "" }
        val coordinateList = MutableList<RouteGraphicResponse?>(mapObjectList.size) { null }
        runBlocking {
            val jobs = mapObjectList.mapIndexed { index, mapObject -> // 순서대로 다시 정렬하기 위함
                launch(Dispatchers.IO) {
                    if (mapObject == "") {
                        coordinateList[index] = null
                    } else {
                        val routeGraphicResponse = getODsayGraphicRoute(
                            ODsayGraphicRequest(mapObject = ODsayMapObject(responseMapObject = mapObject))
                        )
                        if (routeGraphicResponse != null) {
                            coordinateList[index] = routeGraphicResponse
                        }
                    }
                }
            }
            jobs.forEach { it.join() } // 비동기 요청 완료 대기
        }

        return coordinateList.filterNotNull()
    }


    /**
     * ODsay 대중교통 길찾기 후 대중교통 구간에 대한 좌표 값 받아오는 함수
     *
     * @param response
     * @return
     */
    fun requestCoordinateForRoute(start: LatLng, end: LatLng, response: ODsayPath?): List<PedestrianResponse> {
        var pedestrianResponse: PedestrianResponse = PedestrianResponse()
        if(response == null){
            runBlocking {
                val pedestrianRouteRequest = PedestrianRouteRequest(
                    startX = start.longitude.toFloat(),
                    startY = start.latitude.toFloat(),
                    endX = end.longitude.toFloat(),
                    endY = end.latitude.toFloat(),
                )
                val job =
                    launch(Dispatchers.IO) {
                        pedestrianResponse = getPedestrianRoute(pedestrianRouteRequest)
                    }
                job.join() // 비동기 요청 완료 대기
            }

            return listOfNotNull(pedestrianResponse)
        }
        val PEDESTRINAN_CODE = 3 // trafficType이 3일때
        val FIRST_INDEX = 0 // 경로에서 첫 번째 구간이 도보일 때
        val LAST_INDEX = response.subPath.size - 1 // 경로에서 마지막 구간이 도보일 떄

        val routeCoordinateList: MutableList<PedestrianRouteRequest> = mutableListOf()

        var tmp:PedestrianRouteRequest = PedestrianRouteRequest(0.0f,0.0f,0.0f,0.0f)

        var targetSX:Float = 0.0f
        var targetSY:Float = 0.0f
        var targetEX:Float = 0.0f
        var targetEY:Float = 0.0f

        var ELEVATOR_PRIMARY_SETTING = StaticValue.userInfo.userSetting!!.elevatorFirst // 지하철 우선 안내 ON/OFF

        response.subPath.forEachIndexed { index, it ->
            if (it.trafficType == PEDESTRINAN_CODE) {
                routeCoordinateList.add(
                    when (index) {
                        FIRST_INDEX -> {
                            try {
                                if(ELEVATOR_PRIMARY_SETTING == true && response.subPath[index+1].trafficType==1) {

                                    var elevatorLocation = getSubwayElevLocation(response.subPath[index+1].startName)

                                    var minLoc = findMinDistanceLatLng(elevatorLocation, LatLng(response.subPath[index+1].startY, response.subPath[index+1].startX))

                                    tmp = PedestrianRouteRequest(
                                        startX = start.longitude.toFloat(),
                                        startY = start.latitude.toFloat(),
                                        endX = minLoc.longitude.toFloat(),
                                        endY = minLoc.latitude.toFloat()
                                    )

                                }
                                else{
                                    throw ElevatorException("Hold")
                                }
                            }catch (e: ElevatorException){
                                Log.d("ElevatorConvertError",e.msg)
                                tmp = PedestrianRouteRequest(
                                    startX = start.longitude.toFloat(),
                                    startY = start.latitude.toFloat(),
                                    endX = response.subPath[FIRST_INDEX + 1].startX.toFloat(),
                                    endY = response.subPath[FIRST_INDEX + 1].startY.toFloat()
                                )
                            }

                            tmp
                        }
                        LAST_INDEX -> {
                            try {
                                if(ELEVATOR_PRIMARY_SETTING == true && response.subPath[index-1].trafficType==1){
                                    var elevatorLocation = getSubwayElevLocation(response.subPath[index-1].endName)

                                    var minLoc = findMinDistanceLatLng(elevatorLocation, LatLng(end.latitude, end.longitude))

                                    tmp = PedestrianRouteRequest(
                                        startX = minLoc.longitude.toFloat(),
                                        startY = minLoc.latitude.toFloat(),
                                        endX = end.longitude.toFloat(),
                                        endY = end.latitude.toFloat()
                                    )

                                }
                                else{
                                    throw ElevatorException("Hold")
                                }
                            }catch (e: ElevatorException) {
                                Log.d("ElevatorConvertError", e.msg)
                                tmp = PedestrianRouteRequest(
                                    startX = response.subPath[LAST_INDEX - 1].endX.toFloat(),
                                    startY = response.subPath[LAST_INDEX - 1].endY.toFloat(),
                                    endX = end.longitude.toFloat(),
                                    endY = end.latitude.toFloat()
                                )
                            }

                            tmp
                        }

                        else -> {
                            targetSX = response.subPath[index - 1].endX.toFloat()
                            targetSY = response.subPath[index - 1].endY.toFloat()
                            targetEX = response.subPath[index + 1].startX.toFloat()
                            targetEY = response.subPath[index + 1].startY.toFloat()

                            var beforeTransit = response.subPath[index-1]
                            var afterTransit = response.subPath[index+1]

                            if(ELEVATOR_PRIMARY_SETTING==true){
                                if(beforeTransit.trafficType==1) {
                                    try {
                                        var elevLocation = getSubwayElevLocation(beforeTransit.endName)
                                        var minLoc = findMinDistanceLatLng(elevLocation, LatLng(afterTransit.startY, afterTransit.startX))

                                        targetSX = minLoc.longitude.toFloat()
                                        targetSY = minLoc.latitude.toFloat()
                                    } catch (e: ElevatorException){
                                        Log.d("ElevatorConvertError", e.msg)
                                    }
                                }

                                if(afterTransit.trafficType==1) {
                                    try {
                                        var elevatorLocation = getSubwayElevLocation(afterTransit.startName)
                                        var minLoc = findMinDistanceLatLng(elevatorLocation, LatLng(beforeTransit.endY, beforeTransit.endX))

                                        targetEX = minLoc.longitude.toFloat()
                                        targetEY = minLoc.latitude.toFloat()
                                    } catch (e: ElevatorException){
                                        Log.d("ElevatorConvertError", e.msg)
                                    }
                                }
                            }

                            PedestrianRouteRequest (
                                startX = targetSX,
                                startY = targetSY,
                                endX = targetEX,
                                endY = targetEY
                            )

                        }
                    }
                )
            }
        }
        val coordinateList = MutableList<PedestrianResponse?>(routeCoordinateList.size) { null } // 결과 저장하는 리스트
        runBlocking {
            val jobs = routeCoordinateList.mapIndexed { index, pedestrianRouteRequest -> // 순서대로 다시 정렬하기 위함
                launch(Dispatchers.IO) {
                    val pedestrianResponse = getPedestrianRoute(pedestrianRouteRequest)
                    coordinateList[index] = pedestrianResponse
                }
            }
            jobs.forEach { it.join() } // 비동기 요청 완료 대기
        }

        return coordinateList.filterNotNull()
    }



    private fun setODsayRequestToMap(request: ODsayTransitRouteRequest): Map<String, String> {
        return mapOf(
            "apiKey" to request.apiKey,
            "SX" to request.SX,
            "SY" to request.SY,
            "EX" to request.EX,
            "EY" to request.EY,
            "OPT" to request.OPT,
            "SearchType" to request.SearchType,
            "SearchPathType" to request.SearchPathType
        )
    }

    /**
     * OSRM 쿼리스트링 매핑 함수:
     * - 각 매개변수에 대한 상세 설명을 포함합니다.
     *
     * 설정 옵션:
     * - `alternatives`: 대체 경로의 수 (false, true, number)
     * - `steps`: 경로의 세부 단계 포함 여부 (false, true)
     * - `annotations`: 경로의 각 구간에 대한 세부 정보 (false, true, nodes, distance, duration, datasource, weight, speed)
     * - `geometries`: 경로선을 그리는 데이터 유형 (polyline, polyline6, geojson)
     * - `overview`: 경로 전체 경로선의 세부 수준 (simplified, full, false)
     *
     * [OSRM 공식문서](https://project-osrm.org/docs/v5.24.0/api/#route-service)
     * @return OSRM API에 전달될 설정이 포함된 Map<String, String>
     */
    private fun setOSRMRequestToMap(): Map<String, String> {
        /**
         * 대체 경로의 수
         * - false (기본) : 기본 경로 1개
         * - true : 대체 경로를 모두 요청
         * - number : number개의 대체 경로 수를 요청 ex) "2"이면 최대 3개 까지의 경로를 요청 받게 된다.
         */
        val alternatives = "true"

        /**
         *  각 경로 구간에 대한 이동 단계 :
         *  - false (기본) : 요청하지 않음
         *  - true : 요청
         */
        val steps = "true"

        /**
         *  각 geometry 마다의 세부 정보 :
         *  - false (기본) : 요청하지 않음
         *  - true : 세부 내용 전부 요청
         *  - nodes : 노드 정보만 요청
         *  - distance : 거리 정보만 요청
         *  - duration : 이동 시간만 요청
         *  - datasource : TODO 데이터 분석 필요
         *  - weight : TODO 데이터 분석 필요
         *  - speed : 보행자 이동 속도
         */
        val annotations = "false"

        /**
         * 경로선을 그리는 데이터 유형 (경로 전체, 각 구간별 포함) :
         * - polyline (기본) : 정밀도 5의 폴리라인 데이터 제공
         * - polyline6 : 정밀도 6의 폴리라인 데이터 제공
         * - geojson : json 값으로 좌표 리스트를 제공
         *
         * 폴리라인 라이브러리 : [mapbox/polyline](https://www.npmjs.com/package/polyline)
         */
        val geometries = "geojson"

        /**
         * 경로 전체 경로선을 보여주는 정도 :
         * - simplified (기본) : 가장 높은 줌 레벨에서 보여주는 경로
         * - full : 전체 상세 경로
         * - false : 요청하지 않음
         */
        val overview = "full"
        return mapOf(
            "alternatives" to alternatives,
            "steps" to steps,
            "annotations" to annotations,
            "geometries" to geometries,
            "overview" to overview,
        )
    }

    fun findMinDistanceLatLng(elevatorLocation: ElevatorLocResponse, pedestrianLoc: LatLng):LatLng{
        Log.d("minDist","pede : " + pedestrianLoc.latitude + "," + pedestrianLoc.longitude + "\n")

        var target:LatLng
        lateinit var ret:LatLng
        var mindist = 100000000000.0

        for (i in elevatorLocation.data){
            var slicing = i.location.split(",")
            var lat = slicing[0].toDouble()
            var lng = slicing[1].toDouble()

            target = LatLng(lat, lng)

            if(target.distanceTo(pedestrianLoc)<mindist){
                mindist = target.distanceTo(pedestrianLoc)
                ret = target
            }
        }

        Log.d("minDist", "ret : " + ret.latitude + ", " + ret.longitude)

        return ret
    }
}