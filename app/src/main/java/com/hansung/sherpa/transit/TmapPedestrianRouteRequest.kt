package com.hansung.sherpa.transit

import com.google.gson.annotations.SerializedName

data class TmapPedestrianRouteRequest(
    @SerializedName("startX") val startX: Float,
    @SerializedName("startY") val startY: Float,
    @SerializedName("angle") val angle: Int=20,
    @SerializedName("speed") val speed: Int=30,
    @SerializedName("endPoiId") val endPoiId: String="10001",
    @SerializedName("endX") val endX: Float,
    @SerializedName("endY") val endY: Float,
    @SerializedName("passList") val passList: String="126.92774822,37.55395475_126.92577620,37.55337145",
    @SerializedName("reqCoordType") val reqCoordType: String = "WGS84GEO",
    @SerializedName("startName") val startName: String = "%EC%B6%9C%EB%B0%9C",
    @SerializedName("endName") val endName: String = "%EB%8F%84%EC%B0%A9",
    @SerializedName("searchOption") val searchOption: String = "0",
    @SerializedName("resCoordType") val resCoordType: String = "WGS84GEO",
    @SerializedName("sort") val sort: String = "index"
)