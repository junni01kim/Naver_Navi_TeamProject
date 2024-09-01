package com.hansung.sherpa.schedule

import com.google.gson.annotations.SerializedName

data class ScheduleResponse(
    @SerializedName("code"    ) var code    : Int,
    @SerializedName("message" ) var message : String,
    @SerializedName("data"    ) var data    : List<Schedules>
)