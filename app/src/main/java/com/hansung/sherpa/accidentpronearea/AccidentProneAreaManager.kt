package com.hansung.sherpa.accidentpronearea

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.naver.maps.geometry.LatLng
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AccidentProneAreaCallback{
    fun onSuccess(accidentProneAreas: ArrayList<AccidentProneArea>)
    fun onFailure(message : String)
}

class AccidentProneAreaManager {

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(AccidentProneAreaResponse::class.java, AccidentProneAreaDeserializer())
        .create()

    private val retrofitService = Retrofit.Builder()
        .baseUrl("http://13.209.212.166:8080")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(SearchAccidentProneAreaService::class.java)

    fun request(latLongs: MutableList<LatLng>, callback: AccidentProneAreaCallback){
        val response = retrofitService.search(body = createRequestBody(latLongs)).execute()
        if(response.isSuccessful)
            callback.onSuccess(response.body()!!.data)
        else
            callback.onFailure(response.body()!!.message)
    }

    private fun createRequestBody(latLongs: MutableList<LatLng>) : AccidentProneAreaReqeust{
        val list = mutableListOf<Coordinate>()
        latLongs.forEach {
            list.add(Coordinate(it.latitude, it.longitude))
        }
        return AccidentProneAreaReqeust(list)
    }
}