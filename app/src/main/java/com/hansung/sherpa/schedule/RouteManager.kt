package com.hansung.sherpa.schedule

import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.ui.preference.calendar.ScheduleData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
                cron = "",
                location = Location(
                    name = scheduleData.scheduledLocation.name,
                    latitude = scheduleData.scheduledLocation.lat,
                    longitude = scheduleData.scheduledLocation.lon
                ),
            )
        ).execute()

        return if (response.isSuccessful)
            response.body()?.data
        else
            null
    }

    fun deleteRoute(routeId : Int){
        runBlocking {
            withContext(Dispatchers.IO){
                val response = retrofitService.delete(routeId).execute()
            }
        }
    }

    fun updateRoute(routeId : Int, route : Route){
        runBlocking {
            withContext(Dispatchers.IO){
                val response = retrofitService.update(routeId, route).execute()
            }
        }
    }

}