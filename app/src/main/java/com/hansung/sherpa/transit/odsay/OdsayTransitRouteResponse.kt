package com.hansung.sherpa.transit.odsay

/**
 * 도시 내 길찾기
 *
 * @param result 데이터를 포함하는 최상위 노드
 */
data class ODsayTransitRouteResponse (
    val result: ODsayResult? = null
)

/**
 * 데이터를 포함하는 최상위 노드
 *
 * @param searchType 결과 구분 (도시내 or 도시간 직통 or 도시간 환승) 0-도시내, 1-도시간 직통, 2-도시간 환승
 * @param outTrafficCheck 도시간 "직통" 탐색 결과 유무(환승 X) 0-False, 1-True
 * @param busCount 버스 결과 개수
 * @param subwayCount 지하철 결과 개수
 * @param subwayBusCount “버스+지하철” 결과 개수
 * @param pointDistance 출발지(SX, SY)와 도착지(EX, EY)의 직선 거리 (미터)
 * @param startRadius 출발지 반경
 * @param endRadius 도착지 반경
 * @param path 결과 리스트 확장 노드
 */
data class ODsayResult(
    val searchType: Int,
    val outTrafficCheck: Int,
    val busCount: Int,
    val subwayCount: Int,
    val subwayBusCount: Int,
    val pointDistance: Int,
    val startRadius: Int,
    val endRadius: Int,
    val path: List<ODsayPath>,
)

/**
 * 결과 리스트 확장 노드
 *
 * @param pathType 결과 종류 1-지하철, 2-버스, 3-버스+지하철
 * @param info 요약 정보 확장 노드
 * @param subPath 도보를 제외한 총 이동 거리
 */
data class ODsayPath(
    val pathType: Int,
    val info: ODsayInfo,
    val subPath: List<ODsaySubPath>,
)

/**
 * 요약 정보 확장 노드
 *
 * @param trafficDistance 도보를 제외한 총 이동 거리
 * @param totalWalk 총 도보 이동 거리
 * @param totalTime 총 소요시간
 * @param payment 총 요금
 * @param busTransitCount 버스 환승 카운트
 * @param subwayTransitCount 지하철 환승 카운트
 * @param mapObj 보간점 API를 호출하기 위한 파라미터 값
 * @param firstStartStation 최초 출발역/정류장
 * @param firstStartStationKor 최초 출발역/정류장 국문 (다국어 서비스 시 표출)
 * @param firstStartStationJpnKata 최초 출발역/정류장 일문(가타카나) (lang = 2 인 경우 표출)
 * @param lastEndStation 최종 도착역/정류장
 * @param lastEndStationKor 최종 도착역/정류장 국문 (다국어 서비스 시 표출)
 * @param lastEndStationJpnKata 최종 도착역/정류장 일문(가타카나) (lang = 2 인 경우 표출)
 * @param totalStationCount 총 정류장 합
 * @param busStationCount 버스 정류장 합
 * @param subwayStationCount 지하철 정류장 합
 * @param totalDistance 총 거리
 * @param checkIntervalTime 배차간격 체크 기준 시간(분)
 * @param checkIntervalTimeOverYn 배차간격 체크 기준시간을 초과하는 노선이 존재하는지 여부(Y/N)
 * @param totalIntervalTime 전체 배차간격 시간(분)
 */
data class ODsayInfo(
    val trafficDistance: Double,
    val totalWalk: Int,
    val totalTime: Int,
    val payment: Int,
    val busTransitCount: Int,
    val subwayTransitCount: Int,
    val mapObj: String,
    val firstStartStation: String,
    val firstStartStationKor: String,
    val firstStartStationJpnKata: String,
    val lastEndStation: String,
    val lastEndStationKor: String,
    val lastEndStationJpnKata: String,
    val totalStationCount: Int,
    val busStationCount: Int,
    val subwayStationCount: Int,
    val totalDistance: Double,
    val checkIntervalTime: Int,
    val checkIntervalTimeOverYn: String,
    val totalIntervalTime: Int,
)

