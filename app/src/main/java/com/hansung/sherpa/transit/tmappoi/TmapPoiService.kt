package com.hansung.sherpa.transit.tmappoi

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface TmapPoiService {
    @Headers(
        "accept: application/json",
        "content-type: application/json",
    )
    @GET("tmap/pois")
    fun getTmapPoi(
        @Header("appkey") appkey:String,
        @Query("searchKeyword") keyword: String,
        @Query("centerLat") centerLat:Double,
        @Query("centerLon") centerLon:Double,
        @Query("version") version: String="1"
    ): Call<ResponseBody>
}