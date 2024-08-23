package com.hansung.sherpa.accidentpronearea

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.naver.maps.geometry.LatLng
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class PolygonCenter(
    val radius : Double,
    val center : LatLng
)
interface AccidentProneAreaCallback{
    fun onSuccess(accidentProneAreas: ArrayList<AccidentProneArea>, listOfCenter : List<PolygonCenter>)
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
        if(response.isSuccessful) {
            val accidentProneAreas = response.body()!!.data
            val listOfCenter = centerOfPolygon(accidentProneAreas)
            callback.onSuccess(accidentProneAreas, listOfCenter)
        }
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

    private fun centerOfPolygon(accidentProneAreas: ArrayList<AccidentProneArea>)
        : List<PolygonCenter> {
        val listOfCetner = mutableListOf<PolygonCenter>()
        accidentProneAreas.forEach { accidentProneArea ->
            accidentProneArea.polygons?.coordinates?.forEach { polygon ->
                if(polygon.isNotEmpty()) {
                    val point = polygon[0]
                    var center : LatLng? = null
                    var maxDistance = Double.MIN_VALUE
                    for(i : Int in 1..<polygon.size){
                        val distance = distanceCalculate(point.latitude, point.longitude, polygon[i].latitude, polygon[i].longitude)
                        if(maxDistance < distance){
                            maxDistance = distance
                            center = LatLng((point.latitude + polygon[i].latitude)/2,
                                (point.longitude + polygon[i].longitude) / 2)
                        }
                    }
                    if(center != null)
                        listOfCetner.add(PolygonCenter(maxDistance/2, center))
                }
            }
        }
        return listOfCetner
    }

    companion object {
        fun distanceCalculate(x1: Double, y1: Double, x2: Double, y2: Double): Double {
            val distance: Double
            val radius = 6371.0
            val toRadian = Math.PI / 180

            val deltaLatitude = abs(x1 - x2) * toRadian
            val deltaLongitude = abs(y1 - y2) * toRadian

            val sinDeltaLat = sin(deltaLatitude / 2)
            val sinDeltaLng = sin(deltaLongitude / 2)
            val squareRoot = sqrt(
                sinDeltaLat * sinDeltaLat
                        + cos(x1 * toRadian) * cos(x2 * toRadian) * sinDeltaLng * sinDeltaLng
            )
            distance = 2 * radius * asin(squareRoot)
            return distance
        }
    }
}