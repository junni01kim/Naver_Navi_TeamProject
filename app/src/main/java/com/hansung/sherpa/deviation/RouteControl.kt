package com.hansung.sherpa.deviation

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.hansung.sherpa.StaticValue
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Utmk
import kotlin.collections.*
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @property route 그려질 경로 좌표 리스트
 * @property navigation RouteControl을 생성한 Navigation 객체
 */
class RouteControl(val coordParts: SnapshotStateList<MutableList<LatLng>>) {

//    경로 이탈 : 8m
//    경로 구간 확인 : 동적

    /**
     * @param route 이동할 네비게이션 경로
     * @param nowSection route에서 지금 이동하고 있는 경로
     * @param outRouteDistance 이탈 되었다고 판단 할 거리
     */
    var nowSection = 0
    var nowSubpath = 0
    val outRouteDistance = 60.0

    /**
     * @param from 섹션의 시작점. 항상 route[nowSection]
     * @param to 섹션의 도착점. 항상 route[nowSection+1]
     */
    lateinit var from: Utmk
    lateinit var to: Utmk

    /**
     * 실질적인 이탈 영역의 범위
     *
     * @param froms 섹션의 시작점에서의 이탈 영역
     * @param tos 섹션의 시작점에서의 이탈 영역
     */
    lateinit var froms:Pair<Utmk, Utmk>
    lateinit var tos: Pair<Utmk, Utmk>

    /**
     * 새로운 경로가 발생할 때 기존 값을 초기화 하고 새로운 값들로 변경하는 함수
     * 함수가 호출되기 전 (새로운) route가 존재해야 한다.
     */
    fun initializeRoute() {
        from = Utmk.valueOf(coordParts[nowSubpath][nowSection])
        to = Utmk.valueOf(coordParts[nowSubpath][nowSection+1])

        froms = findIntersectionPoints(from)
        tos = findIntersectionPoints(to)
    }

    /**
     * 현재 섹션을 다음 섹션으로 이동할지 판단하는 함수
     *
     * @param location 현재 내 위치
     * @return to의 도착지 좌표 8m 이내에 진입할 시 true
     * -1. 최종 목적지 도착
     * 0. 해당 섹션 미통과
     * 1. 해당 섹션 통과
     */
    fun detectNextSection(location:LatLng):Int {
        // subPath의 마지막 구간인지 미리 탐색
        val lastSection = isNextSubpath()

        // 거리를 탐색할 섹션 목적지 좌표
        val destination =
            if(lastSection)
                coordParts[nowSubpath+1][0]
            else
                coordParts[nowSubpath][nowSection+1]

        // 내 위치에서 목적지까지의 거리
        val distance = location.distanceTo(destination)

        // 섹션 목적지 도달
        if(distance <= outRouteDistance) {
            // 목적지에 도착한 경우 lastSubPath, lastSection
            if(nowSubpath + 1 == coordParts.size && lastSection){
                // TODO: 아직 잘되는지는 모르겠음
                return -1
            }

            // 다음 섹션 이동
            if(lastSection){
                nowSection = 0
                nowSubpath++
            } else {
                nowSection++
            }

            val lastlastSection = isNextSubpath()

            // 섹션 값 재설정
            from = Utmk.valueOf(coordParts[nowSubpath][nowSection])
            to = Utmk.valueOf(
                if(lastlastSection) coordParts[nowSubpath+1][0]
                else coordParts[nowSubpath][nowSection+1]
            )

            // 섹션 영역 재설정
            froms = findIntersectionPoints(from)
            tos = findIntersectionPoints(to)
            return 1
        }
        return 0
    }

    fun isNextSubpath() = nowSection + 1 >= coordParts[nowSubpath].size

    /**
     * 원과 직선의 교점을 구하는 함수
     * to, from을 지나는 직선의 방정식에 수직인 기울기와 원의 중심 좌표를 갖는 직선과의 교점을 구한다.
     * Pair.first: 직사각형의 왼쪽 꼭짓점 Pair.second: 직사각형의 오른쪽 꼭짓점
     *
     * @param point 원의 중점
     * @return 교점1, 교점2 - Pair() 혹은 val (get1, get2)로 반환 받을 것
     */
    fun findIntersectionPoints(point:Utmk): Pair<Utmk, Utmk> {
        // 교점을 구하는 방정식 Wx^2 + Lx + M = 0
        // 계산 결과 W=m^2+1, L=--2*(a+m*b), M=a^2+b^2-r^2
        // m은 기울기(slope), (a, b)는 원의 중점과 직선이 지나는 한 점(point)

        val deltaY = to.y - from.y
        val deltaX = to.x - from.x
        // Utmk from과 to의 직선과 수직인 직선 기울기
        val m = -1*deltaX/deltaY

        // 직선 y = m(x-a)+b
        val a = point.x
        val b = point.y
        val r = outRouteDistance

        // 원의 방정식 0 = (x-a)^2 + (y-b)^2 - r^2
        // 원과 직선의 교점 방정식 2a(m^2)±sqrt(4(m^2+1)r^2)/2(m^2+1) -> 원본: 2a(m^2)±sqrt(4a^2(m^2+1)^2-4a^2(m^2+1)^2+4(m^2+1)r^2)/2(m^2+1)
        // 자주 사용하는 m^2+1는 L, ±할 sqrt(4(m^2+1)r^2)/2(m^2+1)는 M으로 지정
        // 계산식 = (2aL±M)/(2L)

        val L = m.pow(2)+1
        val M = sqrt(4*L*r.pow(2))
        val bigPointX = (2*a*L+M)/(2*L)
        val smallPointX = (2*a*L-M)/(2*L)

        val bigPoint = Utmk(bigPointX, m*(bigPointX-a)+b)
        val smallPoint = Utmk(smallPointX, m*(smallPointX-a)+b)

        // 방향성에 따라 직사각형의 위치 관계가 달라진다.
        if (deltaY>=0) return Pair(smallPoint,bigPoint)
        else return Pair(bigPoint,smallPoint)
    }

