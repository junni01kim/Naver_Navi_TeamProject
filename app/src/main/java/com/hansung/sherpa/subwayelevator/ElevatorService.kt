package com.hansung.sherpa.subwayelevator

import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ElevatorService {
    @GET("{stationName}")
    fun getElevatorLocation(@Path("stationName") stationName:String):Call<ResponseBody>
}