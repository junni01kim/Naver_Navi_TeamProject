package com.hansung.sherpa.routegraphic

import com.google.android.gms.common.api.internal.ApiKey
import com.hansung.sherpa.transit.TransitRouteRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface RouteGraphicService {
    fun getService(
        @QueryMap options: Map<String,String>
    ): Call<ResponseBody>
}