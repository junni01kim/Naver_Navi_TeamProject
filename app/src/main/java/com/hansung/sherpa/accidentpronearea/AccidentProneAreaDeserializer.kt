package com.hansung.sherpa.accidentpronearea

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.naver.maps.geometry.LatLng
import java.lang.reflect.Type

fun removeEscapeRegex(str : String) : String {
    val pattern = "\\\\\"".toRegex()
    return pattern.replace(str, "\"")
}

class AccidentProneAreaDeserializer : JsonDeserializer<AccidentProneAreaResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): AccidentProneAreaResponse {

        val jsonObject = json?.asJsonObject ?: throw JsonSyntaxException("Response is null")

        val code = jsonObject["code"].asInt
        val message = jsonObject["message"].asString

        val data = jsonObject["data"].asJsonArray.map { dataElement ->
            val dataObject = dataElement.asJsonObject
            val accidentProneAreaFid = dataObject["accidentProneAreaFid"].asInt
            val accidentProneAreaId = dataObject["accidentProneAreaId"].asInt
            val beopjeongdongCode = dataObject["beopjeongdongCode"].asString
            val jijeomCode = dataObject["jijeomCode"].asInt
            val address = dataObject["address"].asString
            val jijeomName = dataObject["jijeomName"].asString
            val accidentCount = dataObject["accidentCount"].asInt
            val casualtyCount = dataObject["casualtyCount"].asInt
            val deathsCount = dataObject["deathsCount"].asInt
            val seriouslyInjuredCount = dataObject["seriouslyInjuredCount"].asInt
            val minorInjuredCount = dataObject["minorInjuredCount"].asInt
            val reportedInjuredCount = dataObject["reportedInjuredCount"].asInt
            val longitude = dataObject["longitude"].asDouble
            val latitude = dataObject["latitude"].asDouble

            val polygonsString = dataObject["polygons"].asString
            val cleanedPolygonsString = removeEscapeRegex(polygonsString)
            val polygonObject = JsonParser.parseString(cleanedPolygonsString).asJsonObject
            val polygonsArray = polygonObject["coordinates"].asJsonArray

            val polygons = mutableListOf<List<LatLng>>()
            polygonsArray.forEach {
                val polygonJsonArray = it.asJsonArray
                val polygon = mutableListOf<LatLng>()
                polygonJsonArray.forEach { coordinateJsonArray ->
                    val coordinate = coordinateJsonArray.asJsonArray
                    polygon.add(LatLng(coordinate[1].asDouble, coordinate[0].asDouble))
                }
                polygons.add(polygon)
            }

//            val polygon = context?.deserialize<Polygon>(polygonObject, Polygon::class.java)

            AccidentProneArea(
                accidentProneAreaFid = accidentProneAreaFid,
                accidentProneAreaId = accidentProneAreaId,
                beopjeongdongCode = beopjeongdongCode,
                jijeomCode = jijeomCode,
                address = address,
                jijeomName = jijeomName,
                accidentCount = accidentCount,
                casualtyCount = casualtyCount,
                deathsCount = deathsCount,
                seriouslyInjuredCount = seriouslyInjuredCount,
                minorInjuredCount = minorInjuredCount,
                reportedInjuredCount = reportedInjuredCount,
                longitude = longitude,
                latitude = latitude,
                polygons = Polygon(
                    type = polygonObject["type"].asString,
                    coordinates = polygons
                )
            )
        } as ArrayList<AccidentProneArea>

        return AccidentProneAreaResponse(
            code = code,
            message = message,
            data = data
        )
    }
}