    /**
     * Utmk 좌표에서 벡터의 스칼라를 구하는 함수
     *
     * @param vector 스칼라 값을 구할 벡터
     * @return 스칼라 값
     */
    fun toScalar(vector:Utmk) = sqrt(vector.x.pow(2)+vector.y.pow(2))

    /**
     * Utmk 좌표에서 두 벡터의 코사인 값을 구하는 함수
     *
     * @param vector1 첫번째 벡터
     * @param vector2 두번째 벡터
     * @return 코사인 값
     */
    fun getCosine(vector1: Utmk, vector2: Utmk) = (vector1.x * vector2.x + vector1.y * vector2.y) / (toScalar(vector1) * toScalar(vector2))

    /**
     * 두 벡터 사이의 각도를 구하는 함수이다.
     * 시계 방향으로 각도를 구한다.
     *
     * @param vector1 방향을 정하는 기준 벡터
     * @param vector2 각도를 정하는 벡터
     * @return 두 벡터 사이의 각도
     */
    fun getTheta(vector1: Utmk, vector2: Utmk): Double {
        // 코사인 값 계산
        val cosine = getCosine(vector1, vector2)
        val radian = acos(cosine)

        val direction = vector1.x*vector2.y - vector1.y*vector2.x

        var theta = Math.toDegrees(radian)

        if(direction > 0) theta = 360-theta

        return theta
    }

    /**
     * 출발지와 도착지 간의 점과 직선 사이의 거리가 8m 이하인지 확인한다.
     *
     * @param location 내 위치
     * @return 섹션 출발지와 목적지로부터 수직으로 8m 안에 존재한다면 true
     */
    fun isInArea(location: Utmk): Boolean {

        val (leftFrom, rightFrom) = froms
        val (leftTo, rightTo) = tos

        val vector1 = Utmk(leftFrom.x - rightFrom.x, leftFrom.y - rightFrom.y)
        val vector2 = Utmk(rightTo.x - rightFrom.x, rightTo.y - rightFrom.y)
        val locationVector = Utmk(location.x - rightFrom.x, location.y - rightFrom.y)

        val cosine = getCosine(vector1, locationVector)

        val x = toScalar(locationVector) * cosine
        val y = toScalar(locationVector) * sqrt(1-cosine.pow(2)) // sqrt(1-cosine.pow(2)) = 사인값

        val angle = getTheta(vector1,locationVector)

        return x in 0.0..outRouteDistance*2 && y in 0.0..toScalar(vector2) // 직사각형 내부에 내 위치가 존재
                && angle in 0.0..90.0 // 내 위치의 각이 90보다 작아야 함
    }

    /**
     * 사용자가 주어진 경로에서 이탈되었는지 판단하는 함수
     *
     * @param location 내 위치 좌표
     * @return
     * -1. 경로 종료
     * 0. 출발지점에서 반경 n+2미터 안 or 직선 과의 거리 n미터에서 내부
     * 1. 출발지점에서 반경 n+2미터 밖 or 직선 과의 거리 n미터에서 외부
     */
    fun detectOutRoute(location:LatLng):Int{
        while(true){
            when(detectNextSection(location)){
                1 -> continue
                -1 -> return -1 // 도착한 경우
                0 -> break
            }
        }

        val transportRoute = StaticValue.transportRoute
        // 출발지와 내 위치의 거리를 판단한다.
        val distance = location.distanceTo(transportRoute.subPath[nowSubpath].sectionRoute.routeList[nowSection])

        // 출발지와 도착지 간의 점과 직선거리가 올바른지 판단한다.
        val user = Utmk.valueOf(location)
        val inArea = isInArea(user)

        return if(distance > outRouteDistance+2 && !inArea) 1 else 0
    }

    /**
     * 사용자와 가장 가까운 section의 index를 알려주는 함수
     *
     * @param location 현재 내 위치
     * @return 현재 SubPath의 section 중 location과 가장 가까운 section의 Index
     */
    fun findShortestIndex(location:LatLng):Int{
        var dist=1000000000.0

        var tmp:Double //확인된 거리
        var tmpIndex:Int=nowSection// 가장 짧은 거리에 있는 좌표의 값

        for(i in nowSection until StaticValue.transportRoute.subPath[nowSubpath].sectionRoute.routeList.size){
            tmp = location.distanceTo(StaticValue.transportRoute.subPath[nowSubpath].sectionRoute.routeList[i])
            if(tmp<dist){
                dist = tmp
                tmpIndex = i
            }
        }

        return tmpIndex
    }
}
