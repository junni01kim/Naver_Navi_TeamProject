package com.hansung.sherpa.user

import android.util.Log
import com.google.gson.Gson
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
                        .baseUrl("http://localhost:8080/api/v1/user/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(CreateUserService::class.java)
                        .getLoginService(request.getMap()).execute()
                    result = Gson().fromJson(response.body()!!.string(), CreateUserResponse::class.java)
                } catch(e:IOException){
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
        return result
    }
}