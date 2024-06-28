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
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.databinding.AlertBinding
import com.hansung.sherpa.transit.TransitRouteRequest
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Utmk
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.overlay.PolygonOverlay
import com.naver.maps.map.overlay.PolylineOverlay
import kotlin.collections.*
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
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

    fun checkingSection(strloc:StrengthLocation): Section? {/// ???
        if (route.isEmpty()) return null
        when(strloc.Strength){
            "Strong"->{ roundRadius = 40.0 }
            "Weak"->{ roundRadius = 43.0 }
        }

        var returnIndex = 0
        var flag=0
        while (returnIndex<route.size-2){
            if(route[returnIndex].distanceTo(strloc.Location)<=roundRadius &&
                route[returnIndex+1].distanceTo(strloc.Location)>roundRadius){
                flag=1
                Log.d("거리", "현재 index : " + returnIndex)
                Log.d("거리","1. " + route[returnIndex].distanceTo(strloc.Location))
                Log.d("거리","2. " + route[returnIndex+1].distanceTo(strloc.Location))
                break
            }
            returnIndex+=1
        }

        var checkDist = Double.MAX_VALUE
        var distTmp:Double
        if(flag==0){
            for(i in 0..route.size-2){
                distTmp = route[i].distanceTo(strloc.Location)
                if(checkDist>distTmp){
                    returnIndex = i
                    checkDist = distTmp
                }
            }
        }

        Log.d("거리1", "현재 index : " + returnIndex)
        Log.d("거리1", "CheckDist : " + checkDist)

        return Section(route[returnIndex],route[returnIndex+1], strloc.Location)
    }

    /**
     * 사용자와 섹션 사이의 거리 값 m단위 반환
     *
     * @param section 섹션값
     * @param location 사용자 위치
    * */
    fun detectOutRoute(section:Section, location:LatLng):Boolean{
        var res = 0.0
        if (section.Start.latitude == section.End.latitude && section.Start.longitude == section.End.longitude) return false
        //Utm-K로 좌표계 변환
        val from = Utmk.valueOf(section.Start)
        val to = Utmk.valueOf(section.End)
        val user = Utmk.valueOf(location)

        //기울기
        var slope = (from.y - to.y)/(from.x - to.x)

        // y절편
        var yCoeff = from.y - slope*from.x

        var a = -1*slope
        var b = 1
        var c = -1*yCoeff

        res = abs(a*(user.x) + b*(user.y) + c) / sqrt(a*a + b*b)

        Log.d("이탈: ","거리: "+res)
        if(res>=8){
            return true
        }
        else{
            return false
        }
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
