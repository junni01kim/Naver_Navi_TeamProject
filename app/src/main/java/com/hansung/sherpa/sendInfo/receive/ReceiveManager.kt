package com.hansung.sherpa.sendInfo.receive

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.itemsetting.SubPath
import com.hansung.sherpa.itemsetting.SubPathAdapter
import com.hansung.sherpa.itemsetting.TransportRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

val nncBackendUserUrl = BuildConfig.SHERPA_URL

class ReceiveManager {
    /**
     * [Sherpa 내부 서버] 모든 경로 좌표를 받아오는 함수
     *
     * @return API 결과와 TransportRoute
     */
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

    /**
     * [Sherpa 내부 서버] 모든 재갱신된 경로를 받아오는 함수
     *
     * @return API 결과와 ReceiveRouteResponse
     */
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

    fun fromTransportRouteJson(encodingJson:String):TransportRoute {
        // 디코딩 작업 (transportRoute는 디코딩되어 전달된다.)
        val json = encodingJson
            .substring(1, encodingJson.length - 1)
            .chunked(2)
            .map { Integer.parseInt(it, 16).toByte() }
            .toByteArray()
            .toString(Charsets.UTF_8)

        // transportRoute는 interface로 제작되어 있어 추가적인 타입 변환 어댑터가 필요하다.
        val gson = GsonBuilder()
            .registerTypeAdapter(SubPath::class.java, SubPathAdapter())
            .create()

        // 정상화된 json 객체 파싱 진행
        Log.d("API Log", "반환: ${json}")
        return gson.fromJson(json, TransportRoute::class.java)
    }

    fun fromReceiveRouteResponse(encodingJson:String):ReceiveRouteResponse {
        // 디코딩 작업 (ReceiveResponse는 디코딩되어 전달된다.)
        val json = encodingJson
            .substring(1, encodingJson.length - 1)
            .chunked(2)
            .map { Integer.parseInt(it, 16).toByte() }
            .toByteArray()
            .toString(Charsets.UTF_8)

        // 정상화된 json 객체 파싱 진행
        Log.d("API Log", "반환: ${json}")
        return Gson().fromJson(json, ReceiveRouteResponse::class.java)
    }
}