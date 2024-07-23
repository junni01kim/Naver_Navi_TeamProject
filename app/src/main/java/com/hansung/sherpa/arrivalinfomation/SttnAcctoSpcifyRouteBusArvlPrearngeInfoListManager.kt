package com.hansung.sherpa.arrivalinfomation

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.R
import com.hansung.sherpa.transit.TransitRouteResponse
import com.hansung.sherpa.transit.TransitRouteService
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class SttnAcctoSpcifyRouteBusArvlPrearngeInfoListManager(val context: Context) {
    fun getSttnAcctoSpcifyRouteBusArvlPrearngeInfoList(request:SttnAcctoSpcifyRouteBusArvlPrearngeInfoListRequest):SttnAcctoSpcifyRouteBusArvlPrearngeInfoListResponse {
        val serviceKey = "VCzeRv5K5pqKSztweNXA4lJt8uyxti3zL4LJr7h%2BKEULei9hs7ZRTRU0b5jSlTZQ5i3lycuEN7NS6uACbg4ZwA%3D%3D"
        lateinit var result: SttnAcctoSpcifyRouteBusArvlPrearngeInfoListResponse
        runBlocking {
            launch(Dispatchers.IO) {
                try {
                    val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()
                    var retrofit = Retrofit.Builder()
                        .baseUrl("http://apis.data.go.kr/1613000/ArvlInfoInqireService/")
                        .addConverterFactory(TikXmlConverterFactory.create(parser))
                        .build()
                    val service = retrofit.create(SttnAcctoSpcifyRouteBusArvlPrearngeInfoListService::class.java).getService(request.getMap())
                    //TODO: 해결할 부분 응답 받기

                } catch (e: IOException) {
                    Log.i("Error", "Transit API Exception")
                    // result = SttnAcctoSpcifyRouteBusArvlPrearngeInfoListResponse()
                }
            }
        }
        return result
    }
}