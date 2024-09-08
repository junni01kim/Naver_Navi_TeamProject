package com.hansung.sherpa.searchlocation

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import java.lang.reflect.Type

fun stripHTMLTags(html: String): String {
    val tagRegex = Regex("<[^>]*>")
    var processedHtml = html.replace(Regex("</?b>"), "")
    processedHtml = processedHtml.replace(tagRegex, "")
    val multipleSpacesRegex = Regex("\\s{2,}")
    return multipleSpacesRegex.replace(processedHtml, " ").trim()
}

class SearchLocationDeserializer : JsonDeserializer<SearchLocationResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): SearchLocationResponse {

        val jsonObject = json?.asJsonObject ?: throw JsonSyntaxException("Response is null")
        val itemsList = mutableListOf<Items>()

        try {
            val jsonArray = jsonObject["items"].asJsonArray
            for (item in jsonArray) {
                val itemObject = item.asJsonObject
                itemsList.add(
                    Items(
                        title = stripHTMLTags(itemObject["title"].asString),
                        link = itemObject["link"].asString,
                        category = itemObject["category"].asString,
                        description = stripHTMLTags(itemObject["description"].asString),
                        telephone = itemObject["telephone"].asString,
                        address = itemObject["address"].asString,
                        roadAddress = itemObject["roadAddress"].asString,
                        mapx = insertDecimal(itemObject["mapx"].asInt,3).toDouble(),
                        mapy = insertDecimal(itemObject["mapy"].asInt,2).toDouble()
                    )
                )
            }
        } catch (exception: Exception) {
            println("Error parsing items: ${exception.message}")
            throw JsonSyntaxException("Error parsing items", exception)
        }

        return SearchLocationResponse(
            lastBuildDate = jsonObject["lastBuildDate"]?.asString,
            total = jsonObject["total"]?.asInt,
            start = jsonObject["start"]?.asInt,
            display = jsonObject["display"]?.asInt,
            items = itemsList.toCollection(ArrayList())
        )
    }
}

fun insertDecimal(number: Int, position: Int): String {
    val numberStr = number.toString()
    val length = numberStr.length

    return if (length > position) {
        val integerPart = numberStr.substring(0, position)
        val decimalPart = numberStr.substring(position)
        "$integerPart.$decimalPart"
    } else {
        "0.${numberStr.padStart(position, '0')}"
    }
}