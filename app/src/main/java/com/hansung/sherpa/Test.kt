package com.hansung.sherpa

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.transit.PedestrianResponse
import com.hansung.sherpa.transit.PedestrianRouteRequest
import com.hansung.sherpa.transit.PedestrianRouteService
import com.hansung.sherpa.transit.TransitManager
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

fun main(){
    val tempStartLatLng = LatLng(37.642743, 126.835375)
    val tempEndLatLng = LatLng(37.627444, 126.829600)

    val tm = Test()

    var res:PedestrianResponse = tm.getPedestrianRoute(
        PedestrianRouteRequest(
            startX = tempStartLatLng.latitude.toFloat(),
            startY = tempStartLatLng.longitude.toFloat(),
            endX = tempEndLatLng.latitude.toFloat(),
            endY =  tempEndLatLng.longitude.toFloat()
        )
    )

    println(res)
}

class Test{
    fun getPedestrianRoute(routeRequest: PedestrianRouteRequest): PedestrianResponse {
        val appKey = BuildConfig.TMAP_APP_KEY // 앱 키
        lateinit var rr: PedestrianResponse
        runBlocking<Job> {
            launch(Dispatchers.IO) {
                try {
                    val response = Retrofit.Builder()
                        .baseUrl("https://apis.openapi.sk.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create<PedestrianRouteService?>(PedestrianRouteService::class.java)
                        .postPedestrianRoutes(appKey, routeRequest).execute() // API 호출
                    rr = Gson().fromJson(
                        response.body()!!.string(),
                        PedestrianResponse::class.java
                    )
                } catch (e: IOException) {
                    Log.i("Error", "Transit API Exception")
                    rr = PedestrianResponse()
                }
            }
        }
        return rr
    }
}