/**
 * 이동 교통 수단 정보 확장 노드
 *
 * @param trafficType 이동 수단 종류 (도보, 버스, 지하철) 1-지하철, 2-버스, 3-도보
 * @param distance 이동 거리
 * @param sectionTime 이동 소요 시간
 * @param stationCount 이동하여 정차하는 정거장 수(지하철, 버스 경우만 필수)
 * @param lane 교통 수단 정보 확장 노드
 * @param intervalTime 평균 배차간격(분)
 * @param startName 승차 정류장/역명
 * @param startNameKor 승차 정류장/역명 국문 (다국어 서비스 시 표출)
 * @param startNameJpnKata 승차 정류장/역명 일문(가타카나) (lang = 2 인 경우 표출)
 * @param startX 승차 정류장/역 X 좌표
 * @param startY 승차 정류장/역 Y 좌표
 * @param endName 하차 정류장/역명
 * @param endNameKor 하차 정류장/역명 국문 (다국어 서비스 시 표출)
 * @param endNameJpnKata 하차 정류장/역명 일문(가타카나) (lang = 2 인 경우 표출)
 * @param endX 하차 정류장/역 X 좌표
 * @param endY 하차 정류장/역 Y 좌표
 * @param way 방면 정보 (지하철인 경우에만 필수)
 * @param wayCode 방면 정보 코드(지하철의 첫번째 경로에만 필수) 1 : 상행, 2: 하행
 * @param door 지하철 빠른 환승 위치 (지하철인 경우에만 필수)
 * @param startID 출발 정류장/역 코드
 * @param startStationCityCode 출발 정류장 도시코드 (버스인 경우에만 필수)
 * @param startStationProviderCode 출발 정류장 BIS 코드 (BIS 제공지역인 경우에만 필수)
 * @param startLocalStationID 각 지역 출발 정류장 ID (BIS 제공지역인 경우에만 필수)
 * @param startArsID 각 지역 출발 정류장 고유번호 (BIS 제공지역인 경우에만 필수)
 * @param endID 도착 정류장/역 코드
 * @param endStationCityCode 도착 정류장 도시코드 (버스인 경우에만 필수)
 * @param endStationProviderCode 도착 정류장 BIS 코드 (BIS 제공지역인 경우에만 필수)
 * @param endLocalStationID 각 지역 도착 정류장 ID (BIS 제공지역인 경우에만 필수)
 * @param endArsID 각 지역 도착 정류장 고유번호 (BIS 제공지역인 경우에만 필수)
 * @param startExitNo 지하철 들어가는 출구 번호 (지하철인 경우에만 사용되지만 해당 태그가 없을 수도 있다.)
 * @param startExitX 지하철 들어가는 출구 X좌표(지하철인 경우에 만 사용되지만 해당 태그가 없을 수도 있다.)
 * @param startExitY 지하철 들어가는 출구 Y좌표(지하철인 경우에 만 사용되지만 해당 태그가 없을 수도 있다.)
 * @param endExitNo 지하철 나가는 출구 번호(지하철인 경우에만 사용되지만 해당 태그가 없을 수도 있다.)
 * @param endExitX 지하철 나가는 출구 X좌표(지하철인 경우에만 사용되지만 해당 태그가 없을 수도 있다.)
 * @param endExitY 지하철 나가는 출구 Y좌표(지하철인 경우에만 사용되지만 해당 태그가 없을 수도 있다.)
 * @param passStopList 경로 상세구간 정보 확장 노드
 */
