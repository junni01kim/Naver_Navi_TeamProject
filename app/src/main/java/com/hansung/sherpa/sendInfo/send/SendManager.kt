package com.hansung.sherpa.sendInfo.send

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.Gson
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.sendInfo.receive.ReceivePos
import com.hansung.sherpa.sendInfo.receive.ReceiveRouteResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ColorPart
import com.owlike.genson.Genson
import com.owlike.genson.GensonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val nncBackendUserUrl = BuildConfig.SHERPA_URL

class SendManager() {
    /**
     * 아래 기술 된 함수들과 오버로딩이 가능하지만, 각 함수의 동작을 정확하게 명시하고 싶어 다르게 구현
     */
    fun sendPos(myPos: LatLng) {
        val request = SendRequest("위치/myPos",Gson().toJson(myPos))

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
                    //Log.d("FCM Log", "sendPos: ${response.code()}")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
            })
    }

    fun sendPos(myPos: LatLng, passedRoute:SnapshotStateList<Double>) {
        val request = SendRequest("경로이동/Pair",Gson().toJson(ReceivePos(myPos,passedRoute)))

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
                    Log.d("FCM Log", "sendPos(경로이동): ${response.code()}")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
            })
    }

    fun devateRoute(coordParts:SnapshotStateList<MutableList<LatLng>>, colorParts: MutableList<ColorPart>) {
        val json = Gson().toJson(ReceiveRouteResponse(coordParts,colorParts))

        val temp = json.toByteArray(Charsets.UTF_8)
        val request = temp.joinToString("") { String.format("%02X", it) }

        Retrofit.Builder()
            .baseUrl(nncBackendUserUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SendService::class.java)
            .patchUpdateRoute(StaticValue.userInfo.userId?:-1, request)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("FCM Log", "devateRoute: ${response.code()}")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
            })
    }

    fun startNavigation(transportRoute: TransportRoute) {
        val json = Gson().toJson(transportRoute)

        val temp = json.toByteArray(Charsets.UTF_8)
        val request = temp.joinToString("") { String.format("%02X", it) }

        Retrofit.Builder()
            .baseUrl(nncBackendUserUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SendService::class.java)
            .postCreateNavigation(StaticValue.userInfo.userId?:-1,request)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("FCM Log", "startNavigation: ${response.code()}")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
            })
    }

    fun deleteNavigation() {
        Retrofit.Builder()
            .baseUrl(nncBackendUserUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SendService::class.java)
            .deleteRoute(StaticValue.userInfo.userId?:-1)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("FCM Log", "deleteNavigation: ${response.code()}")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
            })
    }
}