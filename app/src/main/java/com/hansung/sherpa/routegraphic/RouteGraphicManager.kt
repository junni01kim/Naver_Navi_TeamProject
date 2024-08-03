package com.hansung.sherpa.routegraphic

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hansung.sherpa.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 * @param context Activity의 context
 */
class RouteGraphicManager(val context: Context) {
    /**
     * ODsay '노선 그래픽 데이터 검색' API를 사용해 경로 데이터를 가져와 역직렬화하는 함수
     *
     * @param request 요청할 정보 객체
     * @return LiveData<RouteGraphicResponse>
     */
    fun getRouteGraphic(request:RouteGraphicRequest) : LiveData<RouteGraphicResponse> {
        val resultLiveData = MutableLiveData<RouteGraphicResponse>()
        val retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.odsay_route_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RouteGraphicService::class.java)

        service.getService(request.getMap()).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val routeGraphicResponse = Gson().fromJson(
                        responseBody.string(),
                        RouteGraphicResponse::class.java
                    )
                    resultLiveData.postValue(routeGraphicResponse)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("error:RouteGraphic", "RouteGraphic 요청 실패")
                Log.e("error:RouteGraphic", "error code: ${t.cause} message: ${t.message}")
            }
        })
        return resultLiveData
    }
    /**
     * ODsay '노선 그래픽 데이터 검색' API를 사용해 경로 데이터를 가져와 역직렬화하는 함수
     *
     * @param request 요청할 정보 객체
     * @return RouteGraphicResponse
     */
    fun getRouteGraphic2(request: RouteGraphicRequest): RouteGraphicResponse? {
        var result: RouteGraphicResponse? = null
        runBlocking {
            launch(Dispatchers.IO) {
                try {
                    val response = Retrofit.Builder()
                        .baseUrl(context.getString(R.string.odsay_route_base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(RouteGraphicService::class.java)
                        .getService(request.getMap()).execute()
                    result = Gson().fromJson(response.body()!!.string(), RouteGraphicResponse::class.java)
                } catch (e: IOException) {
                    Log.e("error:RouteGraphic", "RouteGraphic 요청 실패")
                    Log.e("error:RouteGraphic", "error code: ${e.cause} message: ${e.message}")
                }
            }
        }
        return result
    }
}