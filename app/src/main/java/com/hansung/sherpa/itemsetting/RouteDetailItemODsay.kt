package com.hansung.sherpa.itemsetting

import com.naver.maps.geometry.LatLng

/**
 * 출발지에서 목적지까지 가능데 이용되는 전체 경로 집합 클래스
 * @param info 전체 경로에 대한 통합 정보
 * @param subPath 이동수단 단위의 경로 집합
 */
data class TransportRoute( // OdsayPath에 해당
    val info: Info,
    val subPath:List<SubPath>
)

/**
 * 전체 경로에 대한 정보 클래스 ex) 도착하기 위한 버스, 지하철, 도보를 모두 통합한 정보
 * # 김명준: RouteList에서 리스트를 정렬하기 위한 수단으로 사용한다.
 * @param totalDistance 총 이동거리
 * @param totalTime 총 소요 시간
 * @param transferCount 총 환승 수
 * @param totalWalk 총 도보 수
 */
data class Info( // OdsayInfo에 해당
    val totalDistance:Double,
    val totalTime:Int,
    val transferCount:Int,
    val totalWalk:Int
)

/**
 * 이동수단 단위의 경로 집합 클래스
 * @param trafficType 대중교통 종류(#김재호 기준: 다운 캐스팅을 위함)
 * @param sectionInfo 한 이동수단의 정보 ex) 버스, 지하철, 도보 단위의 정보
 * @param sectionRoute 한 이동수단의 경로 ex) 기존 좌표값 리스트랑 비슷
 */
data class SubPath( // OdsaySubPath에 해당
    val trafficType:Int,
    val sectionInfo:SectionInfo,
    val sectionRoute:SectionRoute
)

/**
 * 한 이동수단의 정보 #김재호: RouteDetailItem 클래스와 유사
 * @param distance 이동 거리 #김명준: 차트 비율 만들기 위함
 * @param sectionTime 이동 소요 시간 #김명준: 요약 정보에 기술하기 위함 #김재호: RouteDetailItem.time
 * @param startName 승차 정류장/역명 <도보 출발지> #김재호: RouteDetailItem.fromName
 * @param endName 하차 정류장/역명 <도보 목적지> #김재호: RouteDetailItem.toName
 * @param startX
 * @param startY
 * @param endX
 * @param endY
 */
interface SectionInfo { // OdsaySubPath에 해당
    var distance: Double?
    var sectionTime: Int?
    var startName: String?
    var endName: String?
    var startX:Double?
    var startY:Double?
    var endX:Double?
    var endY:Double?
}


/**
 * 한 이동수단의 정보 #김재호: RouteDetailItem 클래스와 유사
 * @param distance 이동 거리 #김명준: 차트 비율 만들기 위함
 * @param sectionTime 이동 소요 시간 #김명준: 요약 정보에 기술하기 위함 #김재호: RouteDetailItem.time
 * @param startName 승차 정류장/역명 <도보 출발지> #김재호: RouteDetailItem.fromName,
 * @param endName 하차 정류장/역명 <도보 목적지> #김재호: RouteDetailItem.toName
 * @param startX 보행자 경로 시작 X좌표
 * @param startY 보행자 경로 시작 Y좌표
 * @param endX 보행자 경로 도착 X좌표
 * @param endY 보행자 경로 도착 Y좌표
 * @param contents 보행자 세부 경로 (100m 앞 우회전, 200m 좌회전, 파리바게트 우회전...) -> TMap API 이용
 */
data class PedestrianSectionInfo(
    override var distance: Double? = null,
    override var sectionTime: Int? = null,
    override var startName: String? = null, // PedestrianResponse.Feature[0].Properties.facilityName 김명준 추정
    override var endName: String? = null, // PedestrianResponse.Feature[PedestrianResponse.Feature.size-1].Properties.facilityName 김명준 추정
    override var startX: Double? = null,
    override var startY: Double? = null,
    override var endX: Double? = null,
    override var endY: Double? = null,
    var contents:MutableList<String>
):SectionInfo

/**
 * 한 버스의 정보 #김재호: BusRouteDetailItem 클래스와 유사
 * @param distance 이동 거리 #김명준: 차트 비율 만들기 위함
 * @param sectionTime 이동 소요 시간 #김명준: 요약 정보에 기술하기 위함 #김재호: RouteDetailItem.time
 * @param lane 교통 수단 정보 확장 노드 #김명준: 요약 정보(대체버스인듯)에 기술하기 위함 + Lane안의 type 속성으로 색상 구별 가능 (https://lab.odsay.com/guide/releaseReference#busLaneDetail - 버스노선 타입 참조)
 * @param startName 승차 정류장/역명 <도보 출발지> #김재호: RouteDetailItem.fromName
 * @param endName 하차 정류장/역명 <도보 목적지> #김재호: RouteDetailItem.toName
 * @param startX 버스 경로의 시작 정류장 X좌표
 * @param startY 버스 경로의 시작 정류장 Y좌표
 * @param endX 버스 경로의 도착 정류장 X좌표
 * @param endY 버스 경로의 도착 정류장 Y좌표
 * @param stationCount 이동하여 정차하는 정거장 수(지하철, 버스 경우만 필수) #김재호: RouteDetailItem.summary
 * @param startID 출발 정류장/역 코드 #김명준: 요약 정보에 기술하기 위함
 * @param startStationCityCode 출발 정류장 도시코드 (버스인 경우에만 필수) #김명준: 버스 대기 시간 요청 파라미터
 * @param startStationProviderCode 출발 정류장 BIS 코드 (BIS 제공지역인 경우에만 필수)
 * @param startLocalStationID 각 지역 출발 정류장 ID (BIS 제공지역인 경우에만 필수) #김명준: 버스 대기 시간 요청 파라미터
 * @param endID 도착 정류장/역 코드
 * @param endStationCityCode 도착 정류장 도시코드 (버스인 경우에만 필수) #김명준: 버스 대기 시간 요청 파라미터
 * @param endStationProviderCode 도착 정류장 BIS 코드 (BIS 제공지역인 경우에만 필수)
 * @param endLocalStationID 각 지역 도착 정류장 ID (BIS 제공지역인 경우에만 필수)
 * @param stationNames Bus 정류장 이름들
 */
