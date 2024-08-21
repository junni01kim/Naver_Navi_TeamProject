package com.hansung.sherpa.transit.tmap

data class TmapTransitErrorCode(
    val result: Result? = null
)

data class Result(
    val message: String,
    val status: Int
)
