package com.hansung.sherpa

import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.navigation.Navigation
import com.hansung.sherpa.user.login.UserInfomation

class StaticValue {
    companion object{
        lateinit var navigation: Navigation
        lateinit var mainActivity: MainActivity
        lateinit var FcmToken: String
        lateinit var transportRoute: TransportRoute
        var userInfo: UserInfomation = UserInfomation(role1 = "ADMIN")
        var searchPermission = false
    }
}