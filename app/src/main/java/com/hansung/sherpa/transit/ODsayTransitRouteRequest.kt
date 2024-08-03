package com.hansung.sherpa.transit

import com.google.gson.annotations.SerializedName

/**
 * Odsay 대중교통 길찾기 v1.8
 * https://api.odsay.com/v1/api/searchPubTransPathT
 *
 * @param startX 출발지 좌표(경도) WGS84
 * @param startY 출발지 좌표(위도) WGS84
 * @param endX 도착지 좌표(경도) WGS84
 * @param endY 도착지 좌표(위도) WGS84
 * @param lang 응답 언어 선택 (국문 : 0, 영문 : 1)
 * @param format 출력포맷 (json, xml)
 * @param count 최대 응답 결과 개수 (1~10)
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