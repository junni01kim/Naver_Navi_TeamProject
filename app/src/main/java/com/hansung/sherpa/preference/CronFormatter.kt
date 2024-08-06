package com.hansung.sherpa.preference

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class CronFormatter {
    companion object {
        private lateinit var dateTime : Calendar
        fun format(index : Int, mills : Long) : String{
            dateTime = Calendar.getInstance().apply {
                timeInMillis = mills
            }
            return when(index){
                // 매일
                1 -> convert(getMinute(), getHour(), "*", "*", "*", "*")
                //매주
                2 -> convert(getMinute(), getHour(), "*", "*", "*", getDayOfWeek())
                //2주마다
                3 -> convert(getMinute(), getHour(), "*", "*", "*", "*")
                //매월
                4 -> convert(getMinute(), getHour(), getDayOfMonth(), "*", "*", "*")
                //매년
                5 -> convert(getMinute(), getHour(), getDayOfMonth(), getDayOfMonth(), "*", "*")
                else -> throw NullPointerException("Exception while to set cron")
            }
        }
        fun convert(minute : String = "*", hour : String = "*", dayOfMonth : String = "*",
                    month : String = "*", year : String = "*", dayOfWeek : String = "*") : String {
            return "0 $minute $hour $dayOfMonth ${month} $year $dayOfWeek"
        }

        private fun getMinute() : String { return dateTime.get(Calendar.MINUTE).toString() }
        private fun getHour() : String { return dateTime.get(Calendar.HOUR).toString() }
        private fun getDayOfMonth() : String { return dateTime.get(Calendar.DAY_OF_MONTH).toString() }
        private fun getMonth() : String { return (dateTime.get(Calendar.MONTH) + 1).toString() }
        private fun getYear() : String { return dateTime.get(Calendar.YEAR).toString() }
        private fun getDayOfWeek() : String { return dateTime.get(Calendar.DAY_OF_WEEK).toString() }
    }
}