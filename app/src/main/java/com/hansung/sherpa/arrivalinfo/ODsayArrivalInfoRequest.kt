package com.hansung.sherpa.arrivalinfo

import com.google.gson.annotations.SerializedName
import com.hansung.sherpa.BuildConfig

/**
 * 정류소별특정노선버스 도착예정정보 목록조회 API를 사용해 경로 데이터를 가져와 역직렬화하는 클래스
 *
 * @property apiKey ODsay API에 사용하기 위한 인증 Key 값
 * @property stationID 버스정류장 코드
 * @property stationID 노선정보 필터링
 * @property stationBase 저상버스 필터링(0: 전체 버스, 1: 저상버스만)
 */
class ODsayArrivalInfoRequest (
    @SerializedName("apiKey") val apiKey : String = BuildConfig.ODSAY_APP_KEY,
    @SerializedName("stationID") val stationID:Int,
    @SerializedName("routeIDs") val routeIDs:Int,
    @SerializedName("stationBase") val stationBase:Int = 1,
    @SerializedName("lowBus") val lowBus:Int = 0
) {
    /**
     * ODsayArrivalInfoService에서 @QueryMap를 사용하기 위해 Map으로 변환
     * 
     * @return 모든 객체를 해싱한 String 값
     */
    fun getMap():Map<String,String> = mapOf(
        "apiKey" to apiKey,
        "stationID" to stationID.toString(),
        "routeIDs" to routeIDs.toString(),
        "stationBase" to stationBase.toString(),
        "lowBus" to lowBus.toString()
    )
}