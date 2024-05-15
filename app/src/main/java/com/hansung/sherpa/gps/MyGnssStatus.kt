package com.hansung.sherpa.gps

import android.location.GnssStatus
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class MyGnssStatus : GnssStatus.Callback() {
    var satelliteCount : Int = 0
    override fun onSatelliteStatusChanged(status: GnssStatus) {
        super.onSatelliteStatusChanged(status)
        satelliteCount = status.satelliteCount
        Log.d("위성",""+satelliteCount)
    }
}