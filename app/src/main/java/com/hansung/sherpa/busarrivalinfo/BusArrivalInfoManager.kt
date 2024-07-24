package com.hansung.sherpa.busarrivalinfo

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hansung.sherpa.transit.TransitRouteRequest
import com.hansung.sherpa.transit.TransitRouteResponse
import com.naver.maps.geometry.LatLng
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Thread.sleep

class BusArrivalInfoManager(val context: Context) {
    fun getBusArrivalInfoList(request:BusArrivalInfoRequest):BusArrivalInfoResponse {
        lateinit var result: BusArrivalInfoResponse
        runBlocking {
            launch(Dispatchers.IO) {
                try {
                    val response = Retrofit.Builder()
                        .baseUrl("https://apis.data.go.kr/1613000/ArvlInfoInqireService/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(BusArrivalInfoService::class.java)
                        .getService(request.getMap()).execute()
                    // TODO: 파싱이 진행되어야 함
                    result = Gson().fromJson(
                        response.body()!!.string(),
                        BusArrivalInfoResponse::class.java
                    )
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

    fun getBusArrivalInfoList2(request:BusArrivalInfoRequest): LiveData<BusArrivalInfoResponse> {
        val resultLiveData = MutableLiveData<BusArrivalInfoResponse>()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/1613000/ArvlInfoInqireService/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BusArrivalInfoService::class.java)

        service.getService(request.getMap()).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                val responseBody = response.body()
                Log.d("explain", "responseBody: ${responseBody?.string()}")
                if (responseBody != null) {
                    val busArrivalInfoResponse = Gson().fromJson(
                        responseBody.string(),
                        BusArrivalInfoResponse::class.java
                    )
                    resultLiveData.postValue(busArrivalInfoResponse)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("explain", "onFailure: 실패")
                Log.d("explain", "message: ${t.message}")
            }
        })
        return resultLiveData
    }
}

