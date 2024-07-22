package com.hansung.sherpa.arrivalinfomation

import com.google.gson.annotations.SerializedName

/**
 * '정류소별특정노선버스 도착예정정보 목록조회'를 받기 위한 요청 파라미터 클래스
 *  클래스 명은 공공 데이터 포털의 내용 그래도 가져온 것. 내가 쓴 거 아님
 *  https://www.data.go.kr/iim/api/selectAPIAcountView.do
 *
 *  @param _type 응답 데이터 타입(xml, json) - Default: xml
 *  @param cityCode 도시코드
 *  @param nodeId 정류소 ID - [국토교통부(TAGO)_버스정류소정보]에서 조회가능
 *  @param routeId 노선 ID - 버스 ID
 */
data class SttnAcctoSpcifyRouteBusArvlPrearngeInfoListRequest(
    @SerializedName("_type") val _type:String = "xml",
    @SerializedName("cityCode") val cityCode:Int,
    @SerializedName("nodeId") val nodeId:String,
    @SerializedName("routeId") val routeId:String
)