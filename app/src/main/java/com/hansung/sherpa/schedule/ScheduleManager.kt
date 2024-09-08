package com.hansung.sherpa.schedule

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.ui.preference.calendar.ScheduleData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date

/**
 * @author 6-keem
 *
 * 스케줄 데이터 추가, 수정, 삭제를 수행하는 클래스
 */
class ScheduleManager {
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(ScheduleResponse::class.java, ScheduleDeserializer())
        .create()

    private val retrofitService = Retrofit.Builder()
        .baseUrl(BuildConfig.SHERPA_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ScheduleService::class.java)

    /**
     * 스케줄 찾기
     *
     * @param datetime
     */
    fun findSchedules(datetime : String) : ScheduleResponse? {
        var scheduleResponse : ScheduleResponse? = null
        try {
            runBlocking {
                withContext(Dispatchers.IO) {
                    val response = retrofitService.search(userId = StaticValue.userInfo.userId!!,
                        datetime = datetime).execute()

                    when {
                        response.isSuccessful -> response.body()?.let { scheduleResponse = it}
                        else -> scheduleResponse = null
                    }
                }
            }
        } catch (_ : NullPointerException){
            Log.e("Schedule API", "user data is null")
        }
        return scheduleResponse
    }

    /**
     * 스케줄 삽입
     *
     * @param datetime
     */
    fun insertSchedules(scheduleData: ScheduleData, routeId : Int?){
        try {
            val simpleDateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val start = simpleDateFormatter.format(Date(scheduleData.startDateTime.longValue))
            val end = simpleDateFormatter.format(Date(scheduleData.endDateTime.longValue))

            runBlocking {
                withContext(Dispatchers.IO) {
                    retrofitService.insert(
                        Schedules(
                            title = scheduleData.title.value,
                            address = scheduleData.scheduledLocation.address,
                            userId = StaticValue.userInfo.userId!!,
                            isWholeday = scheduleData.isWholeDay.value,
                            description = scheduleData.comment.value,
                            dateBegin = start,
                            dateEnd = end,
                            guideDatetime = if(scheduleData.scheduledLocation.isGuide)
                                scheduleData.scheduledLocation.guideDatetime.toString() else null,
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

    /**
     * 스케줄 삭제
     *
     * @param scheduleId
     */
    fun deleteSchedules(scheduleId : Int){
        try {
            runBlocking {
                withContext(Dispatchers.IO) {
                    retrofitService.delete(scheduleId).execute()
                }
            }
        } catch (_ : NullPointerException){
            Log.e("Schedule API", "user data is null")
        }
    }

    /**
     * 스케줄 수정
     *
     * @param schedules
     */
    fun updateSchedule(schedules: Schedules){
        runBlocking {
            withContext(Dispatchers.IO) {
                retrofitService.update(schedules).execute()
            }
        }
    }
}