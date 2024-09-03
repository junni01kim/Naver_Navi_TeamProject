package com.hansung.sherpa.transit.routegraphic

import com.google.gson.annotations.SerializedName
import com.hansung.sherpa.BuildConfig

class ODsayGraphicRequest(
    @SerializedName("apiKey") val apiKey: String = BuildConfig.ODSAY_APP_KEY,
    @SerializedName("mapObject") val mapObject: ODsayMapObject
) {
    fun getQuery() = mapOf("apiKey" to apiKey, "mapObject" to mapObject.addBaseCoordinate())
}

/**
 * ODsay 노선 그래픽 API param
 * mapObject
 *
 * @param baseX 경도의 정수 부분만 입력
 *
 * @param baseY 위도의 정수 부분만 입력
 *
 * @param responseMapObject 전체 경로 중 구간에 대한 대중교통 mapObject 값
 * - ODsayTransitRouteResponse.result.path[0].info.mapObj
 */
class ODsayMapObject(
    private val baseX:Int = 0,
    private val baseY:Int = 0,
    private val responseMapObject: String
) {
    /**
     * ODsay 대중교통 길찾기 API에서 받은 mapObject에 base좌표를 추가해 리턴해준다.
     */
    fun addBaseCoordinate() = "${baseX}:${baseY}@${responseMapObject}"
}