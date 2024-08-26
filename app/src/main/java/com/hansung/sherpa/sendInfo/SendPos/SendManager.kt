package com.hansung.sherpa.sendInfo.SendPos

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.StaticValue
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
    fun sendPosToToken(myPos: LatLng) {
        val request = SendRequest("위치/사용자위치",Gson().toJson(myPos), "eSGfk_33RIG5PTTb0rsCUM:APA91bG4Jc-xJtzK52Xz7h_Vhd6wg-5as7i_oo0qXcoVNNfTyPiUOu9RIH9TTtZOn237T3JjDd1qpaaIc6syrmNEZvKqpWJLR4WpsIUxs0TcwU3CrQYpXXXWeSG7gxqXGVwXBhlepuLc")

        Retrofit.Builder()
            .baseUrl(nncBackendUserUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SendService::class.java)
            .postSendServiceToToken(request).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("FCM Log", "${response.code()}")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
            })
    }

    fun sendPos(myPos: LatLng) {
        val request = SendRequest("위치/위치좌표",Gson().toJson(myPos))

        Retrofit.Builder()
            .baseUrl(nncBackendUserUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SendService::class.java)
            .postSendService(StaticValue.userInfo.userId?:-1,request).enqueue(object : Callback<ResponseBody> {
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