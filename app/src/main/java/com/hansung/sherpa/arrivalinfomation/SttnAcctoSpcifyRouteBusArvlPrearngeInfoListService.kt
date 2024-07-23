package com.hansung.sherpa.arrivalinfomation

import androidx.room.Query
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface SttnAcctoSpcifyRouteBusArvlPrearngeInfoListService {

    @GET("getSttnAcctoSpcifyRouteBusArvlPrearngeInfoList")
    fun getService(
        @QueryMap options: Map<String, String>
    ): Call<ResponseBody>
}