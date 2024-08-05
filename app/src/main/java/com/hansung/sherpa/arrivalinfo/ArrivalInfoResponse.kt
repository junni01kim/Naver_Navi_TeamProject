package com.hansung.sherpa.arrivalinfo

/**
 * '정류소별특정노선버스 도착예정정보 목록조회' 최상단 반환 클래스
 *  *  https://www.data.go.kr/iim/api/selectAPIAcountView.do
 *
 *  @param response 결과 값 모든 정보가 들어있다.
 */
data class ArrivalInfoResponse(
    val response: Response? = null
)

/**
 * 결과 값 모든 정보가 들어있는 클래스.
 *
 * @param header 결과 값 반환 상태 정보
 * @param body 실질적인 정보
 */
data class Response(
    val header:Header? = null,
    val body: Body? = null
)

/**
 * 결과 값 반환 상태 정보를 담는 클래스
 *
 * @param resultCode 결과코드 ex) 00
 * @param resultMsg 결과메시지 ex) NORMAL SERVICE
 */
data class Header(
    val resultCode:String? = null,
    val resultMsg:String? = null
)

/**
 * 실질적인 정보가 들어있는 클래스
 *
 * @param items 도착 예정 정보가 들어있다. #김명준: 항목구분은 1개여서 단일 값만 오는데, JSON값 기준으론 리스트로 처리함 좀 해괴함
 * @param numOfRows 한 페이지당 표출 데이터 수
 * @param pageNo 페이지 수
 * @param totalCount 데이터 총 개수
 */
data class Body(
    val items:Items? = null,
    val numOfRows:Int? = null,
    val pageNo:Int? = null,
    val totalCount:Int? = null
)

data class Items(
    val item : Item? = null
)

/**
 * 도착 예정 정보가 들어있다.
 *
 * @param nodeId 정류소ID
 * @param nodenm 정류소명
 * @param routeid 노선ID
 * @param routeno 노선번호
 * @param routetp 노선유형
 * @param arrprevstationcnt 도착예정버스 남은 정류장 수
 * @param vehicletp 도착예정버스 차량유형
 * @param arrtime 도착예정버스 도착예상시간 ex) 500; sec단위이다.
 */
data class Item(
    val nodeId:String? = null,
    val nodenm:String? = null,
    val routeid:String? = null,
    val routeno:Int? = null,
    val routetp:String? = null,
    val arrprevstationcnt:Int? = null,
    val vehicletp:String? = null,
    val arrtime:Int? = null
)