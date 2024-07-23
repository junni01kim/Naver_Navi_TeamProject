package com.hansung.sherpa.busarrivalinfo

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name="response")
data class BusArrivalInfoResponse(
    @Element val header:Header? = null,
    @Element val body: Body? = null
)

@Xml
data class Header(
    @PropertyElement(name="resultCode") val resultCode:Int? = null,
    @PropertyElement(name="resultMsg") val resultMsg:String? = null
)

@Xml
data class Body(
    @Element(name = "item") val items: List<Item>? = null,
    @PropertyElement(name="numOfRows") val numOfRows:Int? = null,
    @PropertyElement(name="pageNo") val pageNo:Int? = null,
    @PropertyElement(name="totalCount") val totalCount:Int? = null
)

@Xml
data class Item(
    @PropertyElement(name="nodeId") val nodeId:String? = null,
    @PropertyElement(name="nodenm") val nodenm:String? = null,
    @PropertyElement(name="routeid") val routeid:String? = null,
    @PropertyElement(name="routeno") val routeno:Int? = null,
    @PropertyElement(name="routetp") val routetp:String? = null,
    @PropertyElement(name="arrprestationcnt") val arrprevstationcnt:Int? = null,
    @PropertyElement(name="vehicletp") val vehicletp:String? = null,
    @PropertyElement(name="arrtime") val arrtime:Int? = null
)