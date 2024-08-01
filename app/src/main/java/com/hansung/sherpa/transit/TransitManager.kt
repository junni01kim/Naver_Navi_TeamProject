package com.hansung.sherpa.transit

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.R
import com.hansung.sherpa.convert.Convert
import com.hansung.sherpa.routegraphic.RouteGraphicResponse
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 * 교통수단 API를 관리하는 클래스
 *
 * @since 2024-05-09
 * @author HS-JNYLee
 *
 * @property context Activity의 context
 * @sample TransitManager.sampleGetTransitRoutes
 */
class TransitManager(context: Context) {

    val context: Context = context

    /**
     * 교통수단 API를 사용해 경로 데이터를 가져와 역직렬화하는 함수
     *
     * @param routeRequest 요청할 정보 객체
     * @return LiveData<TransitRouteResponse>
     */
    fun getTransitRoutes(routeRequest: TmapTransitRouteRequest): LiveData<TmapTransitRouteResponse> {
        val resultLiveData = MutableLiveData<TmapTransitRouteResponse>()
        val appKey = BuildConfig.TMAP_APP_KEY // 앱 키
        Retrofit.Builder()
            .baseUrl(context.getString(R.string.route_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(TransitRouteService::class.java).postTransitRoutes(appKey, routeRequest) // API 호출
            .enqueue(object : Callback<ResponseBody> {
                // 성공시 콜백
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        // Deserialized 역직렬화
                        val tmapTransitRouteResponse = Gson().fromJson(responseBody.string(), TmapTransitRouteResponse::class.java)
                        // post to livedata (Change Notification) 변경된 값을 알림
                        resultLiveData.postValue(tmapTransitRouteResponse)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
            })
        return resultLiveData
    }

    /**
     * 교통수단 API를 사용해 경로 데이터를 가져와 역직렬화하는 함수
     *
     * @param routeRequest 요청할 정보 객체
     * @return TransitRouteResponse
     */
    fun getTmapTransitRoutes(routeRequest: TmapTransitRouteRequest): TmapTransitRouteResponse {
        val appKey = BuildConfig.TMAP_APP_KEY // 앱 키
        lateinit var rr: TmapTransitRouteResponse
        runBlocking<Job> {
            launch(Dispatchers.IO) {
                try {
                    val response = Retrofit.Builder()
                        .baseUrl(context.getString(R.string.route_base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create<TransitRouteService?>(TransitRouteService::class.java)
                        .postTransitRoutes(appKey, routeRequest).execute() // API 호출
                    rr = Gson().fromJson(response.body()!!.string(), TmapTransitRouteResponse::class.java)
                    // Error Log
                    /*if (rr.metaData == null) {
                        val errorCode = Gson().fromJson(response.body()!!.string(), TmapTransitErrorCode::class.java)
                        Log.e("Error", "Error Code: ${errorCode.result?.status}, ${errorCode.result?.message}")
                        // getOdsayTransitRoute(Convert().convertTmapToOdsayRequest(routeRequest))
                    }*/
                } catch (e: IOException) {
                    Log.e("Error", "Transit API Exception")
                    rr = TmapTransitRouteResponse()
                }
            }
        }
        return rr
    }

    fun getODsayTransitRoute(routeRequest: ODsayTransitRouteRequest): ODsayTransitRouteResponse? {
        var rr: ODsayTransitRouteResponse? = null
        runBlocking<Job> {
            launch(Dispatchers.IO) {
                try {
                    val response = Retrofit.Builder()
                        .baseUrl(context.getString(R.string.odsay_route_base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create<TransitRouteService?>(TransitRouteService::class.java)
                        .getODsayTransitRoutes(setODsayRequestToMap(routeRequest)).execute()
                    rr = Gson().fromJson(response.body()!!.string(), ODsayTransitRouteResponse::class.java)
                } catch (e: IOException) {
                    Log.e("Error", "Transit API Exception ${rr}")
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
                        .baseUrl(context.getString(R.string.route_base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create<PedestrianRouteService?>(PedestrianRouteService::class.java)
                        .postPedestrianRoutes(appKey, routeRequest).execute() // API 호출
                    rr = Gson().fromJson(
                        response.body()!!.string(),
                        PedestrianResponse::class.java
                    )
                } catch (e: IOException) {
                    Log.i("Error", "postPedestrianRoutes API Exception")
                    launch(Dispatchers.IO) {
                        val rQ = routeRequest // 축약
                        try {
                            val options = setOSRMRequestToMap()
                            val response = Retrofit.Builder()
                                .baseUrl(context.getString(R.string.osrm_route_base_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                                .create<TransitRouteService?>(TransitRouteService::class.java)
                                .getOSRMWalk(rQ.startX.toString(),
                                    rQ.startY.toString(), rQ.endX.toString(),
                                    rQ.endY.toString(), options).execute() // API 호출
                            val sW = Gson().fromJson(response.body()!!.string(), ShortWalkResponse::class.java)
                            Log.i("item", sW.toString())
                            rr = Convert().convertToPedestrianResponse(sW)
                        } catch (e: IOException) {
                            Log.e("Error", "OSRM API Exception")
                        }
                    }
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
                        .baseUrl(context.getString(R.string.odsay_route_base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(TransitRouteService::class.java)
                        .getGraphicRoute(request.getQuery()).execute()
                    result = Gson().fromJson(response.body()!!.string(), RouteGraphicResponse::class.java)
                } catch (e: IOException) {
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
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
        val mapObjectList: List<String> = response.result.path.map { it.info.mapObj }
        val coordinateList = MutableList<RouteGraphicResponse?>(mapObjectList.size) { null }
        runBlocking {
            val jobs = mapObjectList.mapIndexed { index, mapObject -> // 순서대로 다시 정렬하기 위함
                launch(Dispatchers.IO) {
                    val routeGraphicResponse = getODsayGraphicRoute(
                        ODsayGraphicRequest(mapObject = ODsayMapObject(responseMapObject = mapObject))
                    )
                    if (routeGraphicResponse != null) {
                        coordinateList[index] = routeGraphicResponse
                    }
                }
            }
            jobs.forEach { it.join() } // 비동기 요청 완료 대기
        }

        return coordinateList.filterNotNull()
    }

    enum class Coordinates(val SX: Int, val y: Int)
    /**
     * ODsay 대중교통 길찾기 후 대중교통 구간에 대한 좌표 값 받아오는 함수
     *
     * @param response
     * @return
     */
    fun requestCoordinateForRoute(start: LatLng, end: LatLng,response: ODsayPath): List<PedestrianResponse> {
        val PEDESTRINAN_CODE = 3 // trafficType이 3일때
        val FIRST_INDEX = 0 // 경로에서 첫 번째 구간이 도보일 때
        val LAST_INDEX = response.subPath.size - 1 // 경로에서 마지막 구간이 도보일 떄

        val routeCoordinateList: MutableList<PedestrianRouteRequest> = mutableListOf()
        response.subPath.forEachIndexed { index, it ->
            if (it.trafficType == PEDESTRINAN_CODE) {
                routeCoordinateList.add(
                    when (index) {
                        FIRST_INDEX -> PedestrianRouteRequest(
                            startX = start.longitude.toFloat(),
                            startY = start.latitude.toFloat(),
                            endX = response.subPath[FIRST_INDEX + 1].endX.toFloat(),
                            endY = response.subPath[FIRST_INDEX + 1].endX.toFloat()
                        )

                        LAST_INDEX -> PedestrianRouteRequest(
                            startX = response.subPath[LAST_INDEX - 1].endX.toFloat(),
                            startY = response.subPath[LAST_INDEX - 1].endX.toFloat(),
                            endX = end.longitude.toFloat(),
                            endY = end.latitude.toFloat()
                        )

                        else -> PedestrianRouteRequest(
                            startX = response.subPath[index - 1].endX.toFloat(),
                            startY = response.subPath[index - 1].endX.toFloat(),
                            endX = response.subPath[index + 1].endX.toFloat(),
                            endY = response.subPath[index + 1].endX.toFloat()
                        )
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
         *  - speed : TODO 데이터 분석 필요
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
}