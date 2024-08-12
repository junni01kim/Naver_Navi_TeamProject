package com.hansung.sherpa.FCM

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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