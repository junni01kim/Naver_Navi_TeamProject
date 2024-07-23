package com.hansung.sherpa.busarrivalinfo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface BusArrivalInfoService {

    @GET("getSttnAcctoSpcifyRouteBusArvlPrearngeInfoList")
    fun getService(
        @QueryMap options: Map<String, String>
    ): Call<BusArrivalInfoResponse>
}