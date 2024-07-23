package com.hansung.sherpa.busarrivalinfo

import android.content.Context
import android.util.Log
import com.hansung.sherpa.transit.TransitRouteRequest
import com.naver.maps.geometry.LatLng
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class BusArrivalInfoManager(val context: Context) {
    fun getBusArrivaInfoList(request:BusArrivalInfoRequest):BusArrivalInfoResponse {
        val serviceKey = "VCzeRv5K5pqKSztweNXA4lJt8uyxti3zL4LJr7h%2BKEULei9hs7ZRTRU0b5jSlTZQ5i3lycuEN7NS6uACbg4ZwA%3D%3D"
        lateinit var result: BusArrivalInfoResponse
        runBlocking {
            launch(Dispatchers.IO) {
                try {
                    val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()
                    var retrofit = Retrofit.Builder()
                        .baseUrl("http://apis.data.go.kr/1613000/ArvlInfoInqireService/")
                        .addConverterFactory(TikXmlConverterFactory.create(parser))
                        .build().create(BusArrivalInfoService::class.java)
                        .getService(request.getMap())
                        .enqueue(object: Callback<BusArrivalInfoResponse> {
                            // 성공 시 콜백
                            override fun onResponse(
                                call: Call<BusArrivalInfoResponse>,
                                response: Response<BusArrivalInfoResponse>
                            ) {
                                val responseBody = response.body()
                                if (responseBody != null) {
                                    Log.d("explain", "${responseBody.body}")
                                }
                            }

                            override fun onFailure(call: Call<BusArrivalInfoResponse>, t:Throwable){}
                        })
                } catch (e: IOException) {
                    Log.i("Error", "Transit API Exception")
                    // result = BusArrivaInfoResponse()
                }
            }
        }
        return result
    }
}

