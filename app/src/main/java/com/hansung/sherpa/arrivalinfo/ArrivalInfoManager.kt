package com.hansung.sherpa.arrivalinfo

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.arrivalinfo.odsay.ODsayArrivalInfoRequest
import com.hansung.sherpa.arrivalinfo.odsay.ODsayArrivalInfoResponse
import com.hansung.sherpa.arrivalinfo.odsay.ODsayArrivalInfoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ArrivalInfoManager {
    /**
     * 정류소 별 특정 노선 버스 도착 예정 정보, 목록 조회 API를 사용해 경로 데이터를 가져와 역 직렬화하는 함수
     *
     * @param request ODsay API에 전송할 데이터
     */
    fun getODsayArrivalInfoList(request: ODsayArrivalInfoRequest): ODsayArrivalInfoResponse? {
        var result: ODsayArrivalInfoResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl("https://api.odsay.com/v1/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(ODsayArrivalInfoService::class.java)
                        .getODsayArrivalInfoService(request.getMap()).execute()
                    result = Gson().fromJson(response.body()!!.string(), ODsayArrivalInfoResponse::class.java)
                } catch(e:IOException){
                    Log.e("API Log: IOException", "getODsayArrivalInfoList: ${e.message}(e.message)")
                }
            }
        }
        return result
    }
}