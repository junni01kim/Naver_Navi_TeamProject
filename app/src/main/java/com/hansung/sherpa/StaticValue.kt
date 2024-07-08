package com.hansung.sherpa

import com.hansung.sherpa.deviation.RouteControl
import com.hansung.sherpa.navigation.Navigation
import com.naver.maps.map.NaverMap

class StaticValue {
    companion object{
        lateinit var navigation: Navigation
        lateinit var naverMap:NaverMap
        lateinit var mainActivity: MainActivity
        lateinit var routeControl: RouteControl
    }
}