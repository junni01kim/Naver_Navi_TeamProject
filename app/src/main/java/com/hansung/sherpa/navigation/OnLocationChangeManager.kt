package com.hansung.sherpa.navigation

import android.location.Location
import com.hansung.sherpa.StaticValue

class OnLocationChangeManager {
    companion object{
        private var lastLocation : Location? = null
        fun addMyOnLocationChangeListener(myOnLocationChangeListener: MyOnLocationChangeListener) {
            StaticValue.naverMap.addOnLocationChangeListener { location ->
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