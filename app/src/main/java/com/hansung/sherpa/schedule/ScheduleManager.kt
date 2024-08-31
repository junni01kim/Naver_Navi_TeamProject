package com.hansung.sherpa.schedule

import android.util.Log
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.ui.preference.calendar.ScheduleData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date

interface ScheduleFindCallback{
    fun onSuccess(scheduleData: List<Schedules>)
    fun onFailure(message : String)
}

interface ScheduleInsertCallback{
    fun onSuccess(scheduleData : ScheduleResponse)
    fun onFailure(message : String)
}

class ScheduleManager {
    private val retrofitService = Retrofit.Builder()
        .baseUrl(BuildConfig.SHERPA_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ScheduleService::class.java)

    fun findSchedules(datetime : String, callback: ScheduleFindCallback){
        try {
            runBlocking(Dispatchers.IO) {
                run {
                    var query = URLEncoder.encode(datetime)
                    val response = retrofitService.search(userId = StaticValue.userInfo.userId!!,
                        datetime = query).execute()

                    when {
                        response.isSuccessful -> response.body()?.let { callback.onSuccess(it.scheduleData)}
                        else -> callback.onFailure(response.message())
                    }
                }
            }
        } catch (_ : NullPointerException){
            Log.e("Schedule API", "user data is null")
        }
    }

    fun insertSchedules(scheduleData: ScheduleData, routeId : Int){
        try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val start = simpleDateFormat.format(Date(scheduleData.startDateTime.longValue))
            val end = simpleDateFormat.format(Date(scheduleData.endDateTime.longValue))

            runBlocking {
                withContext(Dispatchers.IO) {
                    retrofitService.insert(
                        Schedules(
                            title = scheduleData.title.value,
                            address = scheduleData.scheduledLocation.address,
                            userId = StaticValue.userInfo.userId!!,
                            isWholeDay = scheduleData.isWholeDay.value,
                            description = scheduleData.comment.value,
                            dateBegin = start,
                            dateEnd = end,
                            guideDatetime = scheduleData.scheduledLocation.guideDatetime.toString(),
                            routeId = routeId,
                            scheduleId = null
                        )
                    ).execute()
                }
            }
        } catch (_ : NullPointerException){
            Log.e("Schedule API", "user data is null")
        }
    }

    fun deleteSchedules(scheduleId : Int){
        try {
            runBlocking(Dispatchers.IO) {
                run {
                    retrofitService.delete(scheduleId)
                }
            }
        } catch (_ : NullPointerException){
            Log.e("Schedule API", "user data is null")
        }
    }
}