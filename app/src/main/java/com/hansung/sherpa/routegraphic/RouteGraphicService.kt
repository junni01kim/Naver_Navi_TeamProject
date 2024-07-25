package com.hansung.sherpa.routegraphic

import com.google.android.gms.common.api.internal.ApiKey
import com.hansung.sherpa.transit.TransitRouteRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.QueryMap

/**
 * retrofit을 사용한 RouteGraphicService API 쿼리 클래스
 *
 */
interface RouteGraphicService {
    /**
     * ODsay '노선 그래픽 데이터 검색' API
     *
     * @param options RouteGraphicRequest 매핑 값
     */
    @GET("loadLane")
    fun getService(
        @QueryMap options: Map<String,String>
    ): Call<ResponseBody>
}