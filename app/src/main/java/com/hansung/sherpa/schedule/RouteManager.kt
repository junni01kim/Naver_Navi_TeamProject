package com.hansung.sherpa.schedule

import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.ui.preference.calendar.ScheduleData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RouteManager {
    private val retrofitService = Retrofit.Builder()
        .baseUrl(BuildConfig.SHERPA_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RouteService::class.java)

    fun findRoute(routeId : Int) : RouteData? {
        val response = retrofitService.search(routeId = routeId).execute()
        return if (response.isSuccessful)
            response.body()?.data
        else
            null
    }

    fun insertRoute(scheduleData: ScheduleData) : RouteData?{
        val response = retrofitService.insert(
            Route(
                location = Location(
                    name = scheduleData.scheduledLocation.name,
                    latitude = scheduleData.scheduledLocation.lat,
                    longitude = scheduleData.scheduledLocation.lon
                ),
                cron = ""
            )
        ).execute()

        return if (response.isSuccessful)
            response.body()?.data
        else
            null
    }

    fun deleteRoute(routeId : Int){
        val response = retrofitService.delete(routeId)
    }
}