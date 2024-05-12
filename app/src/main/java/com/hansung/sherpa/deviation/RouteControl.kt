package com.hansung.sherpa.deviation

import android.content.Context
import android.graphics.Color
import com.hansung.sherpa.MainActivity
import com.hansung.sherpa.SearchRoute
import com.hansung.sherpa.transit.TransitRouteRequest
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PathOverlay
import kotlin.collections.*

/**
 *  경로를 그리는 인접한 두 좌표와 현재 위치를 정의하는 클래스
 *
 *  @property Start 구간의 시작 좌표
 *  @property End 구간의 끝 좌표
 *  @property CurrLocation 현재 사용자의 좌표
 */
data class Section(
    val Start: LatLng,
    val End: LatLng,
    val CurrLocation: LatLng
)

class RouteControl constructor(route:MutableList<Pair<MutableList<LatLng>, String>>) {

//    경로 이탈 : 10m
//    경로 구간 확인 : 동적
//    GPS 업데이트 시간 : 1.3s

    /**
     *  전체 경로 중 벡터 좌표 사이 구간의 사용자 이동 경로를 설정하는 함수
     *
     *  @param section 시작, 끝 벡터 좌표, 현재 사용자 위치를 가져옴
     *  @return PathOverlay
     */
    fun drawProgressLine(section: Section): PathOverlay {

        return PathOverlay().also {
            it.coords = listOf(section.Start, section.CurrLocation)
            it.width = 10
            it.passedColor = Color.YELLOW
            it.progress = 1.0
        }
    }

    /**
     *  현재 경로에서 이탈시 현재 위치에서 목적지까지 경로를 그리는 함수
     *
     *  @param section 시작(안씀), 목적지 벡터 좌표, 현재 사용자 위치를 가져옴
     *  @return (NaverMap, Context, MainActivity) -> Unit
     */
    fun redrawDeviationRoute(section: Section): (NaverMap, Context, MainActivity) -> Unit {

        val routeRequest = TransitRouteRequest(
            startX = section.CurrLocation.longitude.toString(),
            startY = section.CurrLocation.latitude.toString(),
            endX = section.End.longitude.toString(),
            endY = section.End.latitude.toString(),
            lang = 0,
            format = "json",
            count = 1
        )
        return { naverMap, context, lifecycle ->
            SearchRoute(naverMap, context, lifecycle).searchRoute(routeRequest)
        }
    }


}
