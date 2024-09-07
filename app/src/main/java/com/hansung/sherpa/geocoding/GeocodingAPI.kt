
package com.hansung.sherpa.geocoding

import android.net.Uri
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.Url
import com.hansung.sherpa.convert.Coordinate
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface GeocodingAPICallBack{
    fun onSuccess(coord : List<Coordinate>)
    fun onFailure(message : String)
}

class GeocodingAPI(){
    val retrofitService = Retrofit.Builder()
        .baseUrl(Url.TMAP)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeocodingService::class.java)
    fun request(address : String, geocodingAPICallBack: GeocodingAPICallBack){
        val encodedAddress : String = Uri.encode(address)

        retrofitService.geocoding(Url.TMAP, "1", encodedAddress)
            .execute()
            .also { response ->
                if(response.isSuccessful){
                    val coordinateList = arrayListOf<Coordinate>()
                    response.body()?.coordinateInfo?.coordinate?.let { coordinates ->
                        coordinates.forEachIndexed { _, item ->
                            val lat = item.lat
                            val lon = item.lon
                            if (lat != null && lon != null) {
                                coordinateList.add(Coordinate(lat.toDouble(), lon.toDouble()))
                            }
                        }
                    }
                    geocodingAPICallBack.onSuccess(coordinateList)
                }
                else {
                    geocodingAPICallBack.onFailure("Geocoding API requests failed.")
                }
            }
    }
}
