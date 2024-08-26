package com.hansung.sherpa.sendInfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hansung.sherpa.itemsetting.Info
import com.hansung.sherpa.itemsetting.TransportRoute
import com.naver.maps.geometry.LatLng

class CaretakerViewModel : ViewModel() {
    private val _transportRoute = MutableLiveData(TransportRoute(Info(0.0,0,0,0), listOf()))
    val transportRoute: LiveData<TransportRoute> get() = _transportRoute

    fun updateTransportRoute(transportRoute: TransportRoute) {
        _transportRoute.postValue(transportRoute)
        //navigate()
    }
}
