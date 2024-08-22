package com.hansung.sherpa.user.login

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("login")
    fun postLoginService(@Body body: LoginRequest): Call<ResponseBody>
}