package com.hansung.sherpa.accidentpronearea

data class Coordinate(
    val latitude : Double,
    val longitude : Double
)

data class AccidentProneAreaReqeust (
    val coordinates : List<Coordinate>
)
