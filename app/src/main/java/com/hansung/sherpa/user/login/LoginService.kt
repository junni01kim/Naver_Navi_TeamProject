package com.hansung.sherpa.user.login

import com.hansung.sherpa.user.createuser.CreateUserRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("login")
    fun postLoginService(@Body body: LoginRequest): Call<ResponseBody>
}