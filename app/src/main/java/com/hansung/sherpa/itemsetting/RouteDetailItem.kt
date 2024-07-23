package com.hansung.sherpa.itemsetting

/**
 * 선택된 세부 경로의 정보들
 * @param fromName  출발지의 이름
 * @param toName 도착지의 이름
 * @param summary 세부 정보의 요약 정보 (도보 150m or 버스정류장 n개)
 * @param time 소요시간
 */
data class RouteDetailItem(
    val fromName:String,
    val toName:String,
    val summary:String,
    val time:String
)