data class ODsaySubPath(
    val trafficType: Int,
    val distance: Double,
    val sectionTime: Int,
    val stationCount: Int? = null,
    val lane: List<ODsayLane>? = null,
    val intervalTime: Int,
    val startName: String,
    val startNameKor: String,
    val startNameJpnKata: String,
    val startX: Double,
    val startY: Double,
    val endName: String,
    val endNameKor: String,
    val endNameJpnKata: String,
    val endX: Double,
    val endY: Double,
    val way: String? = null,
    val wayCode: Int? = null,
    val door: String? = null,
    val startID: Int,
    val startStationCityCode: Int? = null,
    val startStationProviderCode: Int? = null,
    val startLocalStationID: String? = null,
    val startArsID: String? = null,
    val endID: Int,
    val endStationCityCode: Int? = null,
    val endStationProviderCode: Int? = null,
    val endLocalStationID: String? = null,
    val endArsID: String? = null,
    val startExitNo: String? = null,
    val startExitX: Double? = null,
    val startExitY: Double? = null,
    val endExitNo: String? = null,
    val endExitX: Double? = null,
    val endExitY: Double? = null,
    val passStopList: ODsayPassStopList,
)

/**
 * 교통 수단 정보 확장 노드
 *
 * @param name 지하철 노선명 (지하철인 경우에만 필수)
 * @param nameKor 지하철 노선명 국문 (다국어 서비스 시 지하철인 경우 표출)
 * @param nameJpnKata 지하철 노선명 일문(가타카나) (lang = 2 이면서 지하철인 경우 표출)
 * @param busNo 버스 번호 (버스인 경우에만 필수)
 * @param busNoKor 버스 번호 국문 (다국어 서비스 시 버스인 경우 표출)
 * @param busNoJpnKata 버스 번호 일문(가타카나) (lang = 2 이면서 버스인 경우 표출)
 * @param type 버스 타입 (버스인 경우에만 필수,최하단 참조)
 * @param busID 버스 코드 (버스인 경우에만 필수)
 * @param busLocalBlID 각 지역 버스노선 ID (BIS 제공지역인 경우에만 필수)
 * @param busCityCode 운수회사 승인 도시코드 (버스인 경우에만 필수)
 * @param busProviderCode BIS 코드 (BIS 제공지역인 경우에만 필수)
 * @param subwayCode 지하철 노선 번호 (지하철인 경우에만 필수)
 * @param subwayCityCode 지하철 도시코드 (지하철인 경우에만 필수)
 */
data class ODsayLane(
    val name: String? = null,
    val nameKor: String? = null,
    val nameJpnKata: String? = null,
    val busNo: String? = null,
    val busNoKor: String? = null,
    val busNoJpnKata: String? = null,
    val type: Int? = null,
    val busID: Int? = null,
    val busLocalBlID: String? = null,
    val busCityCode: Int? = null,
    val busProviderCode: Int? = null,
    val subwayCode: Int? = null,
    val subwayCityCode: Int? = null,
)

/**
 * 경로 상세구간 정보 확장 노드
 *
 * @param stations 정류장 정보 그룹노드
 */
data class ODsayPassStopList(
    val stations: List<ODsayStations>,
)

/**
 * 정류장 정보 그룹노드
 *
 * @param index 정류장 순번
 * @param stationID 정류장 ID
 * @param stationName 정류장 명칭
 * @param stationNameKor 정류장 명칭 국문 (다국어 서비스 시 표출)
 * @param stationNameJpnKata 정류장 명칭 일문(가타카나) (lang = 2 인 경우 표출)
 * @param stationCityCode 정류장 도시코드 (버스인 경우에만 필수)
 * @param stationProviderCode BIS 코드 (BIS 제공지역인 경우에만 필수)
 * @param localStationID 각 지역 정류장 ID (BIS 제공지역인 경우에만 필수)
 * @param arsID 각 지역 정류장 고유번호 (BIS 제공지역인 경우에만 필수)
 * @param x 정류장 X좌표
 * @param y 정류장 Y좌표
 * @param isNonStop 미정차 정류장 여부 Y/N(버스인 경우에만 필수)
 */
data class ODsayStations(
    val index: Int,
    val stationID: Int,
    val stationName: String,
    val stationNameKor: String,
    val stationNameJpnKata: String,
    val stationCityCode: Int? = null,
    val stationProviderCode: Int? = null,
    val localStationID: String? = null,
    val arsID: String? = null,
    val x: String,
    val y: String,
    val isNonStop: String? = null,
)

