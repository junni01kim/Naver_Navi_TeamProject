package com.hansung.sherpa.busarrivalinfo

import android.content.Context
import android.util.Log
import com.hansung.sherpa.transit.TransitRouteRequest
import com.naver.maps.geometry.LatLng
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.Thread.sleep

class BusArrivalInfoManager(val context: Context) {
    fun getBusArrivaInfoList(request:BusArrivalInfoRequest):BusArrivalInfoResponse {
        lateinit var result: BusArrivalInfoResponse
        runBlocking {
            launch(Dispatchers.IO) {
                try {
                    val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()
                    val response = Retrofit.Builder()
                        .baseUrl("http://apis.data.go.kr/1613000/ArvlInfoInqireService/")
                        .addConverterFactory(TikXmlConverterFactory.create(parser))
                        .build()
                        .create(BusArrivalInfoService::class.java)
                        .getService(request.getMap()).execute()
                    result = response.body()!!
                } catch (e: IOException) {
                    Log.d("explain", "getBusArrivaInfoList API Exception")
                    result = BusArrivalInfoResponse()
                }
            }
        }
        return result
    }

    fun getBusArrivaInfoList2(request:BusArrivalInfoRequest):BusArrivalInfoResponse {
        lateinit var result: BusArrivalInfoResponse
        runBlocking {
            launch(Dispatchers.IO) {
                try {
                    val response = Retrofit.Builder()
                        .baseUrl("http://apis.data.go.kr/1613000/ArvlInfoInqireService/")
                        .addConverterFactory(TikXmlConverterFactory.create(TikXml.Builder().exceptionOnUnreadXml(false).build()))
                        .build()
                        .create(BusArrivalInfoService::class.java)
                        .getService(request.getMap()).execute()
                    result = response.body()!!
                } catch (e: IOException) {
                    Log.d("explain", "getBusArrivaInfoList API Exception")
                    result = BusArrivalInfoResponse()
                }
            }
        }
        return result
    }
}

