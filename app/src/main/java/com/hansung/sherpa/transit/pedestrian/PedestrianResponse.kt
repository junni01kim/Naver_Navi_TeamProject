package com.hansung.sherpa.transit.pedestrian

import com.google.android.gms.common.Feature

/**
 * TODO
 *
 * @property features 포인트 및 라인의 형상 정보입니다.
 * @property type 출발점, 안내점, 경유지, 도착점 정보입니다.
 */
data class PedestrianResponse(
    val features: List<Feature>?=null,
    val type: String? = null
) {
    /**
     * TODO
     *
     * @property geometry 형상 정보 입니다. (geojson 표준 규격)
     * @property properties 사용자 정의 프로퍼티 정보입니다. (geojson 표준 규격)
     * @property type 형상 정보 데이터의 종류입니다.(geojson 표준 규격)
     */
    data class Feature(
        val geometry: Geometry,
        val properties: Properties,
        val type: String
    ) {
        /**
         * TODO
         *
         * @property coordinates 좌표 정보입니다.
         * - 경도와 위도는 콤마(,)로 구분하여 '[',']'으로 묶어주고
         * 복수 좌표 역시 콤마로 구분하여 '[',']'으로 묶어줍니다.
         * -형식: [[x1좌표, y1좌표], [x2좌표, y2좌표]]
         * @property type 형상 정보 데이터의 종류입니다.
         */
        data class Geometry(
            val coordinates: List<List<Double>>,
            val type: String
        )

        /**
         * TODO
         *
         * @property categoryRoadType 특화거리 정보입니다.
         * @property description 길 안내 정보입니다.
         * @property direction 방면 명칭입니다.
         * @property distance 구간 거리(단위 : m)입니다.
         * @property facilityName 구간 시설물 타입의 명칭입니다.
         * @property facilityType 구간의 시설물 정보입니다.
         * @property index 경로 순번입니다.
         * @property intersectionName 교차로 명칭입니다.
         * @property lineIndex 구간의 순번입니다.
         * @property name 도로 명칭 입니다.
         * @property nearPoiName 안내지점 근방 poi 입니다.
         * @property nearPoiX 안내지점 근방 poi X좌표 입니다.
         * @property nearPoiY 안내지점 근방 poi Y좌표 입니다.
         * @property pointIndex 안내점 노드의 순번입니다.
         * @property pointType 안내지점의 구분입니다.
         * @property roadType 도로 타입 정보입니다.
         * @property time 구간의 소요 시간(단위 : 초)입니다.
         * @property totalDistance 경로 총 길이(단위:m)입니다.
         * @property totalTime 경로 총 소요시간(단위: 초)입니다.
         * @property turnType 회전 정보입니다.
         */
        data class Properties(
            val categoryRoadType: Int,
            val description: String,
            val direction: String,
            val distance: Int,
            val facilityName: String,
            val facilityType: String,
            val index: Int,
            val intersectionName: String,
            val lineIndex: Int,
            val name: String,
            val nearPoiName: String,
            val nearPoiX: String,
            val nearPoiY: String,
            val pointIndex: Int,
            val pointType: String,
            val roadType: Int,
            val time: Int,
            val totalDistance: Int,
            val totalTime: Int,
            val turnType: Int
        )
    }
}