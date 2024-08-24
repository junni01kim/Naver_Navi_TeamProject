package com.hansung.sherpa.fcm.send

import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.itemsetting.LatLng
import java.net.InetAddress

fun SendMyPos(myPos:LatLng) {
    val serverAddr = InetAddress.getByName(BuildConfig.SHERPA_URL)

}

class SendManager {
    fun nowSchedule() {

    }

    fun nowRoute() {

    }
}