package com.hansung.sherpa.schedule

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

data class RouteResponse (
    @SerializedName("code"    ) var code    : Int,
    @SerializedName("message" ) var message : String,
    @SerializedName("data"    ) var data    : RouteData
)

data class RouteData(
    @SerializedName("routeId") val routeId : Int,
    @SerializedName("location") val location : Location,
    @SerializedName("cron") val cron : String,
)

interface RouteService {
    @GET("route/{route_id}")
    fun search(@Path("route_id") routeId : Int) : Call<RouteResponse>

    @POST("route")
    fun insert(@Body route : Route) : Call<RouteResponse>

    @DELETE("route/delete/{route_id}")
    fun delete(@Path("route_id") routeId : Int) : Call<RouteResponse>

    @PATCH("route/update/{route_id}")
    fun update(@Path("route_id") routeId :  Int, @Body route : Route) : Call<RouteResponse>
}