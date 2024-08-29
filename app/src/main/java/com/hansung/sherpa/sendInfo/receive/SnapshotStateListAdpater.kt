package com.hansung.sherpa.sendInfo.receive

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class SnapshotStateListAdapter : JsonDeserializer<SnapshotStateList<Double>> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): SnapshotStateList<Double> {
        val list = mutableStateListOf<Double>()
        json.asJsonArray.forEach { element ->
            list.add(element.asDouble)
        }
        return list
    }
}