package com.hansung.sherpa.user.updateFcm

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH

interface UpdateFcmService {
    @PATCH("updateFcm")
    fun patchUpdateFcmService(@Body body: UpdateFcmRequest): Call<ResponseBody>
}