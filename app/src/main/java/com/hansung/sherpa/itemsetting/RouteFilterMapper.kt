package com.hansung.sherpa.itemsetting

import com.hansung.sherpa.routegraphic.RouteGraphicResponse
import com.hansung.sherpa.transit.ODsaySubPath
import com.hansung.sherpa.transit.ODsayTransitRouteResponse
import com.hansung.sherpa.transit.PedestrianResponse
import com.naver.maps.geometry.LatLng

class RouteFilterMapper {

    enum class TrafficType(val value: Int) {
        SUBWAY(1),
        BUS(2),
        WALK(3)
    }

    // ODsay response 값에 대해서 TransportRoute 값 매핑 ( 보행자 데이터 없음 )
    fun mappingODsayResponseToTransportRouteList(
        transitResponse: ODsayTransitRouteResponse, graphicResponseList: List<RouteGraphicResponse>
    ): List<TransportRoute>? {
        if (transitResponse.result == null || graphicResponseList.isEmpty()) return null

        val list = mutableListOf<TransportRoute>()

        for (path in transitResponse.result.path) {
            val oDsayInfo = path.info
            val oDsayPath = path.subPath

            val info = Info(
                totalDistance   = oDsayInfo.totalDistance,
                totalTime       = oDsayInfo.totalTime,
                transferCount   = oDsayInfo.busTransitCount + oDsayInfo.subwayTransitCount,
                totalWalk       = oDsayInfo.totalWalk
            )
            val subPathList: List<SubPath> = oDsayPath.mapIndexed { index, it ->
                SubPath(
                    trafficType     = it.trafficType,
                    sectionInfo     = castSectionInfo(it.trafficType, it),
                    sectionRoute    = castSectionRoute(it.trafficType, graphicResponseList[index])
                )
            }

            val transportRoute = TransportRoute(
                info = info,
                subPath = subPathList
            )
            list.add(transportRoute)
        }
        return list.toList()
    }

    private fun castSectionInfo(trafficType: Int, path: ODsaySubPath): SectionInfo {
        return when (trafficType) {
            TrafficType.SUBWAY.value -> SubwaySectionInfo(
                lane = path.lane!!.map {
                    SubwayLane(
                        name            = it.name!!,
                        subwayCode      = it.subwayCode!!,
                        subwayCityCode  = it.subwayCityCode!!
                    )
                },
                way                         = path.way!!,
                wayCode                     = path.wayCode!!,
                door                        = path.door!!,
                stationCount                = path.stationCount!!,
                startID                     = path.startID,
                startStationProviderCode    = path.startStationProviderCode!!,
                startLocalStationID         = path.startLocalStationID!!,
                endID                       = path.endID,
                endStationProviderCode      = path.endStationProviderCode!!,
                endLocalStationID           = path.endLocalStationID!!,
                stationNames                = path.passStopList.stations.map { it.stationName }
            ).withCommonProperties(path)

            TrafficType.BUS.value -> BusSectionInfo(
                lane = path.lane!!.map {
                    BusLane(
                        busNo           = it.busNo!!,
                        type            = it.type!!,
                        busID           = it.busID!!,
                        busLocalBlID    = it.busLocalBlID!!,
                        busProviderCode = it.busProviderCode!!,
                    )
                },
                stationCount                = path.stationCount!!,
                startID                     = path.startID,
                startStationCityCode        = path.startStationCityCode!!,
                startStationProviderCode    = path.startStationProviderCode!!,
                startLocalStationID         = path.startLocalStationID!!,
                endID                       = path.endID,
                endStationCityCode          = path.endStationCityCode!!,
                endStationProviderCode      = path.endStationProviderCode!!,
                endLocalStationID           = path.endLocalStationID!!,
                stationNames                = path.passStopList.stations.map { it.stationName }
            ).withCommonProperties(path)

            TrafficType.WALK.value -> PedestrianSectionInfo(
                contents = mutableListOf()
            ).withCommonProperties(path)

            else -> {
                PedestrianSectionInfo(contents = mutableListOf()).withCommonProperties(path)
            }
        }
    }

    private fun castSectionRoute(trafficType: Int, response: RouteGraphicResponse): SectionRoute {
        return when (trafficType) {
            TrafficType.SUBWAY.value, TrafficType.BUS.value ->
                SectionRoute(routeList = response
                    .result!!.lane?.get(0)!!.section?.get(0)?.graphPos!!
                    .map { value -> LatLng(value.x!!, value.y!!) }.toMutableList()
                )

            TrafficType.WALK.value ->
                SectionRoute(routeList = mutableListOf())

            else ->
                SectionRoute(routeList = mutableListOf())
        }
    }

    private fun SectionInfo.withCommonProperties(it: ODsaySubPath): SectionInfo {
        distance    = it.distance
        sectionTime = it.sectionTime
        startName   = it.startName
        endName     = it.endName
        startX      = it.startX
        startY      = it.startY
        endX        = it.endX
        endY        = it.endY
        return this
    }

    // 선택한 TransPortRoute에 보행자 데이터 추가
    fun mappingPedestrianRouteToTransportRoute(
        transportRoute: TransportRoute, pedestrianResponseList: List<PedestrianResponse>
    ): TransportRoute {
        var index = 0
        transportRoute.subPath.forEach {
            when (it.trafficType) {
                TrafficType.WALK.value -> {
                    if (it.sectionInfo is PedestrianSectionInfo) {
                        it.sectionInfo.contents =
                            pedestrianResponseList[0].features
                                ?.map { feat -> feat.properties.description }
                                ?.toMutableList()!!
                    }

                    pedestrianResponseList[0].features?.forEach { feat ->
                        feat.geometry.coordinates.forEach { coordinate ->
                            it.sectionRoute.routeList.add(
                                LatLng(coordinate[0], coordinate[1])
                            )
                        }
                    }
                    index++
                }

                else -> {}
            }
        }

        return transportRoute
    }
}