package com.hansung.sherpa.transit

data class TmapTransitErrorCode(
    val result: Result
)

data class Result(
    val message: String,
    val status: Int
)
