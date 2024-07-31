package com.hansung.sherpa.busarrivalinfo

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * retrofit을 사용한 BusArrivalInfo API 쿼리 클래스
 *
 */
interface BusArrivalInfoService {
    /**
     * '정류소별특정노선버스 도착예정정보 목록조회' 공공데이터포털 API
     *
     * @param options BusArrivalInfoRequest 매핑 값
     */
    @GET("getSttnAcctoSpcifyRouteBusArvlPrearngeInfoList")
    fun getService(
        @QueryMap options: Map<String, String>
    ): Call<ResponseBody>
}