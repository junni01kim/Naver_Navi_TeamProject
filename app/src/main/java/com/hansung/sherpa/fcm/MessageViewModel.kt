package com.hansung.sherpa.fcm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hansung.sherpa.schedule.Schedule
import com.naver.maps.geometry.LatLng

/**
 * Fcm을 통해 메세지가 도착하였을 때 전송할 메세지 ViewModel
 *
 * @property showDialog MessageDialog를 띄우기 위한 flag 함수
 * @property title 메세지 제목
 * @property description 메세지 내용
 *
 */
class MessageViewModel : ViewModel() {

    private val _showDialog = MutableLiveData(false)
    val showDialog: LiveData<Boolean> get() = _showDialog

    private val _title = MutableLiveData("")
    val title: LiveData<String> get() = _title

    private val _body = MutableLiveData("")
    val body: LiveData<String> get() = _body

    /**
     * SherpaFirebaseMessageService를 통해 받은 알림을 새로운 Message 알림으로 디코딩하는 함수
     *
     * @param title 전송 받은 메세지의 제목
     * @param body 전송 받은 메세지의 내용
     *
     */
    fun updateValue(title: String, body: String) {
        _showDialog.postValue(true)
        _title.postValue(title)
        _body.postValue(body)
    }

    /**
     * MessageDialog를 닫기 위한 함수
     *
     */
    fun onDialogDismiss() {
        _showDialog.postValue(false)
    }
}