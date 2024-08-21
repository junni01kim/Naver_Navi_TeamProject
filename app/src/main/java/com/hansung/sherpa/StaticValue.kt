package com.hansung.sherpa

import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.user.login.UserInfomation

class StaticValue {
    companion object{
        lateinit var FcmToken: String
        lateinit var transportRoute: TransportRoute
        var userInfo: UserInfomation = UserInfomation(role1 = "ADMIN")
        var searchPermission = false
    }
}