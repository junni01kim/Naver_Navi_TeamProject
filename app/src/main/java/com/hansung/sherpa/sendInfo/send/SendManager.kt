package com.hansung.sherpa.sendInfo.send

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.itemsetting.SubPath
import com.hansung.sherpa.itemsetting.SubPathAdapter
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.sendInfo.receive.ReceiveRouteResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ColorPart
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val nncBackendUserUrl = BuildConfig.SHERPA_URL

class SendManager() {
    val userId = StaticValue.userInfo.userId?.toString() ?: "-1"

    /**
     * 상대방에게 위치를 전달하는 함수 FireBase로 전달한다.
     *
     * @param myPos 내 위치
     * @param table 변경 할 테이블 명
     */
    fun sendPosition(myPos: LatLng, table: String = "current_position") {
        StaticValue.ref
            .child(table)
            .child(userId)
            .setValue(Gson().toJson(myPos))
    }

    /**
     * 상대방에게 위치 및 경로 안내 진행도를 전달하는 함수 FireBase로 전달한다.
     *
     * @param myPos 내 위치
     * @param passedRoute 자신의 경로 진행도
     */
    fun sendPositionAndPassedRoute(myPos: LatLng, passedRoute:SnapshotStateList<Double>) {
        sendPosition(myPos, "moving_position")
        StaticValue.ref
            .child("passed_route")
            .child(userId)
            .setValue(passedRoute)
    }

    /**
     * 경로를 이탈한 후 재탐색한 경로로 경로를 수정하는 함수
     *
     * @param coordParts 현재 이동중인 내 경로 리스트
     * @param colorParts 내 경로의 타입 별 색상
     */
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

    /**
     * 경로 안내 받고 싶은 SpecificRoute로 상대방을 불러오는 함수.
     * ※ 만약 Role1이 사용자라면 이후에 내비게이션 안내가 시작된다.
     *
     * @param transportRoute 현재 이용자가 경로 탐색한 경로
     */
    fun startNavigation(transportRoute: TransportRoute) {
        val gson = GsonBuilder().registerTypeAdapter(SubPath::class.java, SubPathAdapter()).create()
        val json = gson.toJson(transportRoute)

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

    /**
     * 사용자의 경로 안내를 종료하는 함수
     *
     * [Sherpa 내부 서버] Navigation Table 레코드를 삭제한다.
     */
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