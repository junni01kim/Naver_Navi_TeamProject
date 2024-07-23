package com.hansung.sherpa.busarrivalinfo

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name="response")
data class BusArrivalInfoResponse(
    @Element val header:Header? = null,
    @Element val body: Body? = null
)

@Xml(name="header")
data class Header(
    @PropertyElement val resultCode:Int? = null,
    @PropertyElement val resultMsg:String? = null
)

@Xml(name="body")
data class Body(
    @Element val items: Items? = null,
    @PropertyElement val numOfRows:Int? = null,
    @PropertyElement val pageNo:Int? = null,
    @PropertyElement val totalCount:Int? = null
)

@Xml
data class Items(
    @Element(name = "item") val item: List<Item>? = null
)

@Xml(name="item")
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