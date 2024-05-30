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
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PathOverlay
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

class RouteControl constructor(val naverMap:NaverMap, val route:MutableList<LegRoute>, val navigation: Navigation) {

//    경로 이탈 : 10m
//    경로 구간 확인 : 동적
//    GPS 업데이트 시간 : 1.3s

    private var roundRadius = 1.0
    private var routeEnum = ArrayList<LatLng>()
    private val outDistance = 10.0

    init {
        for(i in route){
            for(j in i.coordinates){
                routeEnum.add(LatLng(j.latitude,j.longitude))
            }
        }
    }

    fun upDateRouteEnum(route:MutableList<Pair<MutableList<LatLng>, String>>){
        this.routeEnum = ArrayList<LatLng>()

        for(i in route){
            for(j in i.first){
                routeEnum.add(j)
            }
        }
    }

    fun checkingSection(strloc:StrengthLocation):Section{/// ???

        when(strloc.Strength){
            "Strong"->{ roundRadius = 40.0 }
            "Weak"->{ roundRadius = 43.0 }
        }

        var returnIndex = 0
        var flag=0
        while (returnIndex<routeEnum.size-2){
            if(calcDistance(routeEnum[returnIndex], strloc.Location)<=roundRadius &&
                calcDistance(routeEnum[returnIndex+1], strloc.Location)>roundRadius){
                flag=1
                Log.d("거리", "현재 index : " + returnIndex)
                Log.d("거리","1. " + calcDistance(routeEnum[returnIndex], strloc.Location))
                Log.d("거리","2. " + calcDistance(routeEnum[returnIndex+1], strloc.Location))
                break
            }
            returnIndex+=1
        }

        var checkDist = Double.MAX_VALUE
        var distTmp:Double
        if(flag==0){
            for(i in 0..routeEnum.size-2){
                distTmp = calcDistance(routeEnum[i],strloc.Location)
                if(checkDist>distTmp){
                    returnIndex = i
                    checkDist = distTmp
                }
            }
        }

        Log.d("거리1", "현재 index : " + returnIndex)
        Log.d("거리1", "CheckDist : " + checkDist)

        return Section(routeEnum[returnIndex],routeEnum[returnIndex+1], strloc.Location)
    }

    /**
     *  두 좌표 사이의 거리 계산 m단위
     *
     *  @param latlng1 좌표1
     *  @param latlng2 좌표2
     *  @return Double
     */
    fun calcDistance(latlng1:LatLng, latlng2: LatLng): Double {
        val radLat1 = Math.toRadians(latlng1.latitude)
        val radLon1 = Math.toRadians(latlng1.longitude)
        val radLat2 = Math.toRadians(latlng2.latitude)
        val radLon2 = Math.toRadians(latlng2.longitude)

        // Haversine 공식을 사용하여 거리 계산
        val dLat = radLat2 - radLat1
        val dLon = radLon2 - radLon1
        val a = sin(dLat / 2) * sin(dLat / 2) + cos(radLat1) * cos(radLat2) * sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        var distance = 6371000 * c // 지구 반지름을 곱하여 거리를 미터 단위로 변환

        if (distance < 0) {
            distance *= -1
        }

        return distance
    }


    fun calcPointLineDist(A:LatLng, B:LatLng, user:LatLng):Double{
        val A_coeff = B.latitude - A.latitude
        val B_coeff = A.longitude - B.longitude
        val C_coeff = A.latitude * (B.longitude - A.longitude) - A.longitude * (B.latitude - A.latitude)

        // MY의 위치를 직선의 방정식에 대입하여 거리를 구합니다.
        // 거리 공식: distance = |Ax + By + C| / sqrt(A^2 + B^2)
        val distance = abs(A_coeff * user.longitude + B_coeff * user.latitude + C_coeff) / sqrt(A_coeff * A_coeff + B_coeff * B_coeff)

        return distance
    }

    fun detectOutRoute(section:Section, location:LatLng):Boolean{

        val A = section.Start
        val B = section.End
        val user = location

        val A_coeff = B.latitude - A.latitude
        val B_coeff = A.longitude - B.longitude
        val C_coeff = A.latitude * (B.longitude - A.longitude) - A.longitude * (B.latitude - A.latitude)

        // MY의 위치를 직선의 방정식에 대입하여 거리를 구합니다.
        // 거리 공식: distance = |Ax + By + C| / sqrt(A^2 + B^2)
        val distance = abs(A_coeff * user.longitude + B_coeff * user.latitude + C_coeff) / sqrt(A_coeff * A_coeff + B_coeff * B_coeff)*10000

//        Log.d("경로사이의거리","거리: "+distance+" 구역: "+location.latitude +", " +location.longitude)

        Log.d("이탈: ","거리: "+distance)
        if(distance>=5){
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

    /**
     *  현재 경로에서 이탈시 현재 위치에서 목적지까지 경로를 그리는 함수
     *
     *  @param section 시작(안씀), 목적지 벡터 좌표, 현재 사용자 위치를 가져옴
     *  @return (NaverMap, Context, MainActivity) -> Unit
     */

    fun redrawDeviationRoute(section: Section){
        val routeRequest = TransitRouteRequest(
            startX = section.CurrLocation.longitude.toString(),
            startY = section.CurrLocation.latitude.toString(),
            endX = section.End.longitude.toString(),
            endY = section.End.latitude.toString(),
            lang = 0,
            format = "json",
            count = 1
        )
        navigation.process(routeRequest)
//        return { naverMap, context, lifecycle ->
////            SearchRoute(naverMap, context, lifecycle).searchRoute(routeRequest)
//            searchRoute.searchRoute(routeRequest)
//        }
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
