package com.hansung.sherpa.FCM

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MessageViewModel : ViewModel() {
    private val _showDialog = MutableLiveData(false)
    val showDialog: LiveData<Boolean> get() = _showDialog

    private val _title = MutableLiveData("")
    val title: LiveData<String> get() = _title

    private val _body = MutableLiveData("")
    val body: LiveData<String> get() = _body

    // livedata 필드에 업데이트 알리는 함수
    fun updateValue(title: String, body: String) {
        _title.postValue(title)
        _body.postValue(body)
    }

    // 데이터 수신 : 값 업데이트, 다이얼로그 띄우기
    fun onMessageReceived(title: String, body: String) {
        branch(title, body)
        _showDialog.postValue(true)
    }

    // 다이얼로그 닫기
    fun onDialogDismiss() {
        _showDialog.postValue(false)
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

        when (topic) {
            "알림" -> this.updateValue(title, body)
            "예시" -> Log.d("FCMLog", "FCM: title(${title}) body(${body})")
            "일정" -> Log.d("FCMLog", "FCM: 일정 객체 ${body}")
            "예약경로" -> Log.d("FCMLog", "FCM: 경로 안내 객체 ${body}")
            "로그인" -> Log.d("FCMLog", "FCM: 로그인 성공 ${body}")
            "긴급 연락처" -> Log.d("FCMLog", "FCM: 긴급 연락처 받기 ${body}")
            else -> Log.d("FCMLog", "FCM: message 형식 오류")
        }
    }
}