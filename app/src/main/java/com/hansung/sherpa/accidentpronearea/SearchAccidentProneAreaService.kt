package com.hansung.sherpa.accidentpronearea

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SearchAccidentProneAreaService {
    @Headers("Accept: */*")
    @POST("accident_prone_area/")
    fun search(@Body body : AccidentProneAreaReqeust): Call<AccidentProneAreaResponse>
}