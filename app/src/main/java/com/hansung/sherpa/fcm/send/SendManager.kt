package com.hansung.sherpa.fcm.send

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.searchlocation.SearchLocationResponse
import com.naver.maps.geometry.LatLng
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

val nncBackendUserUrl = BuildConfig.SHERPA_URL
val udpPort = 8000

class SendManager {
    var myPos = LatLng(0.0, 0.0)

    /**
     * 내 위치(사용자 위치)를 서버에게 전달해주는 함수
     * ※ UDP 방식 연구중
     * TODO: 비동기 방식으로 전송할 것
     */
    fun sendMyPosUdp(_myPos: LatLng) {
        myPos = _myPos

        // TODO: 1. 보호자 정보 혹은 사용자Id를 함께 전달해준다.
        val request = SendRequest("","위치/사용자위치",Gson().toJson(myPos))
        val serializationRequest = Gson().toJson(request)

        try {
            val socket = DatagramSocket()
            socket.broadcast = true
            val sendData = serializationRequest.toByteArray()
            val sendPacket = DatagramPacket(sendData, sendData.size, InetAddress.getByName(BuildConfig.SHERPA_URL), udpPort)
            socket.send(sendPacket)
        } catch (e: IOException) {
            Log.e("API Log: IOException", "SendManager.sendMyPos: ${e.message}(e.message)")
        }
    }

    /**
     * 내 위치(사용자 위치)를 서버에게 전달해주는 함수
     */
    fun sendMyPos(_myPos: LatLng) {
        myPos = _myPos
        val request = SendRequest("","위치/사용자위치",Gson().toJson(myPos))

        Retrofit.Builder()
            .baseUrl(nncBackendUserUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SendService::class.java)
            .getSendService(request).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {}

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
            })
    }

    /**
     * 일정 시간이 되었을 때 실행 될 함수
     */
    fun scheduleStart(title: String, body: String) {
        // TODO: 1. body 데이터 파싱
        // TODO: 2. 일정 정보 다이얼로그 띄우기
        // TODO: 3. 휴대폰 팝업 메세지 띄우기
    }

    /**
     * 경로 안내 시간이 되었을 때 실행 될 함수
     */
    fun navigationStart(title: String, body: String) {
        // TODO: 1. body 데이터 파싱
        // TODO: 2. 경로 안내 시작에 대한 다이얼로그 띄우기
        // TODO: 3. 화면 이동
    }
}