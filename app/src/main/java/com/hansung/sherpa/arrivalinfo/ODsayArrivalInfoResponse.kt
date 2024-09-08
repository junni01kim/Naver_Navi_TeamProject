package com.hansung.sherpa.arrivalinfo

/**
 * ODsay 실시간 버스 도착정보 조회 API 반환 값
 *
 * @param result [확장 노드] 데이터를 포함하는 최상위노드
 */
class ODsayArrivalInfoResponse {
    val result: Result? = null
}

/**
 * 데이터를 포함하는 최상위노드
 *
 * @param base [확장 노드] 요청 파라미터 busBase=1인 경우만 존재
 * @param real [확장 노드] 실시간 버스 도착정보
 */
data class Result(
    val base: Base? = null,
    val real: List<Real>? = null
)

/**
 * 요청 파라미터 busBase=1인 경우만 존재
 *
 * @param arsID 정류장 고유번호
 * @param busOnlyCentralLane 버스전용 중앙차로 정류장 해당 여부 (0: 비해당, 1: 해당)
 * @param do 정류장주소 도
 * @param dong 정류장주소 동
 * @param gu 정류장주소 구
 * @param lane [확장 노드]정류장의 버스 노선 리스트
 * @param localStationID 각 지역 버스정류장 ID
 * @param nonstopStation 미정차정류장 (0:정차, 1:미정차)
 * @param stationCityCode 정류장 도시코드
 * @param stationID 정류장 ID
 * @param stationName 정류장 이름
 * @param stationNameKor 정류장 이름 국문 (다국어 서비스 시 표출)
 * @param x 정류장 x좌표(경위도)
 * @param y 정류장 y좌표(경위도)
 */
data class Base(
    val arsID: String? = null,
    val busOnlyCentralLane: Int? = null,
    val `do`: String? = null,
    val dong: String? = null,
    val gu: String? = null,
    val lane: List<Lane?>? = null,
    val localStationID: String? = null,
    val nonstopStation: Int? = null,
    val stationCityCode: String? = null,
    val stationID: Int? = null,
    val stationName: String? = null,
    val stationNameKor: String? = null,
    val x: Double? = null,
    val y: Double? = null
)

/**
 * 실시간 버스 도착정보
 *
 * @param arrival1 [확장 노드] 첫번째 버스 도착정보
 * @param arrival2 [확장 노드] 두번째 버스 도착정보
 * @param localRouteId 각 지역 버스노선 ID
 * @param routeId 버스노선 코드
 * @param routeNm 버스 번호
 * @param stationSeq 해당 노선의 정류장 순번(BIS 순번 기준)
 * @param updownFlag 상하행 구분 (1:상행, 2:하행)
 */
data class Real(
    val arrival1: Arrival? = null,
    val arrival2: Arrival? = null,
    val localRouteId: String? = null,
    val routeId: String? = null,
    val routeNm: String? = null,
    val stationSeq: String? = null,
    val updownFlag: String? = null
)

/**
 * 정류장의 버스 노선 리스트
 * 
 * @param busCityCode 운수회사 승인 도시코드
 * @param busCityName 운수회사 승인 도시이름
 * @param busCityNameKor 운수회사 승인 도시이름 국문 (다국어 서비스 시 표출)
 * @param busDirectionName 방향/방면 명
 * @param busDirectionNameKor 방향/방면 명 국문 (다국어 서비스 시 표출)
 * @param busDirectionStationID 방향/방면 정류장 ID (busDirectionType = 1 또는 2 인 경우 표출)
 * @param busDirectionType 종점/방향/방면 구분 (0: 종점, 1:방향, 2:방면)
 * @param busEndPoint 버스노선 종점
 * @param busEndPointKor 버스노선 종점 국문 (다국어 서비스 시 표출)
 * @param busFirstTime 첫차시간
 * @param busID 버스노선ID
 * @param busInterval 운행간격(분) or 운행횟수(#1)
 * @param busLastTime 막차시간
 * @param busLocalBlID 각 지역 버스노선 ID
 * @param busNo 버스노선 번호
 * @param busNoKor 버스노선 번호 국문 (다국어 서비스 시 표출)
 * @param busStartPoint 버스노선 기점
 * @param busStartPointKor 버스노선 기점 국문 (다국어 서비스 시 표출)
 * @param busStationIdx 정류장 순번
 * @param type 버스노선 종류(문서하단 버스노선타입 참조)
 *
 */
data class Lane(
    val busCityCode: Int? = null,
    val busCityName: String? = null,
    val busCityNameKor: String? = null,
    val busDirectionName: String? = null,
    val busDirectionNameKor: String? = null,
    val busDirectionStationID: Int? = null,
    val busDirectionType: Int? = null,
    val busEndPoint: String? = null,
    val busEndPointKor: String? = null,
    val busFirstTime: String? = null,
    val busID: Int? = null,
    val busInterval: String? = null,
    val busLastTime: String? = null,
    val busLocalBlID: String? = null,
    val busNo: String? = null,
    val busNoKor: String? = null,
    val busStartPoint: String? = null,
    val busStartPointKor: String? = null,
    val busStationIdx: Int? = null,
    val type: Int? = null
)

/**
 * 두번째 버스 도착정보
 *
 * @param arrivalSec 버스 도착예정시간(초)
 * @param busPlateNo 버스 번호판 정보
 * @param busStatus 버스 운행(대기) 상태 (1:운행중, 2:차고지대기, 3:회차지대기, 이 외:운행종료)
 * @param congestion 버스 혼잡도 (nmprType = 4 인 경우) (-1:데이터 없음, 1:여유, 2:보통, 3:혼잡)
 * @param endBusYn 막차버스 여부 (Y:막차버스, N:일반버스)
 * @param fulCarAt 만차 여부 (0:만차 아님, 1:만차)
 * @param leftStation 첫번째 버스 남은 정류장 수
 * @param lowBusYn 저상버스 여부 (Y:저상버스, N:일반버스)
 * @param nmprType 첫번째 버스 재차구분 (0: 데이터 없음, 2: 재차인원, 4: 버스 혼잡도)
 * @param waitStatus ???
 */
data class Arrival(
    val arrivalSec: Int? = null,
    val busPlateNo: String? = null,
    val busStatus: String? = null,
    val congestion: Int? = null,
    val endBusYn: String? = null,
    val fulCarAt: String? = null,
    val leftStation: Int? = null,
    val lowBusYn: String? = null,
    val nmprType: Int? = null,
    val waitStatus: String? = null
)