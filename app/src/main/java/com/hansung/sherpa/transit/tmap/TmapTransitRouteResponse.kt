package com.hansung.sherpa.transit.tmap

/**
 * TMap 대중교통 API Response Data Class
 *
 * @property metaData 정상적인 데이터 검색 결과
 */
data class TmapTransitRouteResponse(
    val metaData: MetaData? = null
)

/**
 * 검색 결과
 *
 * @property requestParameters 요청한 데이터
 * @property plan 이동 방법
 */
data class MetaData(
    val requestParameters: RequestParameters? = null,
    val plan: Plan? = null
)

/**
 * 요청한 데이터
 *
 * @property busCount 버스 결과 개수
 * @property expressbusCount 고속/시외 버스 결과 개수
 * @property subwayCount 지하철 결과 개수
 * @property airplaneCount 항공 결과 개수
 * @property locale 언어코드(ko, en)
 * @property endY 도착지 좌표(위도) WGS84
 * @property endX 도착지 좌표(경도) WGS84
 * @property wideareaRouteCount 광역 노선 결과 개수
 * @property subwayBusCount 버스+지하철 결과 개수
 * @property startY 출발지 좌표(위도) WGS84
 * @property startX 출발지 좌표(경도) WGS84
 * @property ferryCount 해운 결과 개수
 * @property trainCount 기차 결과 개수
 * @property reqDttm 요청 시각  yyyymmddhhmiss
 */
data class RequestParameters(
    val busCount: Int? = null,
    val expressbusCount: Int? = null,
    val subwayCount: Int? = null,
    val airplaneCount: Int? = null,
    val locale: String? = null,
    val endY: String? = null,
    val endX: String? = null,
    val wideareaRouteCount: Int? = null,
    val subwayBusCount: Int? = null,
    val startY: String? = null,
    val startX: String? = null,
    val ferryCount: Int? = null,
    val trainCount: Int? = null,
    val reqDttm: String? = null
)

/**
 * @property itineraries : 데이터 상세정보 최상위 노드
 */
data class Plan(
    val itineraries: List<Itinerary>
)

/**
 * 데이터 상세정보 최상위 노드
 *
 * @property fare 금액 최상위 노드
 * @property totalTime 총 소요시간(sec)
 * @property legs 대중교통 정보 최상위 노드
 * @property totalWalkTime 	총 보행자 이동 거리(m)
 * @property transferCount 환승횟수
 * @property pathType 경로 탐색 결과 종류
 * - 1 : 지하철
 * - 2 : 버스
 * - 3 : 버스+지하철
 * - 4 : 고속/시외버스
 * - 5 : 기차
 * - 6 : 항공
 * - 7 : 해운
 */
data class Itinerary(
    val fare: Fare? = null,
    val totalTime: Int? = null,
    val legs: List<Leg>? = null,
    val totalWalkTime: Int? =null,
    val transferCount: Int? = null,
    val pathType: Int? = null // 교통수단 종류
)

/**
 * 금액 최상위 노드
 *
 * @property regular 금액 상위 노드
 */
data class Fare(
    val regular: Regular? = null
)

/**
 * 금액 상위 노드
 *
 * @property totalFare 대중교통요금
 * @property currency 금액 상위 노드
 */
data class Regular(
    val totalFare: Int? = null,
    val currency: Currency? = null
)

/**
 * 금액 상위 노드
 *
 * @property symbol 금액 상징(￦)
 * @property currency 금액 단위(원)
 * @property currencyCode 금액 단위 코드(KRW)
 */
data class Currency(
    val symbol: String? = null,
    val currency: String? = null,
    val currencyCode: String? = null
)

/**
 * 대중교통 정보 최상위 노드
 *
 * @property mode 이동 수단 종류
 * - 도보 : WALK
 * - 버스 : BUS
 * - 지하철 : SUBWAY
 * - 고속/시외버스 : EXPRESSBUS
 * - 기차 : TRAIN
 * - 항공 : AIRPLANE
 * - 해운 : FERRY
 * @property sectionTime 구간별 소요시간(sec)
 * @property distance 구간별 이동거리(m)
 * @property start 구간별 출발정보 최상위 노드
 * @property end 구간별 도착정보 최상위 노드
 * @property steps 구간별 도보상세정보 최상위 노드
 * @property routeColor 대중교통 노선 색상
 * @property route 대중교통 노선 명칭
 * @property routeId 대중교통 노선 ID
 * @property service 설명없음
 * @property passStopList 대중교통 구간 정류장 정보 최상위 노드
 * @property type [이동수단별 노선코드](https://skopenapi.readme.io/reference/%EB%8C%80%EC%A4%91%EA%B5%90%ED%86%B5-%EC%83%98%ED%94%8C-%EC%98%88%EC%A0%9C)
 * @property passShape 대중교통 구간 좌표 최상위 노드
 */
data class Leg(
    val mode: String? = null,
    val sectionTime: Int? = null,
    val distance: Int? = null,
    val start: Location,
    val end: Location,
    val steps: List<Step>? = null,
    val routeColor: String? = null,
    val route: String? = null,
    val routeId: String? = null,
    val service: Int? = null,
    val passStopList: PassStopList? = null,
    val type: Int? = null,
    val passShape: PassShape? = null
)

/**
 * 위치
 *
 * @property name 정류장 명칭
 * @property lon 좌표(경도) WGS84
 * @property lat 좌표(위도) WGS84
 */
data class Location(
    val name: String,
    val lon: Double,
    val lat: Double
)

/**
 * 구간별 도보상세정보 최상위 노드
 *
 * @property streetName 도로명
 * @property distance 도보 이동거리(m)
 * @property description 도보 구간 정보
 * @property linestring 도보 구간 좌표 WGS84
 */
data class Step(
    val streetName: String? = null,
    val distance: Int? = null,
    val description: String? = null,
    val linestring: String
)

/**
 * 대중교통 구간 정류장 정보 최상위 노드
 *
 * @property stationList
 */
data class PassStopList(
    val stationList: List<Station>? = null
)

/**
 * 정류장 상세정보
 *
 * @property index 순번
 * @property stationName 정류장 명칭
 * @property lon 정류장 좌표(경도) WGS84
 * @property lat 정류장 좌표(위도) WGS84
 * @property stationID 정류장 ID
 */
data class Station(
    val index: Int? = null,
    val stationName: String? = null,
    val lon: String,
    val lat: String,
    val stationID: String? = null
)

/**
 * 대중교통 구간 좌표 최상위 노드
 *
 * @property linestring 대중교통 구간 좌표 WGS84
 */
data class PassShape(
    val linestring: String? = null
)
