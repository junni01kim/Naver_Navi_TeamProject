package com.hansung.sherpa.navigation

import android.location.Location

interface MyOnLocationChangeListener {
    fun callback(location : Location)
}