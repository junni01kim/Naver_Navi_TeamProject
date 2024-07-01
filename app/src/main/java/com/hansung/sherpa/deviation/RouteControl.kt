package com.hansung.sherpa.deviation

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.hansung.sherpa.navigation.Navigation
import com.hansung.sherpa.R
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.databinding.AlertBinding
import com.hansung.sherpa.transit.Station
import com.hansung.sherpa.transit.TransitRouteRequest
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Utmk
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.overlay.PolylineOverlay
import kotlin.collections.*
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 *  경로를 그리는 인접한 두 좌표와 현재 위치를 정의하는 클래스
 *
 *  @property Start 구간의 시작 좌표
 *  @property End 구간의 끝 좌표
 *  @property CurrLocation 현재 사용자의 좌표
 */
data class Section(
    var Start: LatLng,
    var End: LatLng,
    var CurrLocation: LatLng
)

/**
 *  GPS의 세기와 현재 사용자 위치를 정의하는 클래스
 *
 *  @property Strength GPS의 세기
 *  @property Loncation 사용자의 현재 위치
 */
data class StrengthLocation (
    val Strength: String,
    val Location: LatLng
)

/**
 * @property route 그려질 경로 좌표 리스트
 * @property navigation RouteControl을 생성한 Navigation 객체
 */
class RouteControl {

//    경로 이탈 : 10m
//    경로 구간 확인 : 동적
//    GPS 업데이트 시간 : 1.3s

    private var roundRadius = 1.0
    private val outDistance = 10.0
    var route : List<LatLng> = emptyList()

    /**
     * 사용자와 섹션 사이의 거리 값 m단위 반환
     *
     * @param section 섹션값
     * @param location 사용자 위치
    * */

    // Todo: 김명준이 작성
    var nowSection = 0
    lateinit var from: Utmk
    lateinit var to: Utmk
    lateinit var froms:Pair<Utmk, Utmk>
    lateinit var tos: Pair<Utmk, Utmk>

    // 섹션 통과 판단
    fun detectNextSection(location:LatLng):Boolean {
        var distance = 0.0

        val toLatLng = route[nowSection+1]

        // 목적지까지의 거리
        distance = location.distanceTo(toLatLng)

        if(distance <= 8) {
            Log.d("explain", "detctNextSection: 섹션 이동 ${nowSection}")
            nowSection++

            // 재설정
            from = Utmk.valueOf(route[nowSection])
            to = Utmk.valueOf(route[nowSection+1])

            Log.d("explain", "findIntersectionPoints: 함수 진입")
            froms = findIntersectionPoints(from)
            tos = findIntersectionPoints(to)
            Log.d("explain", "froms: ${froms}, tos: ${tos}")
            return true
        }
        return false
    }

    fun findIntersectionPoints(point:Utmk): Pair<Utmk, Utmk> {
        // 교점을 구하는 방정식 Wx^2 + Lx + M = 0
        // 계산 결과 W=m^2+1, L=--2*(a+m*b), M=a^2+b^2-r^2
        // m은 기울기(slope), (a, b)는 원의 중점과 직선이 지나는 한 점(point)

        val m = -1*(from.x - to.x)/(from.y - to.y)

        val a = point.x
        val b = point.y
        val r = 8.0

        //Log.d("explain", "이게 계속 음수가 나옴: ${interceptY.pow(2)-4*W*M}")

        // 결과적인 근의 공식
        val L = m.pow(2)+1
        val M = sqrt(4*L*r.pow(2))
        val bigPointX = (2*a*L+M)/(2*L)
        val smallPointX = (2*a*L-M)/(2*L)
        Log.d("explain", "bigPointX: ${bigPointX}")

        return Pair(Utmk(bigPointX, m*(bigPointX-a)+b), Utmk(smallPointX, m*(smallPointX-a)+b))
    }

    fun toScalar(point:Utmk) = sqrt(point.x.pow(2)+point.y.pow(2))
    fun getCosine(vector1:Utmk, vector2:Utmk) = (vector1.x*vector2.x+vector1.y+vector2.y)/(toScalar(vector1)*toScalar(vector2))

    // 두 벡터 사이의 각도 계산 함수
    fun angleBetweenVectors(vector1:Utmk, vector2:Utmk): Double {
        // 내적 계산
        val dotProduct = vector1.x * vector2.x + vector1.y * vector2.y

        // 벡터 크기 계산
        val magnitudeA = sqrt(vector1.x * vector1.y + vector1.y * vector1.y)
        val magnitudeB = sqrt(vector2.x * vector2.y + vector2.y * vector2.y)

        // 각도 계산 (라디안)
        val theta = acos(dotProduct / (magnitudeA * magnitudeB))

        // 외적 계산
        val crossProduct = vector1.x * vector2.y - vector1.y * vector2.x

        // 시계 방향 각도 조정
        return if (crossProduct > 0) {
            2 * Math.PI - theta
        } else {
            theta
        }
    }

    fun checkFlag(location: Utmk): Boolean {
        val coords = mutableListOf(
            route[nowSection],
            route[nowSection+1],
        )

        val polyline = PolylineOverlay()
        polyline.coords = coords
        polyline.width = 30
        polyline.color = Color.RED

        // 아직 반영되지 않음
        polyline.coords = coords
        polyline.map = StaticValue.naverMap
        // 반영됨

        val (bigFrom, smallFrom) = froms
        val (bigTo, smallTo) = tos
        Log.d("explain", "bigFrom: ${bigFrom} smallFrom: ${smallFrom} smallTo: ${smallTo}")

        val vector1 = Utmk(bigFrom.x - smallFrom.x, bigFrom.y - smallFrom.x)
        val vector2 = Utmk(smallTo.x - smallFrom.x, smallTo.y - smallFrom.x)
        val locationVector = Utmk(location.x - smallFrom.x, location.y - smallFrom.x)

        // 2. cosine의 주파수를 1/2로 줄인다.
        val cosine = getCosine(vector1, locationVector)

        val x = toScalar(locationVector) * cosine
        val y = toScalar(locationVector) * sqrt(1-cosine.pow(2))

        return x > toScalar(vector1) || y > toScalar(vector2) || angleBetweenVectors(vector1,vector2) < -1 || angleBetweenVectors(vector1,vector2) > 91
    }

    fun detectOutRoute2(location:LatLng):Boolean{
        var distance = 0.0

        while(detectNextSection(location)){ continue }

        val user = Utmk.valueOf(location)

        // 출발지 이탈 범위
        distance = location.distanceTo(route[nowSection])

        // Todo: 점과 직선 간의 거리 영역 제한
        val flag = checkFlag(user)
        if(distance >= 10 && flag) Log.d("explain", "---- 경로 재탐색 -----")

        return distance > 10 && flag
    }

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

    object AlterToast {
        fun createToast(context: Context): Toast? {
            val inflater = LayoutInflater.from(context)
            val binding: AlertBinding = DataBindingUtil.inflate(inflater, R.layout.alert, null, false)

            return Toast(context).apply {
                setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 16.toPx())
                duration = Toast.LENGTH_SHORT
                view = binding.root
            }
        }

        private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
    }



}
