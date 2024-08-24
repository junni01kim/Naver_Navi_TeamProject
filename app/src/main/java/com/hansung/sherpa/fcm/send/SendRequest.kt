package com.hansung.sherpa.fcm.send

import com.naver.maps.geometry.LatLng

class SendRequest {
}

data class sendMyPosRequest(val userId: Int, val myPos: LatLng)