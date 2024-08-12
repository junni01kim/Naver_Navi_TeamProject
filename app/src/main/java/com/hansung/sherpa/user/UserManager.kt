package com.hansung.sherpa.user

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.user.createuser.CreateUserRequest
import com.hansung.sherpa.user.createuser.CreateUserResponse
import com.hansung.sherpa.user.createuser.CreateUserService
import com.hansung.sherpa.user.linkpermission.LinkPermissionResponse
import com.hansung.sherpa.user.linkpermission.LinkPermissionService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserManager {
    fun create(request: CreateUserRequest): CreateUserResponse? {
        var result: CreateUserResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl("http://13.209.212.166:8080/api/v1/user/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(CreateUserService::class.java)
                        .postLoginService(request).execute()
                    result = Gson().fromJson(response.body()!!.string(), CreateUserResponse::class.java)
                } catch(e:IOException){
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
        Log.d("FCMLog", "create 함수 실행 성공 ${result?.code}")
        return result
    }

    fun linkPermission(caregiverEmail: String):LinkPermissionResponse? {
        var result: LinkPermissionResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl("http://13.209.212.166:8080/api/v1/user/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(LinkPermissionService::class.java)
                        .getLinkPermissionService(caregiverEmail).execute()
                    result = Gson().fromJson(response.body()!!.string(), LinkPermissionResponse::class.java)
                } catch(e:IOException){
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
        Log.d("FCMLog", "create 함수 실행 성공 ${result?.code}")
        return result
    }
}