data class BusSectionInfo(
    override var distance: Double? = null,
    override var sectionTime: Int? = null,
    override var startName: String? = null,
    override var endName: String? = null,
    override var startX: Double? = null,
    override var startY: Double? = null,
    override var endX: Double? = null,
    override var endY: Double? = null,
    var lane: List<Lane>,
    val stationCount: Int,
    val startID: Int,
    val startStationCityCode: Int,
    val startStationProviderCode: Int,
    val startLocalStationID: String,
    val endID: Int,
    val endStationCityCode: Int,
    val endStationProviderCode: Int,
    val endLocalStationID: String,
    var stationNames:List<String>
):SectionInfo

/**
 * 한 지하철의 정보 #김재호: SubwayRouteDetailItem 클래스와 유사
 * @param distance 이동 거리 #김명준: 차트 비율 만들기 위함
 * @param sectionTime 이동 소요 시간 #김명준: 요약 정보에 기술하기 위함 #김재호: RouteDetailItem.time
 * @param lane 교통 수단 정보 확장 노드 #김명준: 요약 정보(대체버스인듯)에 기술하기 위함
 * @param startName 승차 정류장/역명 <도보 출발지> #김재호: RouteDetailItem.fromName
 * @param endName 하차 정류장/역명 <도보 목적지> #김재호: RouteDetailItem.toName
 * @param startX 지하철 경로의 시작 정류장 X좌표
 * @param startY 지하철 경로의 시작 정류장 Y좌표
 * @param endX 지하철 경로의 도착 정류장 X좌표
 * @param endY 지하철 경로의 도착 정류장 Y좌표
 * @param stationCount 이동하여 정차하는 정거장 수(지하철, 버스 경우만 필수) #김재호: RouteDetailItem.summary
 * @param way 방면 정보 (지하철인 경우에만 필수)
 * @param wayCode 방면 정보 코드(지하철의 첫번째 경로에만 필수) 1 : 상행, 2: 하행
 * @param door 지하철 빠른 환승 위치 (지하철인 경우에만 필수) #김명준: 요약 정보에 기술하기 위함
 * @param startID 출발 정류장/역 코드 #김명준: 요약 정보에 기술하기 위함
 * @param startStationProviderCode 출발 정류장 BIS 코드 (BIS 제공지역인 경우에만 필수)
 * @param startLocalStationID 각 지역 출발 정류장 ID (BIS 제공지역인 경우에만 필수) #김명준: 버스 대기 시간 요청 파라미터
 * @param endID 도착 정류장/역 코드
 * @param endStationProviderCode 도착 정류장 BIS 코드 (BIS 제공지역인 경우에만 필수)
 * @param endLocalStationID 각 지역 도착 정류장 ID (BIS 제공지역인 경우에만 필수)
 * @param stationNames Subway 정류장들 이름
 */
data class SubwaySectionInfo(
    override var distance: Double? = null,
    override var sectionTime: Int? = null,
    override var startName: String? = null,
    override var endName: String? = null,
    override var startX: Double? = null,
    override var startY: Double? = null,
    override var endX: Double? = null,
    override var endY: Double? = null,
    var lane: List<Lane>,
    val way:String,
    val wayCode:Int,
    val door:String,
    val stationCount: Int,
    val startID: Int,
    val startStationProviderCode: Int,
    val startLocalStationID: String,
    val endID: Int,
    val endStationProviderCode: Int,
    val endLocalStationID: String,
    var stationNames:List<String>
):SectionInfo

/**
 * 교통 수단 정보 확장 노드
 * @param name 지하철 노선명
 * @param busNo 버스 번호
 */
interface Lane {
    val name: String?
    val busNo:String?
}
/**
* 교통 수단 정보 확장 노드
* @param name 지하철 노선명 Bus에서는 불필요
* @param busNo 버스 번호
* @param type // 해당값을 통해 색상 구별 가능(시외, 고속, 마을 등등)
* @param busID 버스 코드 (버스인 경우에만 필수) #김명준: 버스 대기 시간 요청 파라미터
* @param busLocalBlID 각 지역 버스노선 ID (BIS 제공지역인 경우에만 필수)
* @param busProviderCode BIS 코드 (BIS 제공지역인 경우에만 필수)
 */
data class BusLane(
    override val name: String? = null,
    override val busNo: String,
    val type: Int,
    val busID: Int,
    val busLocalBlID: String,
    val busProviderCode: Int
):Lane

/**
* @param name 호선명
* @param busNo 버스 번호 Subway에서는 불필요
* @param subwayCode 호선 번호
* @param subwayCityCode 지하철 도시코드
*/
data class SubwayLane(
    override val name: String,
    override val busNo: String? = null,
    val subwayCode:Int,
    val subwayCityCode: Int
):Lane

/**
 * 한 이동수단의 경로 클래스
 * @param routeList 경로 위치 리스트
 */
data class SectionRoute(
    var routeList:MutableList<LatLng> = mutableListOf()
)