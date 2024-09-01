package com.hansung.sherpa.schedule

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import java.lang.reflect.Type

// TODO: 추후 dateBegin -> Long 변환 작업 수행 예정 
class ScheduleDeserializer: JsonDeserializer<ScheduleResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ScheduleResponse {
        val jsonObject = json?.asJsonObject ?: throw JsonSyntaxException("Response is null")
        val schedulesList = mutableListOf<Schedules>()

        val schedulesJsonArray = jsonObject["data"].asJsonArray
        schedulesJsonArray?.forEach { schedulesJsonElement ->
            val schedulesJsonObject = schedulesJsonElement.asJsonObject
            val wholeday = schedulesJsonObject["isWholeday"].asBoolean
            schedulesList.add(
                Schedules(
                    scheduleId = schedulesJsonObject["scheduleId"].asInt,
                    routeId = schedulesJsonObject["routeId"].asInt,
                    userId = schedulesJsonObject["userId"].asInt,
                    title = schedulesJsonObject["title"].asString,
                    description = schedulesJsonObject["description"].asString,
                    dateBegin = schedulesJsonObject["dateBegin"].asString,
                    dateEnd = schedulesJsonObject["dateEnd"].asString,
                    address = schedulesJsonObject["address"].asString,
                    isWholeday = wholeday,
                    guideDatetime = try {
                        schedulesJsonObject["guideDatetime"].asString
                    } catch (_ : UnsupportedOperationException) {
                        null
                    }
                )
            )
        }

        return ScheduleResponse(
            code = jsonObject["code"].asInt,
            message = jsonObject["message"].asString,
            data = schedulesList
        )
    }
}