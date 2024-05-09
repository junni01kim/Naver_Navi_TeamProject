/*

         ----------------  호출 예시  ----------------

        (findViewById<Button>(R.id.Button)).setOnClickListener { it ->
            var address = findViewById<EditText>(R.id.editTextText).text.toString();
            if (address != "")
                GeocodingAPI(address).request()
        }

        ----------------  호출 예시  ----------------

 */

package com.hansung.sherpa.geocoding

import android.net.Uri
import android.util.Log
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.convert.Coordinate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ConcurrentHashMap

class GeocodingAPI() {
    private val coordinatesHashMap = ConcurrentHashMap<String,Coordinate>()
    private val flagHashMap = ConcurrentHashMap<String, Boolean>()

    fun request(address : String, key : String){
        flagHashMap.put(key, false);
        val encodedAddress : String = Uri.encode(address)
        GeocodingRetrofitManager.getGeocodingServiceInstance()
            .geocoding(BuildConfig.TMAP_APP_KEY,"1", encodedAddress)
            .enqueue(object : Callback<Geocoding> {
                override fun onResponse(call: Call<Geocoding>, response: Response<Geocoding>) {
                    if(response.isSuccessful) {
                        try {
                            val coordinate = response.body()?.coordinateInfo?.coordinate?.get(0)
                            val lat: String = if (coordinate?.lat != null) coordinate.lat!!
                            else throw IllegalArgumentException("null")

                            val lon: String = if (coordinate.lon != null) coordinate.lon!!
                            else throw IllegalArgumentException("null")

                            coordinatesHashMap[key] = Coordinate(lat.toDouble(),lon.toDouble())
                            flagHashMap[key] = true
                        } catch (_: Exception) {
                            Log.d("status", "GeocodingAPI : Exception")
                        }
                    }
                }
                override fun onFailure(call: Call<Geocoding>, t: Throwable) {
                    Log.d("status", "GeocodingAPI : failure")
                }
            })
    }

    fun getFlag(key : String) : Boolean? {
        return flagHashMap[key]
    }

    fun getCoordinate(key : String) : Coordinate?{
        return coordinatesHashMap[key];
    }
}
