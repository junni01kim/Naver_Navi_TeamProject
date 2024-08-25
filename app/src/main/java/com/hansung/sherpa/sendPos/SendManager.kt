package com.hansung.sherpa.sendPos

import android.util.Log
import androidx.navigation.NavController
import com.google.gson.Gson
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.SherpaScreen
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.navigation.Navigation
import com.naver.maps.geometry.LatLng
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val nncBackendUserUrl = BuildConfig.SHERPA_URL

class SendManager(val navController: NavController? = null) {
    var myPos = LatLng(0.0, 0.0)

    /**
     * 내 위치(사용자 위치)를 서버에게 전달해주는 함수
     */
    fun sendMyPos(_myPos: LatLng) {
        myPos = _myPos
        val request = SendRequest("eSGfk_33RIG5PTTb0rsCUM:APA91bG4Jc-xJtzK52Xz7h_Vhd6wg-5as7i_oo0qXcoVNNfTyPiUOu9RIH9TTtZOn237T3JjDd1qpaaIc6syrmNEZvKqpWJLR4WpsIUxs0TcwU3CrQYpXXXWeSG7gxqXGVwXBhlepuLc","위치/사용자위치",Gson().toJson(myPos))

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
        // TODO: 1. body에서 목적지 받아오기
        val destinationLatLng = LatLng(37.1115, 127.0106) // 임시
        StaticValue.transportRoute = Navigation().getDetailTransitRoutes(myPos,destinationLatLng)[0]
        // TODO: 2. 경로 안내 시작에 대한 다이얼로그 띄우기

        // TODO: 3. 화면 이동
        navController?.navigate(SherpaScreen.SpecificRoute.name)
    }

    fun getPos(title: String, body: String) {
        val caretakerPos = Gson().fromJson(body, LatLng::class.java)
        Log.d("FCM Log:getPos", "caretaker pos: $caretakerPos")
    }
}