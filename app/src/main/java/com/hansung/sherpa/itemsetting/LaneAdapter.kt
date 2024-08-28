package com.hansung.sherpa.itemsetting

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
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