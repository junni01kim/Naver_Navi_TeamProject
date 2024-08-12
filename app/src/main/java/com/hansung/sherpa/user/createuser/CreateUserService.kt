package com.hansung.sherpa.user.createuser

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateUserService {
    @POST("createUser")
    fun postLoginService(@Body body: CreateUserRequest): Call<ResponseBody>
}
