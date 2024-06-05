package com.hansung.sherpa

import com.hansung.sherpa.deviation.RouteControl
import com.naver.maps.map.NaverMap

class StaticValue {
    companion object{
        lateinit var naverMap:NaverMap
        lateinit var mainActivity: MainActivity
        lateinit var routeControl: RouteControl
    }
}