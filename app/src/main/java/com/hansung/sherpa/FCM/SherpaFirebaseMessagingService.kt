package com.hansung.sherpa.FCM

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hansung.sherpa.StaticValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SherpaFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var viewModel: MessageViewModel

    override fun onCreate() {
        super.onCreate()
        // ApplicationContext를 사용하여 ViewModel을 초기화
        val application = applicationContext as Application
        viewModel = ViewModelProvider(StaticValue.mainActivity).get(MessageViewModel::class.java)
    }

    /**
     * FCM token이 만료되어 재발급이 되는 경우에 해당 메서드를 통해 새로운 token을 받게 된다.
     */
    override fun onNewToken(token: String){
        Log.d("FCMLog", "Refreshed token: "+token);
    }

    /**
     * 메세지가 들어오는 공간
     */
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("FCMLod", "뭔가 전송됨")

        // notification
        if(message.notification != null) { //포그라운드
            branch(message.notification?.title!!, message.notification?.body!!)
        }

        // data
        if (message.data.isNotEmpty()) { //백그라운드
            message.data["title"]?.let { branch(it, message.data["body"]!!) }
        }
    }

    /**
     * 분기를 위한 함수
     * ※ 토큰 방식과 토픽 방식 같이 사용하지 못해서, title 머리를 토픽으로 사용하면 좋을 것 같아서 만듦
     */
    private fun branch(head: String, body: String) {
        Log.d("FCMLog", "branch 메서드: 수신 완료")

        Log.d("FCMLog", "$head, $body")
        val parts = head.split("/")
        val topic = parts[0]
        val title = parts[1]
        viewModel.updateValue(title, body)

        when(topic) {
            "예시" -> Log.d("FCMLog","FCM: title(${title}) body(${body})")
            "일정" -> Log.d("FCMLog","FCM: 일정 객체 ${body}")
            "예약경로" -> Log.d("FCMLog", "FCM: 경로 안내 객체 ${body}")
            "로그인" -> Log.d("FCMLog", "FCM: 로그인 성공 ${body}")
            "긴급 연락처" -> Log.d("FCMLog", "FCM: 긴급 연락처 받기 ${body}")
            else -> Log.d("FCMLog", "FCM: message 형식 오류")
        }
    }
}

class MessageViewModel : ViewModel() {
    private var _title = MutableStateFlow("")
    private var _body = MutableStateFlow("")

    var title: StateFlow<String> = _title
    var body: StateFlow<String> = _body

    fun updateValue(title:String, body:String){
        _title.value = title
        _body.value = body
    }
}