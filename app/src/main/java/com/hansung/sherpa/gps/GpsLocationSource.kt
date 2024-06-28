package com.hansung.sherpa.gps

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.hansung.sherpa.deviation.StrengthLocation
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.util.FusedLocationSource


/**
 * 실시간 GPS 위성 개수 반환 받기 위한 클래스 (GPS 정확도 판단 위함)
 * @author 6-keem
 * @param context GPS 권한 확인을 위함
 */
@RequiresApi(Build.VERSION_CODES.R)
class GPSDatas(val context : Context){
    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val myGnssStatus : MyGnssStatus = MyGnssStatus()
    private val GOOD_SIGNAL = 4

    /**
     * Constructor
     * 권한 확인 및 위성 개수 변경 시 Callback되는 MyGnssStatus 클래스 등록
     */
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

    /**
     * 함수 호출 시 GPS 위성 개수에 따른 정확도(세기) 전달 됨
     * @return StrengthLocation(accuracy : String, latLng : Latlng)
     */
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


/**
 *  static fusedLocationSource 클래스 반환 받을 수 있는 클래스
 *  @author 6-keem
 *
 */

class GpsLocationSource {
    companion object {
        @Volatile
        private lateinit var fusedLocationSource: FusedLocationSource
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

        /**
         * @return fusedLocationSource? - 사전에 createInstance 호출한 적 있어야 함
         */
        fun getInstance() : FusedLocationSource? {
            if(!this::fusedLocationSource.isInitialized)
                return fusedLocationSource
            return null
        }

        fun createInstance(activity: Activity) : FusedLocationSource {
            if (!this::fusedLocationSource.isInitialized) {
                synchronized(this) {
                    if (!this::fusedLocationSource.isInitialized) {
                        fusedLocationSource = FusedLocationSource(activity,LOCATION_PERMISSION_REQUEST_CODE)
                    }
                }
            }
            return fusedLocationSource
        }
        fun createInstance(fragment : Fragment) : FusedLocationSource {
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