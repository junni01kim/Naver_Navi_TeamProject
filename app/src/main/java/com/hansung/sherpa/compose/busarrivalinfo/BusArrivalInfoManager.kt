package com.hansung.sherpa.compose.busarrivalinfo

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.busarrivalinfo.BusArrivalInfoRequest
import com.hansung.sherpa.busarrivalinfo.BusArrivalInfoResponse
import com.hansung.sherpa.busarrivalinfo.BusArrivalInfoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class BusArrivalInfoManager {
    /**
     * 정류소별특정노선버스 도착예정정보 목록조회 API를 사용해 경로 데이터를 가져와 역직렬화하는 함수
     *
     * @param request 요청할 정보 객체
     * @return BusArrivalInfoResponse
     */
    fun getBusArrivalInfoList2(request: BusArrivalInfoRequest): BusArrivalInfoResponse? {
        var result: BusArrivalInfoResponse? = null
        runBlocking {
            launch(Dispatchers.IO) {
                try {
                    val response = Retrofit.Builder()
                        .baseUrl("https://apis.data.go.kr/1613000/ArvlInfoInqireService/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(BusArrivalInfoService::class.java)
                        .getService(request.getMap()).execute()
                    result = Gson().fromJson(response.body()!!.string(), BusArrivalInfoResponse::class.java)
                } catch (e: IOException) {
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
        return result
    }
}