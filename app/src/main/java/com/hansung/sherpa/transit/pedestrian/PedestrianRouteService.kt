package com.hansung.sherpa.transit.pedestrian

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface PedestrianRouteService {
    @Headers(
        "accept: application/json",
        "content-type: application/json",
    )
    @POST("tmap/routes/pedestrian")
    fun postPedestrianRoutes(@Header("appkey") appkey:String, @Body body: PedestrianRouteRequest): Call<ResponseBody>
}