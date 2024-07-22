package com.hansung.sherpa.arrivalinfomation

//ArrivalInfo
data class SttnAcctoSpcifyRouteBusArvlPrearngeInfoListResponse(
    val header:Header,
    val bodu: Body
)

data class Header(
    val resultCode:Int,
    val resultMsg:String
)

data class Body(
    val items: List<Item>,
    val numOfRows:Int,
    val pageNo:Int,
    val totalCount:Int
)

data class Item(
    val nodeId:String,
    val nodenm:String,
    val routeid:String,
    val routeno:Int,
    val routetp:String,
    val arrprevstationcnt:Int,
    val vehicletp:String,
    val arrtime:Int
)