package com.hansung.sherpa.routegraphic

import android.icu.util.Output
import com.google.android.gms.common.api.internal.ApiKey
import com.google.gson.annotations.SerializedName
import com.hansung.sherpa.BuildConfig

/**
 *  ODsay '노선 그래픽 데이터 검색' 요청 파라미터 클래스
 *  https://lab.odsay.com/guide/releaseReference#loadLane
 *
 *  @param apiKey ODsay에서 발급된 키
 *  @param mapObject ODsay에 요청할 정보 객체
 */
class RouteGraphicRequest(
    @SerializedName("apiKey") val apiKey: String = BuildConfig.ODSAY_APP_KEY,
    @SerializedName("mapObject") val mapObject:MapObject
){
    /**
     * RouteGraphicService가 @QueryMap으로 전달됨
     */
    fun getMap() = mapOf("apiKey" to apiKey, "mapObject" to mapObject.toString())
}

/**
 * ODsay에 요청할 정보 객체
 *
 * @param baseX 데이터 기준점이 값이 빼진 값으로 좌표가 리턴됨
 * @param baseY 데이터 기준점이 값이 빼진 값으로 좌표가 리턴됨
 * @param typeId 버스노선ID 또는 지하철노선ID
 * @param typeClass 1(버스), 2(지하철)
 * @param startIdx 출발인덱스
 * @param endIdx 도착인덱스
 */
class MapObject(
    val baseX:Int,
    val baseY:Int,
    val typeId:Int,
    val typeClass:Int,
    val startIdx:Int,
    val endIdx:Int
) {
    /**
     * 객체 반환은 직렬화 해서 한다.
     */
    override fun toString() = "${baseX}:${baseY}@${typeId}:${typeClass}:${startIdx}:${endIdx}"
}