package com.hansung.sherpa.navigation

import android.location.Location
import com.hansung.sherpa.StaticValue
import com.naver.maps.map.NaverMap

class OnLocationChangeManager {
    companion object{
        private var lastLocation : Location? = null
        lateinit var naverMap: NaverMap
        fun addMyOnLocationChangeListener(myOnLocationChangeListener: MyOnLocationChangeListener) {
            naverMap.addOnLocationChangeListener { location ->
                val shouldUpdate : Boolean = when(lastLocation){
                    null -> { true }
                    else -> {
                        lastLocation?.latitude != location.latitude || lastLocation?.longitude != location.longitude
                    }
                }

                if(shouldUpdate){
                    lastLocation = location
                    myOnLocationChangeListener.callback(location)
                }
            }
        }
    }
}