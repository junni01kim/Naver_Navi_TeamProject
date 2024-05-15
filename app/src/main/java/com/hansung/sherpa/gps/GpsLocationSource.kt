package com.hansung.sherpa.gps

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.hansung.sherpa.MainActivity
import com.hansung.sherpa.convert.Coordinate
import com.hansung.sherpa.deviation.StrengthLocation
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.util.FusedLocationSource

@RequiresApi(Build.VERSION_CODES.R)
class GPSDatas(val context : Context){
    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val myGnssStatus : MyGnssStatus = MyGnssStatus()
    private val GOOD_SIGNAL = 4
    init {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            context
        }
        locationManager.registerGnssStatusCallback(myGnssStatus,null)
    }
    fun getGpsSignalAccuracy() : StrengthLocation{
        return try {
            val lastLocation = GpsLocationSource.getInstance()?.lastLocation!!
            val latLng : LatLng = lastLocation.let { LatLng(it.latitude, it.longitude) }

            if(myGnssStatus.satelliteCount < GOOD_SIGNAL)
                StrengthLocation("Weak", latLng)
            else
                StrengthLocation("Strong", latLng)
        } catch (_:NullPointerException){
            StrengthLocation("Weak", LatLng(0.0,0.0))
        }
    }
}

class GpsLocationSource {
    companion object {
        @Volatile
        private var coordinate: Coordinate = Coordinate(0.0,0.0)
        private lateinit var fusedLocationSource: FusedLocationSource
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

        fun getInstance() : FusedLocationSource? {
            if(!this::fusedLocationSource.isInitialized)
                return fusedLocationSource
            return null
        }

        fun getInstance(activity: MainActivity) : FusedLocationSource {
            if (!this::fusedLocationSource.isInitialized) {
                synchronized(this) {
                    if (!this::fusedLocationSource.isInitialized) {
                        fusedLocationSource = FusedLocationSource(activity,LOCATION_PERMISSION_REQUEST_CODE)
                    }
                }
            }
            return fusedLocationSource
        }
        fun getInstance(fragment : Fragment) : FusedLocationSource {
            if (!this::fusedLocationSource.isInitialized) {
                synchronized(this) {
                    if (!this::fusedLocationSource.isInitialized) {
                        fusedLocationSource = FusedLocationSource(fragment,LOCATION_PERMISSION_REQUEST_CODE)
                    }
                }
            }
            return fusedLocationSource
        }

    }
}