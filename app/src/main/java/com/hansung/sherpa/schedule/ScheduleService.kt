package com.hansung.sherpa.schedule

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ScheduleService {
    @GET("schedules/find/usingdatetime/{userid}/{datetime}")
    fun search(@Path("userid") userId : Int, @Path("datetime") datetime : String): Call<ScheduleResponse>

    @POST("schedules/insert")
    fun insert(@Body schedules: Schedules) : Call<Void>

    @DELETE("schedules/remove/{scheduleid}")
    fun delete(@Path("scheduleid") scheduleId : Int)
}