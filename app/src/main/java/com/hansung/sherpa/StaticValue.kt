package com.hansung.sherpa

import com.hansung.sherpa.deviation.RouteControl
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.navigation.Navigation
import com.hansung.sherpa.user.login.LoginResponse
import com.naver.maps.map.NaverMap

class StaticValue {
    companion object{
        lateinit var navigation: Navigation
        lateinit var mainActivity: MainActivity
        lateinit var FcmToken: String
        lateinit var transportRoute: TransportRoute
        lateinit var userInfo: LoginResponse
    }
}