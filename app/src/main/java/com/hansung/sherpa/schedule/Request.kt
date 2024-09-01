package com.hansung.sherpa.schedule

import com.google.gson.annotations.SerializedName

data class Schedules(
    @SerializedName("scheduleId") val scheduleId : Int?,
    @SerializedName("userId") val userId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("dateBegin") val dateBegin : String,
    @SerializedName("dateEnd") val dateEnd : String,
    @SerializedName("description") val description : String,
    @SerializedName("routeId") val routeId : Int?,
    @SerializedName("address") val address : String,
    @SerializedName("isWholeDay") val isWholeDay : Int,
    @SerializedName("guideDatetime") val guideDatetime : String
)

data class Location(
    @SerializedName("name") val name : String,
    @SerializedName("latitude") val latitude : Double,
    @SerializedName("longitude") val longitude : Double,
)

data class Route(
    @SerializedName("location") val location : Location,
    @SerializedName("cron") val cron : String,
)