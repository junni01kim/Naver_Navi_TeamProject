package com.hansung.sherpa.sendInfo.SendPos

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SendService {
    @POST("fcm")
    fun postSendService(@Body request: SendRequest): Call<ResponseBody>
}