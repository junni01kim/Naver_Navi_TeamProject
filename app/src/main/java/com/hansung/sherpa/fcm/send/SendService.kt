package com.hansung.sherpa.fcm.send

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SendService {
    @POST("fcm")
    fun getSendService(@Body request: SendRequest): Call<ResponseBody>
}