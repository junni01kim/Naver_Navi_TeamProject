package com.hansung.sherpa.busarrivalinfo

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name="response")
data class BusArrivalInfoResponse(
    @Element val header:Header?,
    @Element val body: Body?
)

@Xml(name="header")
data class Header(
    @PropertyElement val resultCode:Int,
    @PropertyElement val resultMsg:String
)

@Xml(name="body")
data class Body(
    @Element val items: Items,
    @PropertyElement val numOfRows:Int,
    @PropertyElement val pageNo:Int,
    @PropertyElement val totalCount:Int
)

@Xml
data class Items(
    @Element(name = "item") val item: List<Item>
)

@Xml(name="item")
data class Item(
    @PropertyElement(name="nodeId") val nodeId:String,
    @PropertyElement(name="nodenm") val nodenm:String,
    @PropertyElement(name="routeid") val routeid:String,
    @PropertyElement(name="routeno") val routeno:Int,
    @PropertyElement(name="routetp") val routetp:String,
    @PropertyElement(name="arrprestationcnt") val arrprevstationcnt:Int,
    @PropertyElement(name="vehicletp") val vehicletp:String,
    @PropertyElement(name="arrtime") val arrtime:Int
)