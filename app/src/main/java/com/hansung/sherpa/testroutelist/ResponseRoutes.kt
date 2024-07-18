package com.hansung.sherpa.testroutelist

import com.hansung.sherpa.convert.LegRoute

data class ResponseRoutes(
    val totalPath: List<Route> // <- 리사이클러뷰에 전달 할 내용물
) {
    data class Route(
        val totalRemainingTime: String, // 기본 리스트 하나
        val totalDepartureTime: String,
        val legRouteList: MutableList<LegRoute>, // 실제 경로 리스트
        val detailRoute: List<DetailRoute> // 나중에 편집 되어야 함
    ) {
        data class DetailRoute( // 확장될 영역의 한 줄
            val busNumber: String,
            val watingTime: String
        )
    }
}