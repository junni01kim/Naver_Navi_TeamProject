package com.hansung.sherpa.emergency

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EmergencyService {
    @POST("emergency")
    fun postAddEmergencyService(@Body body: Emergency): Call<ResponseBody>

    @DELETE("emergency/{emergencyId}")
    fun deleteEmergencyService(@Path("emergencyId") emergencyId: Int): Call<ResponseBody>

    @GET("emergency/{userId}/list")
    fun getAllEmergency(@Path("userId") userId: Int): Call<ResponseBody>
}