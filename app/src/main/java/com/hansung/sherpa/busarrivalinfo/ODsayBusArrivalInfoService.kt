package com.hansung.sherpa.busarrivalinfo

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ODsayBusArrivalInfoService {
    @GET("realtimeStation")
    fun getODsayBusArrivalInfoService(@QueryMap options: Map<String,String>): Call<ResponseBody>
}