package com.hansung.sherpa.user

import com.hansung.sherpa.user.createuser.CreateUserRequest
import com.hansung.sherpa.user.login.LoginRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @GET("getUserRelation/{userId}")
    fun getRelationService(@Path("userId") userId:Int): Call<ResponseBody>

    @POST("createUser")
    fun postLoginService(@Body body: LoginRequest): Call<ResponseBody>

    @GET("getLinkPermission/{caregiverEmail}")
    fun getLinkPermissionService(@Body body: String): Call<ResponseBody>

    @POST("createUser")
    fun postLoginService(@Body body: CreateUserRequest): Call<ResponseBody>

    @GET("getUser/{userId}")
    fun getUser(@Path("userId") userId:Int): Call<ResponseBody>
}
