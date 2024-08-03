package com.hansung.sherpa.transit

data class PedestrianResponse(
    val features: List<Feature>?=null,
    val type: String? = null
) {
    data class Feature(
        val geometry: Geometry,
        val properties: Properties,
        val type: String
    ) {
        data class Geometry(
            val coordinates: List<List<Double>>,
            val type: String
        )

        data class Properties(
            val categoryRoadType: Int,
            val description: String,
            val direction: String,
            val distance: Int,
            val facilityName: String,
            val facilityType: String,
            val index: Int,
            val intersectionName: String,
            val lineIndex: Int,
            val name: String,
            val nearPoiName: String,
            val nearPoiX: String,
            val nearPoiY: String,
            val pointIndex: Int,
            val pointType: String,
            val roadType: Int,
            val time: Int,
            val totalDistance: Int,
            val totalTime: Int,
            val turnType: Int
        )
    }
}