package com.hansung.sherpa.user

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface CreateUserService {
    @POST("createUser")
    fun postLoginService(@Body body: CreateUserRequest): Call<ResponseBody>
}
