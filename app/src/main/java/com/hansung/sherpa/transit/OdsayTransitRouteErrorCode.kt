package com.hansung.sherpa.transit

data class ODsayTransitRouteErrorCode(
    val error: ODsayError
)

data class ODsayError(
    val msg: String,
    val code: Int
)