package com.hansung.sherpa.busarrivalinfo

data class BusArrivalInfoResponse(
    val response: Response? = null
)

data class Response(
    val header:Header? = null,
    val body: Body? = null
)

data class Header(
    val resultCode:String? = null,
    val resultMsg:String? = null
)

data class Body(
    val items:Items? = null,
    val numOfRows:Int? = null,
    val pageNo:Int? = null,
    val totalCount:Int? = null
)

data class Items(
    val item: List<Item>
)

data class Item(
    val nodeId:String? = null,
    val nodenm:String? = null,
    val routeid:String? = null,
    val routeno:Int? = null,
    val routetp:String? = null,
    val arrprevstationcnt:Int? = null,
    val vehicletp:String? = null,
    val arrtime:Int? = null
)