package com.hansung.sherpa.subwayelevator

import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ElevatorService {
    @GET("getexitnum")
    fun getElevatorLocation(@Query("stationName") stationName:String):Call<ResponseBody>
}