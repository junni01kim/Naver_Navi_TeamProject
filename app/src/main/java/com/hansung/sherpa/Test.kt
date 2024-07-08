//package com.hansung.sherpa
//
//import android.util.Log
//import com.google.gson.Gson
//import com.hansung.sherpa.transit.PedestrianResponse
//import com.hansung.sherpa.transit.PedestrianRouteRequest
//import com.hansung.sherpa.transit.PedestrianRouteService
//import com.hansung.sherpa.transit.TransitManager
//import com.naver.maps.geometry.LatLng
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.runBlocking
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.io.IOException
//
//fun getPedestrianRoute(routeRequest: PedestrianRouteRequest): PedestrianResponse {
//    val appKey = BuildConfig.TMAP_APP_KEY // 앱 키
//    lateinit var rr: PedestrianResponse
//    runBlocking<Job> {
//        launch(Dispatchers.IO) {
//            try {
//                val response = Retrofit.Builder()
//                    .baseUrl("https://apis.openapi.sk.com/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//                    .create<PedestrianRouteService?>(PedestrianRouteService::class.java)
//                    .postPedestrianRoutes(appKey, routeRequest).execute() // API 호출
//                rr = Gson().fromJson(
//                    response.body()!!.string(),
//                    PedestrianResponse::class.java
//                )
//            } catch (e: IOException) {
//                Log.i("Error", "Transit API Exception")
//                rr = PedestrianResponse()
//            }
//        }
//    }
//    return rr
//}
//
//fun main(){
//    val tempStartLatLng = LatLng(37.642743, 126.835375)
//    val tempEndLatLng = LatLng(37.627444, 126.829600)
//
//    var res:PedestrianResponse = getPedestrianRoute(
//        PedestrianRouteRequest(
//            startX = tempStartLatLng.latitude.toFloat(),
//            startY = tempStartLatLng.longitude.toFloat(),
//            endX = tempEndLatLng.latitude.toFloat(),
//            endY =  tempEndLatLng.longitude.toFloat()
//        )
//    )
//
//    println(res)
//}

package com.hansung.sherpa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hansung.sherpa.transit.PedestrianResponse
import com.hansung.sherpa.transit.PedestrianRouteRequest
import com.hansung.sherpa.transit.PedestrianRouteService
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import kotlin.reflect.typeOf


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
//    val pr = PedestrianRouteRequest(
//        startY = "37.556770374096615".toFloat(),
//        startX = "126.92365493654832".toFloat(),
//        endY = "37.55279861528311".toFloat(),
//        endX = "126.92432158129688".toFloat(),
//        passList = ""+location.latitude+","+location.longitude+"_"+endLatLng.latitude + ","+ endLatLng.longitude
//    )
//
//    var res = getPedestrianRoute(pr)
//
//    // 처음 시작좌표랑 끝 좌표는 어카지...
//    var rres = mutableListOf<LatLng>()
//    res.features?.forEach {feature->
//        if(feature.geometry.type=="LineString"){
//            feature.geometry.coordinates.forEach {coordinates->
//                var tmp = coordinates.toString().split(", ")
//                rres.add(LatLng(tmp[1].replace("]","").toDouble(), tmp[0].replace("[","").toDouble()))
//            }
//        }
//    }
//    rres = rres.distinct().toMutableList()
//    //
//
//    println(rres)
//
//    println()
//
//    println(res.features)


}



//class YourTest {
//    @Test
//    fun testMethod() {
//        val pr = PedestrianRouteRequest(
//            startY = "37.642636".toFloat(),
//            startX = "127.835763".toFloat(),
//            endY = "37.627448".toFloat(),
//            endX = "126.829388".toFloat()
//        )
//
//        var res = getPedestrianRoute(pr)
//
//        /*
//        var res1 = TransitManager(StaticValue.mainActivity).getTransitRoutes(
//            TransitRouteRequest(
//                startX = "126.926493082645",
//                startY = "37.6134436427887",
//                endX = "127.126936754911",
//                endY = "37.5004198786564",
//                lang = 0,
//                format = "json",
//                count = 10
//            )
//        )
//        */
//
//
//        println(res)
//    }
//}