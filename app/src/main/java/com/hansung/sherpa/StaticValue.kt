package com.hansung.sherpa

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.user.table.User1
import com.naver.maps.geometry.LatLng

class StaticValue {
    companion object{
        lateinit var FcmToken: String
        lateinit var transportRoute: TransportRoute
        var userInfo: User1 = User1(role1 = "ADMIN")
        var searchPermission = false
        var ref: DatabaseReference = Firebase.database.reference
        var myPos : LatLng? = null
    }
}