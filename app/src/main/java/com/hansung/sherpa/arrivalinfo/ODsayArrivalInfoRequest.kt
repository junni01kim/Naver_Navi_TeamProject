package com.hansung.sherpa.arrivalinfo

import com.google.gson.annotations.SerializedName
import com.hansung.sherpa.BuildConfig

class ODsayArrivalInfoRequest (
    @SerializedName("apiKey") val apiKey : String = BuildConfig.ODSAY_APP_KEY,
    @SerializedName("stationID") val stationID:Int,
    @SerializedName("routeIDs") val routeIDs:Int,
    @SerializedName("stationBase") val stationBase:Int = 1,
    @SerializedName("lowBus") val lowBus:Int = 0
) {
    fun getMap():Map<String,String> = mapOf("apiKey" to apiKey,"stationID" to stationID.toString(),"routeIDs" to routeIDs.toString(),"stationBase" to stationBase.toString(),"lowBus" to lowBus.toString())
}