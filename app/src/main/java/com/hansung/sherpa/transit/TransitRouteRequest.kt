package com.hansung.sherpa.transit

import com.google.gson.annotations.SerializedName

/**
 * 교통수단 API를 사용하기 위한 요청 param 클래스
 *
 * @param startX 출발지 좌표(경도) WGS84
 * @param startY 출발지 좌표(위도) WGS84
 * @param endX 도착지 좌표(경도) WGS84
 * @param endY 도착지 좌표(위도) WGS84
 * @param lang 응답 언어 선택 (국문 : 0, 영문 : 1)
 * @param format 출력포맷 (json, xml)
 * @param count 최대 응답 결과 개수 (1~10)
 */
data class TransitRouteRequest(
    @SerializedName("startX") val startX: String,
    @SerializedName("startY") val startY: String,
    @SerializedName("endX") val endX: String,
    @SerializedName("endY") val endY: String,
    @SerializedName("lang") val lang: Int = 0,
    @SerializedName("format") val format: String,
    @SerializedName("count") val count: Int = 10
)


