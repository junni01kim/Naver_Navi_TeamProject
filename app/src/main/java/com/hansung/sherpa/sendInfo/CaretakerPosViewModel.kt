package com.hansung.sherpa.sendInfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hansung.sherpa.itemsetting.Info
import com.hansung.sherpa.itemsetting.TransportRoute
import com.naver.maps.geometry.LatLng

class CaretakerPosViewModel : ViewModel() {
    private val _latLng = MutableLiveData(LatLng(0.0,0.0))
    val latLng: LiveData<LatLng> get() = _latLng

    private val _passedRoute = MutableLiveData(listOf<Double>())
    val passedRoute: LiveData<List<Double>> get() = _passedRoute

    private val _transportRoute = MutableLiveData(TransportRoute(Info(0.0,0,0,0), listOf()))
    val transportRoute: LiveData<TransportRoute> get() = _transportRoute

    fun updateLatLng(latLng: LatLng) {
        _latLng.postValue(latLng)
    }

    fun updateTransportRoute(transportRoute: TransportRoute) {
        _transportRoute.postValue(transportRoute)
    }

    fun updatePos(latLng: LatLng, passedRoute:List<Double>) {
        _latLng.postValue(latLng)
        _passedRoute.postValue(passedRoute)
    }

    fun getLatLng(title: String, body: String) {
        val caretakerLatLng = Gson().fromJson(body, LatLng::class.java)
        updateLatLng(caretakerLatLng)

        Log.d("FCM Log:getLatLng", "caretaker latLng: $caretakerLatLng")
    }
}
