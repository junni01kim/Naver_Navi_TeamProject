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
                        .baseUrl("https://apis.data.go.kr/1613000/ArvlInfoInqireService/")
                        .addConverterFactory(TikXmlConverterFactory.create(parser))
                        .build()
                        .create(BusArrivalInfoService::class.java)
                        .getService(request.getMap()).execute()
                    // TODO: 파싱이 진행되어야 함
                    result = response.body()!!
                    Log.d("explain", "response:"+result.toString())
                } catch (e: IOException) {
                    Log.d("explain", "getBusArrivaInfoList API Exception")
                    Log.d("explain", "cause: ${e.cause}")
                    Log.d("explain", "message: ${e.message}")
                    Log.d("explain", "localizedMessage: ${e.localizedMessage}")
                    result = BusArrivalInfoResponse()
                }
            }
        }
        return result
    }

    fun getBusArrivaInfoList2(request:BusArrivalInfoRequest):BusArrivalInfoResponse {
        var result = BusArrivalInfoResponse(null,null)
        val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/1613000/ArvlInfoInqireService/")
            .addConverterFactory(TikXmlConverterFactory.create(parser))
            .build()

        val service = retrofit.create(BusArrivalInfoService::class.java)

        val value = service.getService(request.getMap()).enqueue(object : Callback<BusArrivalInfoResponse>{
                override fun onResponse(
                    call: Call<BusArrivalInfoResponse>,
                    response: Response<BusArrivalInfoResponse>
                ) {
                    if(!response.isSuccessful){
                        Log.d("explain", "onResponse: 실패")
                        return
                    }
                    result = response.body()!!
                    Log.d("explain", "onResponse $result")
                }

                override fun onFailure(call: Call<BusArrivalInfoResponse>, t: Throwable) {
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "cause: ${t.cause}")
                    Log.d("explain", "message: ${t.message}")
                }
            })

        if(result.body!=null && result.header!=null) Log.d("explain","result: ${result}")
        else Log.d("explain","result: 비정상 작동")
        return result
    }
}

