package com.hansung.sherpa.fcm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hansung.sherpa.schedule.Schedule

/**
 * Fcm을 통해 스케쥴이 도착하였을 때 전송할 스케쥴 ViewModel
 *
 * @property showDialog ScheduleDialog를 띄우기 위한 flag 함수
 * @property title 일정 제목
 * @property description 일정 설명
 * @property dateBegin 일정 시작
 * @property dateEnd 일정 종료
 *
 */
class ScheduleViewModel : ViewModel() {
    private val _showDialog = MutableLiveData(false)
    val showDialog: LiveData<Boolean> get() = _showDialog

    private val _title = MutableLiveData("")
    val title: LiveData<String> get() = _title

    private val _description = MutableLiveData("")
    val description: LiveData<String> get() = _description

    private val _dateBegin = MutableLiveData("")
    val dateBegin: LiveData<String> get() = _dateBegin

    private val _dateEnd = MutableLiveData("")
    val dateEnd: LiveData<String> get() = _dateEnd

    /**
     * SherpaFirebaseMessageService를 통해 받은 알림을 새로운 Schedule 알림으로 디코딩하는 함수
     *
     * @param body 전송 받은 메세지의 내용(Schedule 클래스 json 반환)
     */
    fun updateSchedule(body: String){
        val schedule = Gson().fromJson(body, Schedule::class.java)

        _showDialog.postValue(true)
        _title.postValue("${schedule.title}")
        _description.postValue("${schedule.description}")
        _dateBegin.postValue("${schedule.dateBegin}")
        _dateEnd.postValue("${schedule.dateEnd}")
    }

    /**
     * ScheduleDialog를 닫기 위한 함수
     *
     */
    fun dialogClose() {
        _showDialog.postValue(false)
    }
}