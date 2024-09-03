package com.hansung.sherpa.transit.tmappoi

import com.google.gson.annotations.SerializedName

data class TmapPoiRequest(
    val appkey:String,
    val searchKeyword:String,
    val centerLat:String,
    val centerLon:String,
    val version:String = "1"
)