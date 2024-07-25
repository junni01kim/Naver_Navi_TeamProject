package com.hansung.sherpa.routegraphic

import android.icu.util.Output
import com.google.android.gms.common.api.internal.ApiKey
import com.google.gson.annotations.SerializedName
import com.hansung.sherpa.BuildConfig

class RouteGraphicRequest(
    @SerializedName("apiKey") val apiKey: String = BuildConfig.ODSAY_APP_KEY,
    @SerializedName("mapObject") val mapObject:MapObject
){
    fun getMap() = mapOf("apiKey" to apiKey, "mapObject" to mapObject.toString())
}

class MapObject(
    val baseX:Int,
    val baseY:Int,
    val typeId:Int,
    val typeClass:Int,
    val startIdx:Int,
    val endIdx:Int
) {
    override fun toString() = "${baseX}:${baseY}@${typeId}:${typeClass}:${startIdx}:${endIdx}"
}