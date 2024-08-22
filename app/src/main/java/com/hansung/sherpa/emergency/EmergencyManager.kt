package com.hansung.sherpa.emergency

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class EmergencyManager {
    fun insertEmergency(request: Emergency):Emergency? {
        var result: EmergencyResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl("http://13.209.212.166:8080/api/v1/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(EmergencyService::class.java)
                        .postAddEmergencyService(request).execute()
                    result = Gson().fromJson(response.body()!!.string(), EmergencyResponse::class.java)
                } catch(e: IOException){
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
        Log.d("FCMLog", "insertEmergency 함수 실행 성공 ${result?.data?.emergencyId}")
        return result?.data
    }

    fun deleteEmergency(emergencyId:Int) {
        var result: DeleteEmergencyResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl("http://13.209.212.166:8080/api/v1/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(EmergencyService::class.java)
                        .deleteEmergencyService(emergencyId).execute()
                    result = Gson().fromJson(response.body()!!.string(), DeleteEmergencyResponse::class.java)
                } catch(e: IOException){
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
        Log.d("FCMLog", "insertEmergency 함수 실행 성공 ${result?.code}")
    }

    fun getAllEmergency(userId:Int):MutableList<Emergency>?{
        var result: MutableList<Emergency>? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl("http://13.209.212.166:8080/api/v1/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(EmergencyService::class.java)
                        .getAllEmergency(userId).execute()
                    val resultString = response.body()
                    if(resultString == null) result = mutableListOf()
                    else result = Gson().fromJson(resultString.string(), EmergencyListResponse::class.java).data
                } catch(e: IOException){
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
        Log.d("FCMLog", "insertEmergency 함수 실행 성공 ${result?.get(0)?.name}")
        return result
    }
}