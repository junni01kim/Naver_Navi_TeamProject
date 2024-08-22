package com.hansung.sherpa.emergency

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.user.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

val nncBackendEmergencyUrl = "http://13.209.212.166:8080/api/v1/"

class EmergencyManager {
    /**
     * 긴급 연락처 추가 함수
     */
    fun insertEmergency(request: Emergency):EmergencyResponse {
        var result: EmergencyResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl(nncBackendEmergencyUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(EmergencyService::class.java)
                        .postAddEmergencyService(request).execute()
                    val jsonString = response.body()?.string()?:"response is null"

                    //반환 실패에 대한 에러처리
                    if(jsonString == "response is null"){
                        Log.e("API Log: response(Null)", "insertEmergency: "+result?.message)
                        result = EmergencyResponse(404, "'response.body()' is null")
                    }
                    else {
                        Log.i("API Log: Success", "insertEmergency 함수 실행 성공 ${result?.message}")
                        result = Gson().fromJson(
                            jsonString,
                            EmergencyResponse::class.java
                        )
                    }
                } catch(e: IOException){
                    Log.e("API Log: IOException", "insertEmergency: ${e.message}(e.message)")
                    result = EmergencyResponse(404, "IOException: 네트워크 연결 실패")
                }
            }
        }
        return result?:EmergencyResponse(500, "에러 원인을 찾을 수 없음", null)
    }

    /**
     * 긴급 연락처 삭제 함수
     */
    fun deleteEmergency(emergencyId:Int):DeleteEmergencyResponse {
        var result: DeleteEmergencyResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl(nncBackendEmergencyUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(EmergencyService::class.java)
                        .deleteEmergencyService(emergencyId).execute()
                    val jsonString = response.body()?.string()?:"response is null"

                    //반환 실패에 대한 에러처리
                    if(jsonString == "response is null"){
                        Log.e("API Log: response(Null)", "deleteEmergency: "+result?.message)
                        result = DeleteEmergencyResponse(404, "'response.body()' is null")
                    }
                    else {
                        Log.i("API Log: Success", "deleteEmergency 함수 실행 성공 ${result?.message}")
                        result = Gson().fromJson(
                            jsonString,
                            DeleteEmergencyResponse::class.java
                        )
                    }
                } catch(e: IOException){
                    Log.e("API Log: IOException", "deleteEmergency: ${e.message}(e.message)")
                    result = DeleteEmergencyResponse(404, "IOException: 네트워크 연결 실패")
                }
            }
        }
        return result?:DeleteEmergencyResponse(500, "에러원인 찾을 수 없음", null)
    }

    fun getAllEmergency(userId:Int):EmergencyListResponse{
        var result: EmergencyListResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl(nncBackendEmergencyUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(EmergencyService::class.java)
                        .getAllEmergency(userId).execute()
                    val jsonString = response.body()?.string()?:"response is null"

                    // 반환 실패에 대한 에러처리
                    if(jsonString == "response is null"){
                        result = EmergencyListResponse(404, "'response.body()' is null", null)
                        Log.e("API Log: response(Null)", "getAllEmergency: "+result?.message)
                    }
                    else {
                        Log.i("API Log: Success", "getAllEmergency 함수 실행 성공 ${result?.message}")
                        result = Gson().fromJson(
                            jsonString,
                            EmergencyListResponse::class.java
                        )
                    }
                } catch(e: IOException){
                    Log.e("API Log: IOException", "getAllEmergency: ${e.message}(e.message)")
                    result = EmergencyListResponse(404, "IOException: 네트워크 연결 실패")
                }
            }
        }
        return result?: EmergencyListResponse(500, "에러 원인을 찾을 수 없음", null)
    }
}