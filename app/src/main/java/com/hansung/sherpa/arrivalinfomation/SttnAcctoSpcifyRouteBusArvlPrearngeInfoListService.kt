package com.hansung.sherpa.arrivalinfomation

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header

interface SttnAcctoSpcifyRouteBusArvlPrearngeInfoListService {

    fun temp(@Header("serviceKey") serviceKey: String, @Body body: SttnAcctoSpcifyRouteBusArvlPrearngeInfoListResponse): Call<ResponseBody>
}