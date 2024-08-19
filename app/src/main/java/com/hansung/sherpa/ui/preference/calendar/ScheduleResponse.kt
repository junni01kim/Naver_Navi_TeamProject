package com.hansung.sherpa.ui.preference.calendar

data class ScheduleResponse(
    var title: String,
    var scheduledLocation: ScheduleLocation,
    var isWholeDay: Boolean,
    var startDateTime: Long,
    var endDateTime: Long,
    var repeat: Repeat,
    var comment : String
)