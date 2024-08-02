package com.hansung.sherpa.transit

data class OdsayTransitRouteResponse (
    val result: OdsayResult? = null
)

data class OdsayResult(
    val searchType: Int,
    val outTrafficCheck: Int,
    val busCount: Int,
    val subwayCount: Int,
    val subwayBusCount: Int,
    val pointDistance: Int,
    val startRadius: Int,
    val path: List<OdsayPath>,
)

data class OdsayPath(
    val pathType: Int,
    val info: OdsayInfo,
    val subPath: List<OdsaySubPath>,
)

data class OdsayInfo(
    val trafficDistance: Double,
    val totalWalk: Int,
    val totalTime: Int,
    val payment: Int,
    val busTransitCount: Int,
    val subwayTransitCount: Int,
    val mapObj: String,
    val firstStartStation: String,
    val firstStartStationKor: String,
    val firstStartStationJpnKata: String,
    val lastEndStation: String,
    val lastEndStationKor: String,
    val lastEndStationJpnKata: String,
    val totalStationCount: Int,
    val busStationCount: Int,
    val subwayStationCount: Int,
    val totalDistance: Double,
    val checkIntervalTime: Int,
    val checkIntervalTimeOverYn: String,
    val totalIntervalTime: Int,
)

data class OdsaySubPath(
    val trafficType: Int,
    val distance: Double,
    val sectionTime: Int,
    val stationCount: Int? = null,
    val lane: List<OdsayLane>? = null,
    val intervalTime: Int,
    val startName: String,
    val startNameKor: String,
    val startNameJpnKata: String,
    val startX: Double,
    val startY: Double,
    val endName: String,
    val endNameKor: String,
    val endNameJpnKata: String,
    val endX: Double,
    val endY: Double,
    val way: String? = null,
    val wayCode: Int? = null,
    val door: String? = null,
    val startID: Int,
    val startStationCityCode: Int? = null,
    val startStationProviderCode: Int? = null,
    val startLocalStationID: String? = null,
    val startArsID: String? = null,
    val endID: Int,
    val endStationCityCode: Int? = null,
    val endStationProviderCode: Int? = null,
    val endLocalStationID: String? = null,
    val endArsID: String? = null,
    val startExitNo: String? = null,
    val startExitX: Double? = null,
    val startExitY: Double? = null,
    val endExitNo: String? = null,
    val endExitX: Double? = null,
    val endExitY: Double? = null,
    val passStopList: OdsayPassStopList,
)

data class OdsayLane(
    val name: String? = null,
    val nameKor: String? = null,
    val nameJpnKata: String? = null,
    val busNo: String? = null,
    val busNoKor: String? = null,
    val busNoJpnKata: String? = null,
    val type: Int? = null,
    val busID: Int? = null,
    val busLocalBlID: String? = null,
    val busCityCode: Int? = null,
    val busProviderCode: Int? = null,
    val subwayCode: Int? = null,
    val subwayCityCode: Int? = null,
)

data class OdsayPassStopList(
    val stations: List<OdsayStations>,
)

data class OdsayStations(
    val index: Int,
    val stationID: Int,
    val stationName: String,
    val stationNameKor: String,
    val stationNameJpnKata: String,
    val stationCityCode: Int? = null,
    val stationProviderCode: Int? = null,
    val localStationID: String? = null,
    val arsID: String? = null,
    val x: String,
    val y: String,
    val isNonStop: String? = null,
)

