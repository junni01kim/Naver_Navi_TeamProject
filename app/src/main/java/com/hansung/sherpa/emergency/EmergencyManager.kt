package com.hansung.sherpa.emergency

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.user.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

val nncBackendEmergencyUrl = BuildConfig.SHERPA_URL

class EmergencyManager {
    /**
     * [Sherpa 내부 서버] 긴급 연락처 추가 함수
     *
     * @param request 추가할 Emergency 객체
     *
     * @return ※ data는 무조건 null을 반환한다.
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
                        Log.e("API Log: response(Null)", "insertEmergency: 'response.body()' is null")
                        result = EmergencyResponse(404, "'response.body()' is null")
                    }
                    else {
                        Log.i("API Log: Success", "insertEmergency 함수 실행 성공 ${result?.message}")
                        result = Gson().fromJson(jsonString, EmergencyResponse::class.java)
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
     * [Sherpa 내부 서버] 긴급 연락처 삭제 함수
     *
     * @param emergencyId 삭제할 긴급 연락처 emergancyId
     *
     * @return ※ data는 무조건 null을 반환한다.
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
                        Log.e("API Log: response(Null)", "deleteEmergency: 'response.body()' is null")
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

    /**
     * [Sherpa 내부 서버] userId 가 가진 모든 긴급 연락처를 조회하는 함수
     *
     * @param userId 조회할 사용자의 Id
     *
     * @return userId 사용자의 모든 긴급연락처
     */
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
                        Log.e("API Log: response(Null)", "getAllEmergency: 'response.body()' is null")
                        result = EmergencyListResponse(404, "'response.body()' is null", null)
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

    /**
     * [Sherpa 내부 서버] 긴급 연락처를 홈 화면에 띄우는 로직
     *
     * @param emergencyId 홈화면에서 이용할 긴급 연락처 emergencyId
     *
     * @return ???
     */
    fun updateEmergencyBookmark(emergencyId: Int): EmergencyResponse {
        var result: EmergencyResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl(nncBackendEmergencyUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(EmergencyService::class.java)
                        .updateEmergencyByEmergencyId(emergencyId).execute()
                    val jsonString = response.body()?.string()?:"response is null"

                    // 반환 실패에 대한 에러처리
                    if(jsonString == "response is null"){
                        result = EmergencyResponse(404, "'response.body()' is null", null)
                        Log.e("API Log: response(Null)", "updateEmergencyBookmark: "+result?.message)
                    }
                    else {
                        Log.i("API Log: Success", "updateEmergencyBookmark 함수 실행 성공 ${result?.message}")
                        result = Gson().fromJson(jsonString, EmergencyResponse::class.java)
                    }
                } catch(e: IOException){
                    Log.e("API Log: IOException", "updateEmergencyBookmark: ${e.message}(e.message)")
                    result = EmergencyResponse(404, "IOException: 네트워크 연결 실패")
                }
            }
        }
        return result?: EmergencyResponse(500, "에러 원인을 찾을 수 없음", null)
    }
}