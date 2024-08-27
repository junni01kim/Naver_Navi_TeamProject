package com.hansung.sherpa.sendInfo.send

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface SendService {
    @POST("fcm")
    fun postSendServiceToToken(@Body request: SendRequest): Call<ResponseBody>

    @POST("fcm/send/{userId}")
    fun postSendService(@Path("userId") userId: Int, @Body request: SendRequest): Call<ResponseBody>

    @POST("navigation/create/{caretakerId}")
    fun postCreateNavigation(@Path("caretakerId") caretakerId:Int, @Body request: String): Call<ResponseBody>

    @PATCH("navigation/route/{caretakerId}")
    fun patchUpdateRoute(@Path("caretakerId") caretakerId:Int, @Body request: String): Call<ResponseBody>

    @DELETE("delete/{userId}")
    fun deleteRoute(@Path("userId") userId:Int): Call<ResponseBody>
}