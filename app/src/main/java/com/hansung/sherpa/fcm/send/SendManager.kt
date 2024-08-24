package com.hansung.sherpa.fcm.send

import com.hansung.sherpa.itemsetting.LatLng

class SendManager {
    var myPos = LatLng(0.0, 0.0)
    fun SendMyPos(_myPos:LatLng) {
        myPos = _myPos

    }

    fun nowSchedule(title: String, body: String) {

    }

    fun nowRoute(title: String, body: String) {

    }
}