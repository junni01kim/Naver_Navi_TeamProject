package com.hansung.sherpa.routegraphic


data class RouteGraphicResponse (
    val result: Result? = null
)

data class Result(
    val lane: List<Lane?>? = null,
    val boundary: Boundary? = null
)

data class Lane(
    val `class`: Int? = null,
    val type:Int? = null,
    val section:List<Section>? = null
)

data class Section(
    val graphPos: List<GraphPos>?
)

data class GraphPos(
    val x:Double? = null,
    val y:Double? = null
)

data class Boundary(
    val left:Double,
    val top:Double,
    val right:Double,
    val bottom:Double,
)