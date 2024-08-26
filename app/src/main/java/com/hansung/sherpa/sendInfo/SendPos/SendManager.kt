package com.hansung.sherpa.sendInfo.SendPos

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.BuildConfig
import com.naver.maps.geometry.LatLng
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val nncBackendUserUrl = BuildConfig.SHERPA_URL

class SendManager() {
    /**
     * 내 위치(사용자 위치)를 서버에게 전달해주는 함수
     */
    fun sendMyPos(myPos: LatLng) {
        val request = SendRequest("e5iWfEMESnW1uJJgHsuZYt:APA91bF2tWJ08VfXN-xmV6SdTUNJu_M4b4l0vXcHlZsQ-DqJvc3pkfPXot4xxV3LC3HKiWccqMY195PZlo6fKnGfLbgPS7w0hIMd-b-R4IDbnfZmjRpY3ykRNc2OzPPTMC1fqIFUDdev","위치/사용자위치",Gson().toJson(myPos))

        Retrofit.Builder()
            .baseUrl(nncBackendUserUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SendService::class.java)
            .postSendService(request).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("FCM Log", "${response.code()}")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
            })
    }
}