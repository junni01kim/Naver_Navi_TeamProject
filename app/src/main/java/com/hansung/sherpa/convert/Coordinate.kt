package com.hansung.sherpa.convert

import com.naver.maps.geometry.LatLng


/**
 * 위도, 경도 타입 지정
 */
typealias coordinate = Double

/**
 * 좌표 값을 저장하는 클래스
 *
 * @since 2024-05-09
 * @author HS-JNYLee
 *
 * @param lat 위도
 * @param lon 경도
 * @sample com.hansung.sherpa.convert.coordinateSample
 */
class Coordinate(lat: coordinate = 0.0, lon: coordinate = 0.0) {
    val latitude: coordinate = lat
    val longitude: coordinate = lon
}

/**
 * SearchRoute.kt에 사용하는 네이버 데이터 클래스
 */
data class SearchRouteCoordinate(val latLng: LatLng, val type: PathType)

private fun coordinateSample() {
    val coordinate = Coordinate(37.607075, 127.00584)
}

