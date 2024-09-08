package com.hansung.sherpa.accidentpronearea

import com.google.gson.annotations.SerializedName
import com.naver.maps.geometry.LatLng


data class AccidentProneAreaResponse (
    @SerializedName("code"    ) var code    : Int,
    @SerializedName("message" ) var message : String,
    @SerializedName("data"    ) var data    : ArrayList<AccidentProneArea> = arrayListOf()
)

data class AccidentProneArea (
    @SerializedName("accidentProneAreaFid"  ) var accidentProneAreaFid  : Int?    = null,
    @SerializedName("accidentProneAreaId"   ) var accidentProneAreaId   : Int?    = null,
    @SerializedName("beopjeongdongCode"     ) var beopjeongdongCode     : String? = null,
    @SerializedName("jijeomCode"            ) var jijeomCode            : Int?    = null,
    @SerializedName("address"               ) var address               : String? = null,
    @SerializedName("jijeomName"            ) var jijeomName            : String? = null,
    @SerializedName("accidentCount"         ) var accidentCount         : Int?    = null,
    @SerializedName("casualtyCount"         ) var casualtyCount         : Int?    = null,
    @SerializedName("deathsCount"           ) var deathsCount           : Int?    = null,
    @SerializedName("seriouslyInjuredCount" ) var seriouslyInjuredCount : Int?    = null,
    @SerializedName("minorInjuredCount"     ) var minorInjuredCount     : Int?    = null,
    @SerializedName("reportedInjuredCount"  ) var reportedInjuredCount  : Int?    = null,
    @SerializedName("longitude"             ) var longitude             : Double? = null,
    @SerializedName("latitude"              ) var latitude              : Double? = null,
    @SerializedName("polygons"              ) var polygons              : Polygon? = null
)

data class Polygon(
    @SerializedName("type") val type: String,
    @SerializedName("coordinates") val coordinates: List<List<LatLng>>
)