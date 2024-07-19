package com.hansung.sherpa.routelist

import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.convert.PathType

data class ResponseRoutes(
    val totalPath: List<Route> // <- 리사이클러뷰에 전달 할 내용물
) {
    data class Route(
        val remainingtime: String, // 기본 리스트 하나
        val arrivalTime: String,
        val legRouteList: MutableList<LegRoute>, // 실제 경로 리스트
        val detailRoute: List<DetailRoute> // 나중에 편집 되어야 함
    ) {
        data class DetailRoute( // 확장될 영역의 한 줄
            val iconType: PathType,
            val transportNumber: String,
            val watingTime: String
        )
    }
}