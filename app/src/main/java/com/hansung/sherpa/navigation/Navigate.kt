package com.hansung.sherpa.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.deviation.RouteControl
import com.hansung.sherpa.deviation.StrengthLocation
import com.hansung.sherpa.gps.GPSDatas
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.PathOverlay

class Navigate(val pathOverlayList:MutableList<PathOverlay>) {
    lateinit var routeControl:RouteControl

    @RequiresApi(Build.VERSION_CODES.R)
    val gpsDatas = GPSDatas(StaticValue.mainActivity)

    @RequiresApi(Build.VERSION_CODES.R)
    fun run(){
        redrawRoute()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun redrawRoute() {
        var pathOverlaypre: PathOverlay? = null
        var pathOverlaycurr: PathOverlay? = null

        StaticValue.naverMap.addOnLocationChangeListener { location->
            if(routeControl!=null){
                var section = routeControl.checkingSection(StrengthLocation(gpsDatas.getGpsSignalAccuracy().Strength,LatLng(location.latitude,location.longitude)))
                Log.d("현재구역","현재위치: " + location.longitude+", "+location.latitude)
                Log.d("현재구역","시작: "+section.Start.longitude +", "+ section.Start.latitude+" 끝: " + section.End.longitude + ", " + section.End.latitude)
                if(pathOverlaycurr==null){
                    pathOverlaycurr = routeControl.drawProgressLine(section)
                    pathOverlaycurr!!.map = StaticValue.naverMap
                }
                else{
                    pathOverlaypre = pathOverlaycurr
                    pathOverlaycurr = routeControl.drawProgressLine(section)
                    pathOverlaypre?.map = null
                    pathOverlaycurr!!.map = StaticValue.naverMap
                }
                var isOut = routeControl.detectOutRoute(section, LatLng(location.latitude,location.longitude))
                if(isOut){
                    Log.d("이탈","이탈됨")
                    RouteControl.AlterToast.createToast(StaticValue.mainActivity)?.show()
//                    naverMap.map = null
                    for(i in pathOverlayList){
                        i.map = null
                    }
//                    section.End = LatLng(routeRequest.endY.toDouble(), routeRequest.endX.toDouble())
//                    section.End = LatLng(37.612746,127.834092) // 원당 좌표
                    section.End = LatLng(37.583145,127.011046) // 원당 좌표
                    routeControl.redrawDeviationRoute(section)
                }

            }
        }
    }
}