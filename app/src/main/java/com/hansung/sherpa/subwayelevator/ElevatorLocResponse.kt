package com.hansung.sherpa.subwayelevator

data class ElevatorLocResponse(
    val code: Int,
    val `data`: List<Data>,
    val message: String
) {
    data class Data(
        val elevatorId: Int,
        val location: String,
        val stationName: String
    )
}