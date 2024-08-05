package com.hansung.sherpa.arrivalinfo

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ODsayArrivalInfoService {
    @GET("realtimeStation")
    fun getODsayArrivalInfoService(@QueryMap options: Map<String,String>): Call<ResponseBody>
}