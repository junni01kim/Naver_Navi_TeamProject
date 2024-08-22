package com.hansung.sherpa.user.createuser

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @POST("createUser")
    fun postLoginService(@Body body: CreateUserRequest): Call<ResponseBody>

    @GET("getUser/{userId}")
    fun getUser(@Path("userId") userId:Int): Call<ResponseBody>
}
