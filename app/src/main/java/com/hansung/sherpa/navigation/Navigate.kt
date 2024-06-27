package com.hansung.sherpa.navigation

import android.location.Location
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Dispatcher
import java.lang.Thread.sleep

class Navigate(val pathOverlayList:MutableList<PathOverlay>, val routeControl: RouteControl) {

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

        OnLocationChangeManager.addMyOnLocationChangeListener(object:MyOnLocationChangeListener{
            override fun callback(location: Location) {
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
                    if(routeControl.detectOutRoute(section, LatLng(location.latitude,location.longitude))) {// 경로이탈 탐지
                        Log.d("이탈", "이탈됨")
                        RouteControl.AlterToast.createToast(StaticValue.mainActivity)?.show()
//                    naverMap.map = null
                        for (i in pathOverlayList) {
                            i.map = null
                        }
                        section.End = LatLng(37.583145, 127.011046) // 원당 좌표
                        routeControl.redrawDeviationRoute(section)
                    }
                }
            }

        })

    }
}