package com.hansung.sherpa.itemsetting

/**
* 최상단에 보여질 전체 경로의 요약본
 * @param totalTime 전체 경로를 이동하는 데에 걸리는 시간
 * @param totalDistance 전체 경로의 거리
* */
class RouteDetailTotalSummary(
    val totalTime:String,
    val totalDistance:String
)

/**
 * 선택된 세부 경로의 정보들
 * @param fromName 출발지의 이름
 * @param toName 도착지의 이름
 * @param summary 세부 정보의 요약 정보 (도보 150m or 버스정류장 n개)
 * @param time 소요시간
 */
abstract class RouteDetailItem(
    var fromName:String,
    var toName:String,
    var summary:String,
    var time:String
)

/**
 * 선택된 세부 경로의 보행자 정보
 * @param fromName 출발지의 이름
 * @param toName 도착지의 이름
 * @param summary 세부 정보의 요약 정보 (도보 150m)
 * @param time 소요시간
 * @param contents 이동 보행 경로의 세부 내용
 */
class PedestrianRouteDetailItem(
    fromName:String,
    toName:String,
    summary:String,
    time:String,
    var contents:MutableList<String>
):RouteDetailItem(fromName, toName, summary, time)

/**
 * 선택된 세부 경로의 버스 정보
 * @param fromName 출발지의 이름
 * @param toName 도착지의 이름
 * @param summary 세부 정보의 요약 정보 (도보 150m)
 * @param time 소요시간
 * @param busNo 버스 번호
 * @param stations Bus 정류장들
 */
class BusRouteDetailItem(
    fromName:String,
    toName:String,
    summary:String,
    time:String,
    var busNo:String,
    var stations:MutableList<String>
):RouteDetailItem(fromName, toName, summary, time)

/**
 * 선택된 세부 경로의 버스 정보
 * @param fromName 출발지의 이름
 * @param toName 도착지의 이름
 * @param summary 세부 정보의 요약 정보 (도보 150m)
 * @param time 소요시간
 * @param subLine 호선 번호
 * @param stations Subway 정류장들
 */
class SubwayRouteDetailItem(
    fromName:String,
    toName:String,
    summary:String,
    time:String,
    var subLine:String,
    var stations:MutableList<String>
):RouteDetailItem(fromName, toName, summary, time)

