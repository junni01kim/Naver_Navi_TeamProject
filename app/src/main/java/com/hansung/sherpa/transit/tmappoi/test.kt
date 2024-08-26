package com.hansung.sherpa.transit.tmappoi

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.subwayelevator.ElevatorException
import com.hansung.sherpa.subwayelevator.ElevatorLocResponse
import com.hansung.sherpa.subwayelevator.ElevatorService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

//fun getSubwayExitPois(stationNameExitNum:String, currLat:Double, currLon:Double): TmapPoiResponse? {
//    var rr: TmapPoiResponse? = null
//    runBlocking<Job> {
//        launch(Dispatchers.IO) {
//            try {
//                val response = Retrofit.Builder()
//                    .baseUrl("https://apis.openapi.sk.com/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//                    .create(TmapPoiService::class.java).getTmapPoi(
//                        BuildConfig.TMAP_APP_KEY,
//                        stationNameExitNum,
//                        currLat,
//                        currLon
//                    ).execute()
//                rr = Gson().fromJson(response.body()!!.string(), TmapPoiResponse::class.java)
//            } catch (e: IOException) {
//                Log.e("Error", "Transit API Exception ${rr}")
//                println(rr)
//            }
//        }
//    }
//    return rr
//}

//TODO Base URL에 BuildConfig.SHERPA_URL 적용
//BuildConfig.SHERPA_URL+"elevator/"
fun getSubwayElevLocation(stationName: String): ElevatorLocResponse? {
    lateinit var rr: ElevatorLocResponse
    runBlocking<Job> {
        launch(Dispatchers.IO) {
            try {
                val response = Retrofit.Builder()
                    .baseUrl("http://localhost:8080/api/v1/elevator/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ElevatorService::class.java)
                    .getElevatorLocation(stationName)
                    .execute()
                rr = Gson().fromJson(response.body()!!.string(), ElevatorLocResponse::class.java)
            } catch (e: IOException) {
                Log.e("Error", "Transit API Exception ${rr}")
            }
        }
    }

    if(rr.code==404) {
        throw ElevatorException(rr.message)
    }

    return rr
}

//// 여기서 역 출구만 걸러내짐
//fun fillterSubwayPoi(response: TmapPoiResponse?, subwayLineCode:Int, searchName:String): Poi? {
//    if(response==null) return null
//
//    val subwayLineName = lineCodeToName(subwayLineCode)
//
//    for(i in response.searchPoiInfo.pois.poi){
//        if(i.detailBizName==subwayLineName && i.lowerBizName=="지하철출구번호" && i.name==searchName){
//            return i
//        }
//    }
//
//    return null
//}

fun main(){
    try {
        //역이름은 기존 경로에서 참고
        val stationName = "서울123"

        // 출구는 백엔드 이용
        var elevLoc = getSubwayElevLocation(stationName)

        for(i in elevLoc!!.data){
            var res = i.split(",")
            var Lat = res[0]
            var Lon = res[1]
            println("" + Lat + Lon)
        }


    } catch(e:ElevatorException) {
        println(e.msg)
    }

}