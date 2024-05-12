package com.hansung.sherpa.deviation

import android.graphics.Color
import android.location.Location
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.PathOverlay
import kotlin.collections.*

/**
 *  경로를 그리는 인접한 두 좌표와 현재 위치를 정의하는 클래스
 *
 *  @property Start 구간의 시작 좌표
 *  @property End 구간의 끝 좌표
 *  @property CurrLoaction 현재 사용자의 좌표
 */
data class Section (
    val Start: LatLng,
    val End: LatLng,
    val CurrLoaction: Location
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
        val locationPoint = LatLng(section.CurrLoaction)

        return PathOverlay().also {
            it.coords = listOf(section.Start, locationPoint)
            it.width = 10
            it.passedColor = Color.YELLOW
            it.progress = 1.0
        }
    }
}