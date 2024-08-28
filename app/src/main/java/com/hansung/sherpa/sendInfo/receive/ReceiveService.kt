package com.hansung.sherpa.sendInfo.receive

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ReceiveService {
    @GET("navigation/transportRoute/{caregiverId}")
    fun getTransportRoute(@Path("caregiverId") caregiverId:Int): Call<ResponseBody>

    @GET("navigation/route/{caregiverId}")
    fun getRoute(@Path("caregiverId") caregiverId: Int): Call<ResponseBody>
}