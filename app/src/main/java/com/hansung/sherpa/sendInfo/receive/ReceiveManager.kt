package com.hansung.sherpa.sendInfo.receive

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.itemsetting.TransportRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

val nncBackendUserUrl = BuildConfig.SHERPA_URL

class ReceiveManager {
    fun getTransportRoute():CommonResponse {
        var result: CommonResponse? = null
        runBlocking {
            launch(Dispatchers.IO) {
                try{
                    val response = Retrofit.Builder()
                        .baseUrl(nncBackendUserUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(ReceiveService::class.java)
                        .getTransportRoute(StaticValue.userInfo.userId?:-1).execute()
                    val jsonString = response.body()?.string()?:"response is null"

                    if(jsonString == "response is null") {
                        Log.e("API Log:response(Null)", "getTransportRoute: 'response is null'")
                        result = CommonResponse(404, "getTransportRoute: 'response is null'")
                    }
                    else {
                        Log.i("API Log: Success", "getTransportRoute 함수 실행 성공 ${jsonString}")
                        result = Gson().fromJson(
                            jsonString,
                            CommonResponse::class.java
                        )
                    }
                } catch(e: IOException){
                    Log.e("API Log: IOException", "getTransportRoute: ${e.message}(e.message)")
                    result = CommonResponse(404, "IOException: 네트워크 연결 실패")
                }
            }
        }
        return result?: CommonResponse(500, "에러 원인을 찾을 수 없음")
    }

    fun getRoute(): CommonResponse {
        var result: CommonResponse? = null
        runBlocking {
            launch(Dispatchers.IO) {
                try{
                    val response = Retrofit.Builder()
                        .baseUrl(nncBackendUserUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(ReceiveService::class.java)
                        .getRoute(StaticValue.userInfo.userId?:-1).execute()
                    val jsonString = response.body()?.string()?:"response is null"

                    if(jsonString == "response is null") {
                        Log.e("API Log:response(Null)", "getRoute: 'response is null'")
                        result = CommonResponse(404, "getRoute: 'response is null'")
                    }
                    else {
                        Log.i("API Log: Success", "getRoute 함수 실행 성공 ${result?.message}")
                        result = Gson().fromJson(
                            jsonString,
                            CommonResponse::class.java
                        )
                    }
                } catch(e: IOException){
                    Log.e("API Log: IOException", "getRoute: ${e.message}(e.message)")
                    result = CommonResponse(404, "IOException: 네트워크 연결 실패")
                }
            }
        }
        return result?: CommonResponse(500, "에러 원인을 찾을 수 없음")
    }
}