package com.hansung.sherpa.user.relation

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetUserRelationService {
    @GET("getUserRelation/{userId}")
    fun getUserRelationService(@Path("userId") userId:Int): Call<ResponseBody>
}