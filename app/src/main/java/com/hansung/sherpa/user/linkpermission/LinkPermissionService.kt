package com.hansung.sherpa.user.linkpermission

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface LinkPermissionService {
    @GET("getLinkPermission/{caregiverEmail}")
    fun getLinkPermissionService(@Path("caregiverEmail") caregiverEmail: String): Call<ResponseBody>
}