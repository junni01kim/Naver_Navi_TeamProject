package com.hansung.sherpa.arrivalinfo.odsay

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ODsayArrivalInfoService {
    /**
     * ODsay 실시간 버스 도착정보 조회 API를 이용하기 위한 함수
     * @param options 제작한 ODsayArrivalInfoRequest를 매핑한 값
     *
     */
    @GET("realtimeStation")
    fun getODsayArrivalInfoService(@QueryMap options: Map<String,String>): Call<ResponseBody>
}