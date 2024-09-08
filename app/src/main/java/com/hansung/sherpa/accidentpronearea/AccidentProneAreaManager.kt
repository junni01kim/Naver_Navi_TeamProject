package com.hansung.sherpa.accidentpronearea

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.Url
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

/**
 * @author 6-keem
 *
 * 보행자 사고 다발 지역 검색하는 API
 *
 */
class AccidentProneAreaManager {
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(AccidentProneAreaResponse::class.java, AccidentProneAreaDeserializer())
        .create()

    private val retrofitService = Retrofit.Builder()
        .baseUrl(Url.SHERPA)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(SearchAccidentProneAreaService::class.java)

    /**
     * @param latLongs 좌표들
     * @param callback 클백 함수
     *
     * 여러 좌표를 서버로 넘기면 해당 되는 보행자 사고 다발 지역을 return
     * 해당 좌표를 바탕으로 보행자 사고 다발 구역의 중심 점과 반지름을 계산하여 callback
     */
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

    /**
     * 보행자 사고 다발 지역 폴리곤의 중심 게산 하는 함수
     *
     * 1. 각 폴리곤의 한 점에 대한 각각의 점의 거리를 구하고
     * 2. 가장 거리가 긴 두 점의 중간을 중심으로 설정
     * 3. 거리를 반으로 나눠 반지름으로 설저
     * 4. 리스트에 담아서 return
     */
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