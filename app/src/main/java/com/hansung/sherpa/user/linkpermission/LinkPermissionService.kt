package com.hansung.sherpa.user.linkpermission

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LinkPermissionService {
    @GET("getLinkPermission")
    fun getLinkPermissionService(@Query("caregiverEmail") caregiverEmail: String): Call<ResponseBody>
}