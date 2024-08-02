package com.hansung.sherpa.transit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * retrofit을 사용한 Transit API 쿼리 클래스
 *
 * @since 2024-05-09
 * @author HS-JNYLee
 */
interface TransitRouteService {

    /**
     * 대중교통을 이용한 도보 TMap API
     *
     * @param appKey TMAP APP KEY
     * @param body ROUTE REQUEST
     */
    @Headers(
        "accept: application/json",
        "content-type: application/json",
    )
    @POST("transit/routes")
    fun postTransitRoutes(@Header("appKey") appKey: String, @Body body: TmapTransitRouteRequest): Call<ResponseBody>

    @GET("searchPubTransPathT")
    fun getODsayTransitRoutes(@QueryMap options: Map<String, String>): Call<ResponseBody>

    /**
     * OSRM API 요청
     * 한 번 연결되면 512개의 요청을 보낼 수 있고, 그 이상은 5초를 기다려야 됨.
     *
     * @param SX 출발지 경도
     * @param SY 출발지 위도
     * @param EX 도착지 경도
     * @param EY 도착지 위도
     * @param options 쿼리스트링 옵션들
     *
     * 옵션 더보기 [OSRM route options](https://project-osrm.org/docs/v5.24.0/api/#route-service)
     */
    @GET("routed-foot/route/v1/driving/{SX},{SY};{EX},{EY}")
    fun getOSRMWalk(@Path("SX") SX: String
                    , @Path("SY") SY: String
                    , @Path("EX") EX: String
                    , @Path("EY") EY: String
                    , @QueryMap options: Map<String, String>): Call<ResponseBody>

    /**
     * ODsay 노선 그래픽 API
     */
    @GET("loadLane")
    fun getGraphicRoute(
        @QueryMap options: Map<String,String>
    ): Call<ResponseBody>
}