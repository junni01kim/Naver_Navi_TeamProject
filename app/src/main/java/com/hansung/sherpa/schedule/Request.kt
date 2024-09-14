package com.hansung.sherpa.schedule

import com.google.gson.annotations.SerializedName

/**
 * @param scheduleId 스케줄 아이디
 * @param userId 사용자 아이디
 * @param title 일정 제목
 * @param dateBegin 시작 시간
 * @param dateEnd 종료 시간
 * @param description 스케줄 설명
 * @param routeId 경로 아이디
 * @param address 주소
 * @param isWholeday 하루 종일인지
 * @param guideDatetime 경로 안내 시작 시간
 */
data class Schedules(
    @SerializedName("scheduleId") val scheduleId : Int?,
    @SerializedName("userId") val userId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("dateBegin") val dateBegin : String,
    @SerializedName("dateEnd") val dateEnd : String,
    @SerializedName("description") val description : String,
    @SerializedName("routeId") val routeId : Int?,
    @SerializedName("address") val address : String,
    @SerializedName("isWholeday") val isWholeday : Boolean,
    @SerializedName("guideDatetime") val guideDatetime : String?
)

/**
 * @param name 경로 이름
 * @param latitude lat
 * @param longitude lon
 */
data class Location(
    @SerializedName("name") val name : String,
    @SerializedName("latitude") val latitude : Double,
    @SerializedName("longitude") val longitude : Double,
)

/**
 * @param location 위치정보
 * @param cron 미정
 */
data class Route(
    @SerializedName("location") val location : Location,
    @SerializedName("cron") val cron : String,
)