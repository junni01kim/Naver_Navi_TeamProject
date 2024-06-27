package com.hansung.sherpa.gps

import android.location.GnssStatus
import android.os.Build
import androidx.annotation.RequiresApi


/**
 * 위성 개수가 변하면 CallBack 되는 GnssStatus 클래스
 * @author 6-keem
 *
 */
@RequiresApi(Build.VERSION_CODES.N)
class MyGnssStatus : GnssStatus.Callback() {
    var satelliteCount : Int = 0
    override fun onSatelliteStatusChanged(status: GnssStatus) {
        super.onSatelliteStatusChanged(status)
        satelliteCount = status.satelliteCount
    }
}