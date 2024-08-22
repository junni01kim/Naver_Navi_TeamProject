package com.hansung.sherpa.user

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    /**
     * 계정을 생성하는 API
     */
    @POST("createUser")
    fun postCreateService(@Body body: LoginRequest): Call<ResponseBody>

    /**
     * 계정 로그인(사용자 인증) 하는 API
     */
    @POST("login")
    fun postLoginService(@Body body: LoginRequest): Call<ResponseBody>

    /**
     * 사용자 정보를 얻는 API
     */
    @GET("getUser/{userId}")
    fun getUser(@Path("userId") userId:Int): Call<ResponseBody>

    /**
     * 보호자 인증을 요청하는 API
     */
    @GET("getLinkPermission/{caregiverEmail}")
    fun getLinkPermissionService(@Body body: String): Call<ResponseBody>

    /**
     * 사용자와 보호자의 관계를 얻는 API
     */
    @GET("getUserRelation/{userId}")
    fun getRelationService(@Path("userId") userId:Int): Call<ResponseBody>
}
