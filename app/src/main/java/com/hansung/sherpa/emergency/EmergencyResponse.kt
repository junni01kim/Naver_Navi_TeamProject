package com.hansung.sherpa.emergency

data class EmergencyResponse (
    val code: Int? = null,
    val message: String? = null,
    val `data`: Emergency? = null
)

data class EmergencyListResponse (
    val code: Int? = null,
    val message: String? = null,
    val `data`: MutableList<Emergency>? = null
)

data class DeleteEmergencyResponse (
    val code: Int? = null,
    val message: String? = null,
    val `data`: String? = null
)