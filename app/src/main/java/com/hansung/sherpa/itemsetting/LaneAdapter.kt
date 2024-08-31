package com.hansung.sherpa.itemsetting

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class LaneDeserialize(private val trafficType: Int) : JsonDeserializer<Lane> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Lane {
        return when (trafficType) {
            1 -> Gson().fromJson(json, SubwayLane::class.java)
            2 -> Gson().fromJson(json, BusLane::class.java)
            else -> throw JsonParseException("Unknown trafficType: $trafficType")
        }
    }
}

class LaneSerialize(private val trafficType: Int) : JsonSerializer<Lane> {
    override fun serialize(
        src: Lane?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val jsonObject = JsonObject()

        when (trafficType) {
            1 -> {
                // trafficType 1 corresponds to Subway
                val subwayLane = src as SubwayLane
                jsonObject.addProperty("name", subwayLane.name)
                jsonObject.addProperty("subwayCode", subwayLane.subwayCode)
                jsonObject.addProperty("subwayCityCode", subwayLane.subwayCityCode)
            }
            2 -> {
                // trafficType 2 corresponds to Bus
                val busLane = src as BusLane
                jsonObject.addProperty("busNo", busLane.busNo)
                jsonObject.addProperty("type", busLane.type)
                jsonObject.addProperty("busID", busLane.busID)
                jsonObject.addProperty("busLocalBlID", busLane.busLocalBlID)
                jsonObject.addProperty("busProviderCode", busLane.busProviderCode)
            }
            else -> throw IllegalArgumentException("Unknown trafficType: $trafficType")
            }

        return jsonObject
    }
}