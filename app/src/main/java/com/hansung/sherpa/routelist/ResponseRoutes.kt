package com.hansung.sherpa.routelist

import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.convert.PathType

/**
 * RouteList RecyclerView에 포함되는 데이터 class
 *
 * @param remainingTime 도착하는데 걸리는 시간
 * @param arrivalTime 목적지에 도착하는 시간
 * @param legRouteList 이동 경로 좌표
 * @param detailRoute 아이템 확장 시 보여지는 데이터
 */
data class Route(
    val remainingTime: String, // 기본 리스트 하나
    val arrivalTime: String,
    val legRouteList: MutableList<LegRoute>, // 실제 경로 리스트
    val detailRoute: List<DetailRoute> // 나중에 편집 되어야 함
) {
    /**
     * 아이템 확장 시 보여지는 데이터 class
     *
     * @param iconType 대중교통 아이콘 타입
     * @param transportNumber 대중교통 번호
     * @param watingTime 대중교통 별 정류장 도착시간
     */
    data class DetailRoute( // 확장될 영역의 한 줄
        val iconType: PathType,
        val transportNumber: String,
        val watingTime: String
    )
}