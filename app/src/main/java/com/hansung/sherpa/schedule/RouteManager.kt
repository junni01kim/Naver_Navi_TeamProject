package com.hansung.sherpa.schedule

import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.ui.preference.calendar.ScheduleData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * @author 6-keem
 *
 * 경로 안내 데이터 추가, 수정, 삭제를 수행하는 클래스
 */
class RouteManager {
    private val retrofitService = Retrofit.Builder()
        .baseUrl(BuildConfig.SHERPA_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RouteService::class.java)

    /**
     * 경로 안내 찾기
     *
     * @param routeId 경로 아이디
     * @return RouteData?
     */
    fun findRoute(routeId : Int) : RouteData? {
        var response : Response<RouteResponse>
        runBlocking {
            withContext(Dispatchers.IO) {
                response = retrofitService.search(routeId = routeId).execute()
            }
        }
        return if (response.isSuccessful)
            response.body()?.data
        else
            null
    }

    /**
     * 경로 안내 추가
     *
     * @param scheduleData
     * @return RouteData
     */
    fun insertRoute(scheduleData: ScheduleData) : RouteData?{
        var response : Response<RouteResponse>
        runBlocking {
            withContext(Dispatchers.IO){
                response = retrofitService.insert(
                    Route(
                        cron = "",
                        location = Location(
                            name = scheduleData.scheduledLocation.name,
                            latitude = scheduleData.scheduledLocation.lat,
                            longitude = scheduleData.scheduledLocation.lon
                        ),
                    )
                ).execute()
            }
        }

        return if (response.isSuccessful)
            response.body()?.data
        else
            null
    }

    /**
     * 경로 삭제
     *
     * @param routeId
     */
    fun deleteRoute(routeId : Int){
        runBlocking {
            withContext(Dispatchers.IO){
                retrofitService.delete(routeId).execute()
            }
        }
    }

    /**
     * 경로 업데이트
     *
     * @param routeId
     * @param route
     */
    fun updateRoute(routeId : Int, route : Route){
        runBlocking {
            withContext(Dispatchers.IO){
                retrofitService.update(routeId, route).execute()
            }
        }
    }
}