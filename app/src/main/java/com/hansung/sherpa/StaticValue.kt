package com.hansung.sherpa

import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.user.`class`.User1

class StaticValue {
    companion object{
        lateinit var FcmToken: String
        lateinit var transportRoute: TransportRoute
        var userInfo: User1 = User1(role1 = "ADMIN")
        var searchPermission = false
    }
}