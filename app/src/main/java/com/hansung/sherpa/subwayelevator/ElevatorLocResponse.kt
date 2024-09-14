package com.hansung.sherpa.subwayelevator

/**
 * nnc_backend에서 전달 받은 Elevator 타입
 *
 * @param code : Response 코드 (200 - 정상)
 * @param data : 엘리베이터 정보들
 * @param message : 오류 내용 또는 전달된 정보 내용을 가지고 있음
 *
 * */
data class ElevatorLocResponse(
    val code: Int,
    val `data`: List<Data>,
    val message: String
) {
    /**
     * @param elevatorId 엘리베이터 ID
     * @param location 엘리베이터의 좌표 ("Lat,Lon"형식으로 formatting 되어 있음)
     * @param stationName 지하철역 이름 ("한성대입구"형식 - 뒤에 "역"이 붙어 있지 않음)
     * */
    data class Data(
        val elevatorId: Int,
        val location: String,
        val stationName: String
    )
}