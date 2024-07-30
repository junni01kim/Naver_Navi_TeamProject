//package com.hansung.sherpa.itemsetting
//
//import android.util.Log
//import com.hansung.sherpa.transit.Leg
//import com.hansung.sherpa.transit.OdsayPath
//import com.hansung.sherpa.transit.OdsayTransitRouteResponse
//import com.hansung.sherpa.transit.TmapTransitRouteResponse
//import org.mapstruct.Mapper
//import org.mapstruct.Mapping
//import org.mapstruct.Mappings
//import org.mapstruct.factory.Mappers
//
//@Mapper(componentModel="spring")
//interface RouteDetailMapper {
//    /**
//     * 보행자 데이터 매핑 시키는 함수
//     * @sample samplePedestrian
//     *
//     * TODO 1) target-source 맞추기
//     * TODO 2) parameter-return 맞추기
//     */
//    @Mappings(
//        Mapping(target = "fromName", source = "start"),
//        Mapping(target = "toName", source = "end"),
//        Mapping(target = "summary", source = "distance"),
//        Mapping(target = "time", source = "sectionTime"),
//        Mapping(target = "contents", source = "steps"),
//    )
//    fun convertToPedestrian(path: Leg): PedestrianRouteDetailItem
//
//    companion object {
//        val INSTANCE: RouteDetailMapper = Mappers.getMapper(RouteDetailMapper::class.java)
//    }
//
//    /**
//     * convertToPedestrian 실제 함수 사용 방식
//     * @param transitRouteResponse API response data를 받아오면 넣어주기
//     */
//    /* fun samplePedestrian(transitRouteResponse: TmapTransitRouteResponse) {
//        val routeDetailItem = RouteDetailMapper.INSTANCE.convertToPedestrian(
//            transitRouteResponse.metaData?.plan?.itineraries?.get(0)?.legs?.get(0)!!
//        )
//        Log.i("item", routeDetailItem.toString())
//    } */
//}
//
