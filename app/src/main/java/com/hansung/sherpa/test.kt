package com.hansung.sherpa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hansung.sherpa.transit.PedestrianResponse
import com.hansung.sherpa.transit.PedestrianRouteRequest
import com.hansung.sherpa.transit.PedestrianRouteService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


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


fun main(){
    val pr = PedestrianRouteRequest(
        startY = "37.642636".toFloat(),
        startX = "127.835763".toFloat(),
        endY = "37.627448".toFloat(),
        endX = "126.829388".toFloat()
    )

    var res = getPedestrianRoute(pr)

    /*
    var res1 = TransitManager(StaticValue.mainActivity).getTransitRoutes(
        TransitRouteRequest(
            startX = "126.926493082645",
            startY = "37.6134436427887",
            endX = "127.126936754911",
            endY = "37.5004198786564",
            lang = 0,
            format = "json",
            count = 10
        )
    )
    */


    println(res)
}



class YourTest {
    @Test
    fun testMethod() {
        val pr = PedestrianRouteRequest(
            startY = "37.642636".toFloat(),
            startX = "127.835763".toFloat(),
            endY = "37.627448".toFloat(),
            endX = "126.829388".toFloat()
        )

        var res = getPedestrianRoute(pr)

        /*
        var res1 = TransitManager(StaticValue.mainActivity).getTransitRoutes(
            TransitRouteRequest(
                startX = "126.926493082645",
                startY = "37.6134436427887",
                endX = "127.126936754911",
                endY = "37.5004198786564",
                lang = 0,
                format = "json",
                count = 10
            )
        )
        */


        println(res)
    }
}