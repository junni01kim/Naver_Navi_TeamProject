package com.hansung.sherpa.routegraphic

/**
 * ODsay '노선 그래픽 데이터 검색' 최상단 반환 클래스
 *  https://lab.odsay.com/guide/releaseReference#loadLane
 *
 * @param result 결과 값 모든 정보가 들어있다.
 */
data class RouteGraphicResponse (
    val result: Result? = null
)

/**
 * 결과 값 모든 정보가 들어있는 클래스.
 *
 * @param boundary 좌표의 rectangle. Load된 노선들에 대한 경계값
 */
data class Result(
    val lane: List<Lane?>? = null,
    val boundary: Boundary? = null
)

/**
 * @param tpClass 1(버스노선), 2(지하철노선)
 * @param type 노선종류 * 문서하단 버스노선, 지하철노선 타입 참조
 * @param section section 리스트
 */
data class Lane(
    val tpClass: Int? = null,
    val type:Int? = null,
    val section:List<Section>? = null
)

/**
 * @param graphPos 좌표 리스트
 */
data class Section(
    val graphPos: List<GraphPos>?
)

/**
 * @param x x좌표(경위도)
 * @param y y좌표(경위도)
 */
data class GraphPos(
    val x:Double? = null,
    val y:Double? = null
)

/**
 * @param left Left 좌표
 * @param top Top 좌표
 * @param right Right 좌표
 * @param bottom Bottom 좌표
 */
data class Boundary(
    val left:Double,
    val top:Double,
    val right:Double,
    val bottom:Double,
)