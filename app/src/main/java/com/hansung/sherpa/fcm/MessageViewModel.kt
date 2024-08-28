package com.hansung.sherpa.fcm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hansung.sherpa.schedule.Schedule
import com.naver.maps.geometry.LatLng

class MessageViewModel : ViewModel() {

    private val _showDialog = MutableLiveData(false)
    val showDialog: LiveData<Boolean> get() = _showDialog

    private val _title = MutableLiveData("")
    val title: LiveData<String> get() = _title

    private val _body = MutableLiveData("")
    val body: LiveData<String> get() = _body

    // livedata 필드에 업데이트 알리는 함수
    fun updateValue(title: String, body: String) {
        _showDialog.postValue(true)
        _title.postValue(title)
        _body.postValue(body)
    }

    fun updateSchedule(title: String, body: String){
        val schedule = Gson().fromJson(body, Schedule::class.java)

        _showDialog.postValue(true)
        _title.postValue("${schedule.title}")
        _body.postValue("${schedule.description}")
    }

    // 다이얼로그 닫기
    fun onDialogDismiss() {
        _showDialog.postValue(false)
    }
}