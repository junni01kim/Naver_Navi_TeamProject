package com.hansung.sherpa.transit.odsay

import com.google.gson.annotations.SerializedName

/**
 * ODSay 대중교통 길찾기 API 요청 class
 *
 * @property apiKey
 * @property SX 출발지 x좌표(경도)
 * @property SY 출발지 y좌표(위도)
 * @property EX 도착지 x좌표(경도)
 * @property EY 도착지 y좌표(위도)
 * @property OPT 경로검색결과 정렬방식
 * - 0 :추천경로
 * - 1 : 타입별정렬
 * @property SearchType 도시간 이동/도시내 이동을 선택한다.
 * @property SearchPathType 도시 내 경로수단을 지정한다.
 * - 0 : 모두 default
 * - 1 : 지하철
 * - 2 : 버스
 */
data class ODsayTransitRouteRequest(
    @SerializedName("apiKey") val apiKey: String,
    @SerializedName("SX") val SX: String,
    @SerializedName("SY") val SY: String,
    @SerializedName("EX") val EX	: String,
    @SerializedName("EY") val EY	: String,
    @SerializedName("OPT") val OPT	: String = "0",
    @SerializedName("SearchType") val SearchType	: String = "0",
    @SerializedName("SearchPathType") val SearchPathType: String = "0",
)