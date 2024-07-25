package com.hansung.sherpa.routegraphic

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.R
import com.hansung.sherpa.busarrivalinfo.BusArrivalInfoResponse
import com.hansung.sherpa.transit.TransitRouteRequest
import com.hansung.sherpa.transit.TransitRouteResponse
import com.hansung.sherpa.transit.TransitRouteService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RouteGraphicManager(val context: Context) {
    fun getRouteGraphic(routeRequest:RouteGraphicRequest) : LiveData<RouteGraphicResponse> {
        val resultLiveData = MutableLiveData<RouteGraphicResponse>()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.odsay.com/v1/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RouteGraphicService::class.java)

        service.getService(routeRequest.getMap()).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body()
                Log.d("explain", "responseBody: ${responseBody?.string()}")
                if (responseBody != null) {
                    val routeGraphicResponse = Gson().fromJson(
                        responseBody.string(),
                        RouteGraphicResponse::class.java
                    )
                    resultLiveData.postValue(routeGraphicResponse)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("explain", "onFailure: 실패")
                Log.d("explain", "message: ${t.message}")
            }
        })
        return resultLiveData
    }

    fun getRouteGraphic2(routeRequest: RouteGraphicRequest): RouteGraphicResponse? {
        var rr: RouteGraphicResponse? = null
        runBlocking {
            launch(Dispatchers.IO) {
                try {
                    val response = Retrofit.Builder()
                        .baseUrl("https://api.odsay.com/v1/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(RouteGraphicService::class.java)
                        .getService(routeRequest.getMap()).execute()
                    rr = Gson().fromJson(response.body()!!.string(), RouteGraphicResponse::class.java)
                } catch (e: IOException) {
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
        return rr
    }
}