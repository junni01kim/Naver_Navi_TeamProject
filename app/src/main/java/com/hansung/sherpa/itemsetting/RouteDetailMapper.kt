package com.hansung.sherpa.itemsetting

import com.hansung.sherpa.routegraphic.GraphPos
import com.hansung.sherpa.transit.ODsayInfo
import com.hansung.sherpa.transit.ODsayLane
import com.hansung.sherpa.transit.ODsayPath
import com.hansung.sherpa.transit.ODsayStations
import com.hansung.sherpa.transit.ODsaySubPath
import com.naver.maps.geometry.LatLng
import org.mapstruct.IterableMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named
import org.mapstruct.NullValueMappingStrategy
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ObjectFactory
import org.mapstruct.factory.Mappers

/**
 * TODO MAPSTRUCT MAPPER 사용 금지 (미완성)
 *
 * 현재 매핑은 RouteFilterMapper 사용 !!!
 *
 */


@Mapper(componentModel="spring",
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
interface RouteDetailMapper {
    /**
     * 보행자 데이터 매핑 시키는 함수
     * @sample samplePedestrian
     *
     * TODO 1) target-source 맞추기
     * TODO 2) parameter-return 맞추기
     */
    // subPath
    @Mappings(
        Mapping(target = "info", qualifiedByName = ["ODsayInfoToInfo"]),
        Mapping(target = "subPath", qualifiedByName = ["ODsaySubPathToSubPath"])
    )
    fun convertToTransit(o: ODsayPath, r: List<GraphPos>): TransportRoute

    @Named("ODsayInfoToInfo")
    @Mappings(
        // info
        Mapping(target = ".", source = "totalDistance"),
        Mapping(target = ".", source = "totalTime"),
        Mapping(target = "transferCount", source = "busTransitCount"),
        Mapping(target = ".", source = "totalWalk"),
    )
    fun convertODSsayInfoToInfo(oDsayInfo: ODsayInfo): Info

    @Named("ODsaySubPathToSubPath")
    @Mappings(
        Mapping(target = ".", source = "trafficType"),
        Mapping(target = "sectionInfo", source = "osp", qualifiedByName = ["ODsaySubPathToBusSectionInfo"]),
    )
    @IterableMapping(qualifiedByName = ["ODsaySubPathToBusSectionInfo"])
    fun convertODsaySubPathToSubPath(osp: ODsaySubPath): SubPath

    @Named("ODsaySubPathToBusSectionInfo")
    @Mappings(
        Mapping(target = "distance", source = "distance"),
        Mapping(target = "sectionTime", source = "sectionTime"),
        Mapping(target = "startName", source = "startName", defaultValue = ""),
        Mapping(target = "endName", source = "endName", defaultValue = ""),
        Mapping(target = "startX", source = "startX"),
        Mapping(target = "startY", source = "startY"),
        Mapping(target = "endX", source = "endX"),
        Mapping(target = "endY", source = "endY"),
        Mapping(target = "lane", source = "lane", qualifiedByName = ["ODsayLaneToBusLane"]),
        Mapping(target = "stationCount", source = "stationCount"),
        Mapping(target = "startID", source = "startID"),
        Mapping(target = "startStationCityCode", source = "startStationCityCode"),
        Mapping(target = "startStationProviderCode", source = "startStationProviderCode"),
        Mapping(target = "startLocalStationID", source = "startLocalStationID", defaultValue = ""),
        Mapping(target = "endID", source = "endID", defaultValue = "0"),
        Mapping(target = "endStationCityCode", source = "endStationCityCode", defaultValue = "0"),
        Mapping(target = "endStationProviderCode", source = "endStationProviderCode", defaultValue = "0"),
        Mapping(target = "endLocalStationID", source = "endLocalStationID", defaultValue = ""),
        Mapping(target = "stationNames", source = "oDsaySubPath.passStopList.stations", qualifiedByName = ["ODsayStationsToString"]),
    )
    fun convertODsaySubPathToBusSectionInfo(oDsaySubPath: ODsaySubPath): BusSectionInfo

    @Named("ODsayLaneToBusLane")
    @Mappings(
        Mapping(target = "name", ignore = true),
        Mapping(target = ".", source = "busNo", defaultValue = ""),
        Mapping(target = ".", source = "type", defaultValue = "0"),
        Mapping(target = ".", source = "busID", defaultValue = "0"),
        Mapping(target = ".", source = "busLocalBlID", defaultValue = ""),
        Mapping(target = ".", source = "busProviderCode", defaultValue = "0"),
    )
    fun convertODsayLaneToBusLane(oDsayLane: ODsayLane): BusLane

    @Named("ODsayStationsToString")
    @Mappings(
        Mapping(target = ".", source = "stationName"),
    )
    fun convertODsayStationsToString(stations: ODsayStations): String

    @Named("GraphPosToLatLng")
    @Mappings(
        Mapping(target = "latitude", source = "x"),
        Mapping(target = "longitude", source = "y")
    )
    fun convertGraphPosToLatLng(graphPos: GraphPos): com.hansung.sherpa.itemsetting.LatLng





    // 객체 팩토리
    @ObjectFactory
    fun createLatLng(graphPos: GraphPos): com.hansung.sherpa.itemsetting.LatLng {
        return com.hansung.sherpa.itemsetting.LatLng(graphPos.x!!, graphPos.y!!)
    }

    companion object {
        val INSTANCE: RouteDetailMapper = Mappers.getMapper(RouteDetailMapper::class.java)
    }

    /**
     * convertToPedestrian 실제 함수 사용 방식
     * @param transitRouteResponse API response data를 받아오면 넣어주기
     */
    /* fun samplePedestrian(transitRouteResponse: TmapTransitRouteResponse) {
        val routeDetailItem = RouteDetailMapper.INSTANCE.convertToPedestrian(
            transitRouteResponse.metaData?.plan?.itineraries?.get(0)?.legs?.get(0)!!
        )
        Log.i("item", routeDetailItem.toString())
    } */
}

annotation class Default

@Default
data class LatLng(var latitude: Double, var longitude: Double) {
    fun getLatLng(): LatLng {
        return LatLng(latitude, longitude)
    }
}

