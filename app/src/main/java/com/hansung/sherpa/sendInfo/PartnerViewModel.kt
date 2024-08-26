package com.hansung.sherpa.sendInfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng

class PartnerViewModel : ViewModel()  {
    private val _latLng = MutableLiveData(LatLng(0.0,0.0))
    val latLng: LiveData<LatLng> get() = _latLng

    fun updateLatLng(latLng: LatLng) {
        _latLng.postValue(latLng)
    }

    fun getLatLng(title: String, body: String) {
        val caretakerLatLng = Gson().fromJson(body, LatLng::class.java)
        updateLatLng(caretakerLatLng)

        Log.d("FCM Log:getLatLng", "caretaker latLng: $caretakerLatLng")
    }
}