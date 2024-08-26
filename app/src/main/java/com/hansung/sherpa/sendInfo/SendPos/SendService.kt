package com.hansung.sherpa.sendInfo.SendPos

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface SendService {
    @POST("fcm")
    fun postSendServiceToToken(@Body request: SendRequest): Call<ResponseBody>

    @POST("fcm/send/{userId}")
    fun postSendService(@Path("userId") userId: Int, @Body request: SendRequest): Call<ResponseBody>
}