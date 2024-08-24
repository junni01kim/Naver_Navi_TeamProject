package com.hansung.sherpa.fcm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hansung.sherpa.fcm.send.SendManager

class MessageViewModel : ViewModel() {
    val sendManager = SendManager()
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
        Log.i("FCM Log: Success", "branch 메서드: 수신 완료")

        Log.i("FCM Log: Data", "$head, $body")
        val parts = head.split("/")
        val topic = parts[0]
        val title = parts[1]

        when (topic) {
            "예시" -> Log.i("FCM Log: Message", "FCM: title(${title}) body(${body})")
            "알림" -> this.updateValue(title, body)
            "위치" -> Log.i("FCM Log: Location", "FCM: 사용자 경로 전송<UDP 사용 고려중>")
            "일정" -> sendManager.scheduleStart(title, body)
            "예약경로" -> sendManager.navigationStart(title, body)
            else -> Log.e("FCM Log: Error", "FCM: message 형식 오류")
        }
    }
}