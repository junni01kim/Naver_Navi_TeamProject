package com.hansung.sherpa.fcm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hansung.sherpa.schedule.Schedule

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

    fun updateSchedule(title: String, body: String){
        val schedule = Gson().fromJson(body, Schedule::class.java)

        _showDialog.postValue(true)
        _title.postValue("${schedule.title}")
        _description.postValue("${schedule.description}")
        _dateBegin.postValue("${schedule.dateBegin}")
        _dateEnd.postValue("${schedule.dateEnd}")
    }

    fun onDialogDismiss() {
        _showDialog.postValue(false)
    }
}