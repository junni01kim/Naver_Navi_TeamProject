package com.hansung.sherpa.transit

data class OdsayTransitRouteErrorCode(
    val error: OdsayError
)

data class OdsayError(
    val msg: String,
    val code: Int
)