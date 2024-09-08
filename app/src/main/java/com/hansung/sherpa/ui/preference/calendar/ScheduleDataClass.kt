package com.hansung.sherpa.ui.preference.calendar

import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState

data class Repeat (
    var repeatable : Boolean,
    var cycle : String
)

data class ScheduleLocation (
    var name : String,
    var address : String,
    var lon: Double,
    var lat: Double,
    var isGuide: Boolean,
    var guideDatetime : Long
)

data class ScheduleData(
    var title: MutableState<String>,
    var scheduledLocation: ScheduleLocation,
    var isWholeDay: MutableState<Boolean>,
    var isDateValidate : MutableState<Boolean>,
    var startDateTime: MutableLongState,
    var endDateTime: MutableLongState,
//    var repeat: MutableState<Repeat>,
    var comment : MutableState<String>,
    var routeId : Int?,
    var scheduleId : Int
)
