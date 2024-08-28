package com.hansung.sherpa.itemsetting

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class SubPathAdapter : TypeAdapter<SubPath>() {

    override fun write(out: JsonWriter, value: SubPath) {}

    override fun read(reader: JsonReader): SubPath {
        val jsonObject = JsonParser.parseReader(reader).asJsonObject

        val trafficType = jsonObject.get("trafficType")?.asInt ?: throw JsonParseException("Missing trafficType")
        val gson = GsonBuilder()
            .registerTypeAdapter(object : TypeToken<Lane>() {}.type, LaneDeserialize(trafficType))
            .create()
        val sectionInfo = when (trafficType) {
            1 -> gson.fromJson(jsonObject.getAsJsonObject("sectionInfo"), SubwaySectionInfo::class.java)
            2 ->  gson.fromJson(jsonObject.getAsJsonObject("sectionInfo"), BusSectionInfo::class.java)
            3 -> Gson().fromJson(jsonObject.getAsJsonObject("sectionInfo"), PedestrianSectionInfo::class.java)
            else -> throw JsonParseException("Unknown trafficType: $trafficType")
        }

        val sectionRoute = Gson().fromJson(jsonObject.getAsJsonObject("sectionRoute"), SectionRoute::class.java)

        return SubPath(
            trafficType = trafficType,
            sectionInfo = sectionInfo,
            sectionRoute = sectionRoute
        )
    }
}
