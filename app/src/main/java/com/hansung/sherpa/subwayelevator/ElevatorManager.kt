package com.hansung.sherpa.subwayelevator

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 * DB의 elevator_subway_location 테이블에 접근
 *
 * @param stationName 역 이름(EX : 한성대입구)
 * */

fun getSubwayElevLocation(stationName: String): ElevatorLocResponse {
    val baseUrl = BuildConfig.SHERPA_URL + "elevator/"
    lateinit var rr: ElevatorLocResponse
    runBlocking<Job> {
        launch(Dispatchers.IO) {
            try {
                val response = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ElevatorService::class.java)
                    .getElevatorLocation(stationName)
                    .execute()
                rr = Gson().fromJson(response.body()!!.string(), ElevatorLocResponse::class.java)
            } catch (e: IOException) {
                Log.e("Error", "Transit API Exception $rr")
            }
        }
    }

    // 검색한 역이 DB에 없는 경우
    if(rr.code==404) {
        throw ElevatorException(rr.message)
    }

    return rr
}