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

    override fun write(out: JsonWriter, value: SubPath) {
        // Begin the JSON object
        out.beginObject()

        // Write trafficType
        out.name("trafficType").value(value.trafficType)

        // Write sectionInfo
        out.name("sectionInfo")
        val gson = GsonBuilder()
            .registerTypeAdapter(object : TypeToken<Lane>() {}.type, LaneSerialize(value.trafficType))
            .create()
        when (value.sectionInfo) {
            is SubwaySectionInfo -> gson.toJson(value.sectionInfo, SubwaySectionInfo::class.java, out)
            is BusSectionInfo -> gson.toJson(value.sectionInfo, BusSectionInfo::class.java, out)
            is PedestrianSectionInfo -> gson.toJson(value.sectionInfo, PedestrianSectionInfo::class.java, out)
            else -> throw JsonParseException("Unknown sectionInfo type")
        }

        // Write sectionRoute
        out.name("sectionRoute")
        gson.toJson(value.sectionRoute, SectionRoute::class.java, out)

        // End the JSON object
        out.endObject()
    }

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
