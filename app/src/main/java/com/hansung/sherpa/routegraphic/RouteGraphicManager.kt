package com.hansung.sherpa.routegraphic

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.busarrivalinfo.BusArrivalInfoResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RouteGraphicManager(val context: Context) {
    fun getRouteGraphic(request:RouteGraphicRequest) : LiveData<RouteGraphicResponse> {
        val resultLiveData = MutableLiveData<RouteGraphicResponse>()
        val apiKey = BuildConfig.ODSAY_APP_KEY
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.odsay.com/v1/api/loadLane")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RouteGraphicService::class.java)

        service.getService(apiKey,request.getMap()).enqueue(object : Callback<ResponseBody>{
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
}