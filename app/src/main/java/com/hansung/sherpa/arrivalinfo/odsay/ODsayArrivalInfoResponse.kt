package com.hansung.sherpa.arrivalinfo.odsay

class ODsayArrivalInfoResponse {
    val result: Result? = null
}

data class Result(
    val base: Base? = null,
    val real: List<Real>? = null
)

data class Base(
    val arsID: String? = null,
    val busOnlyCentralLane: Int? = null,
    val `do`: String? = null,
    val dong: String? = null,
    val gu: String? = null,
    val lane: List<Lane?>? = null,
    val localStationID: String? = null,
    val nonstopStation: Int? = null,
    val stationCityCode: String? = null,
    val stationID: Int? = null,
    val stationName: String? = null,
    val stationNameKor: String? = null,
    val x: Double? = null,
    val y: Double? = null
)

data class Real(
    val arrival1: Arrival? = null,
    val arrival2: Arrival? = null,
    val localRouteId: String? = null,
    val routeId: String? = null,
    val routeNm: String? = null,
    val stationSeq: String? = null,
    val updownFlag: String? = null
)

data class Lane(
    val busCityCode: Int? = null,
    val busCityName: String? = null,
    val busCityNameKor: String? = null,
    val busDirectionName: String? = null,
    val busDirectionNameKor: String? = null,
    val busDirectionStationID: Int? = null,
    val busDirectionType: Int? = null,
    val busEndPoint: String? = null,
    val busEndPointKor: String? = null,
    val busFirstTime: String? = null,
    val busID: Int? = null,
    val busInterval: String? = null,
    val busLastTime: String? = null,
    val busLocalBlID: String? = null,
    val busNo: String? = null,
    val busNoKor: String? = null,
    val busStartPoint: String? = null,
    val busStartPointKor: String? = null,
    val busStationIdx: Int? = null,
    val type: Int? = null
)

data class Arrival(
    val arrivalSec: Int? = null,
    val busPlateNo: String? = null,
    val busStatus: String? = null,
    val congestion: Int? = null,
    val endBusYn: String? = null,
    val fulCarAt: String? = null,
    val leftStation: Int? = null,
    val lowBusYn: String? = null,
    val nmprType: Int? = null,
    val waitStatus: String? = null
)