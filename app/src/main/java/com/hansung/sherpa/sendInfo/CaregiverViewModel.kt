package com.hansung.sherpa.sendInfo

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.sendInfo.receive.StartNavigationResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ColorPart

class CaregiverViewModel : ViewModel()  {
    private val _coordParts = MutableLiveData(mutableStateListOf(mutableListOf<LatLng>()))
    val coordParts: LiveData<SnapshotStateList<MutableList<LatLng>>> get() = _coordParts

    private val _colorParts = MutableLiveData(mutableListOf<ColorPart>())
    val colorPart: LiveData<MutableList<ColorPart>> get() = _colorParts

    private val _passedRoute = MutableLiveData(mutableStateListOf<Double>())
    val passedRoute: LiveData<SnapshotStateList<Double>> get() = _passedRoute

    fun updatePassedRoute(latLng: LatLng, passedRoute:SnapshotStateList<Double>) {
        _passedRoute.postValue(passedRoute)
    }

    fun receiveRoute(title: String, body: String) {

    }

    fun startNavigation(title: String, body: String) {
        val response = Gson().fromJson(body, StartNavigationResponse::class.java)
        StaticValue.transportRoute = response.transportRoute
        _coordParts.postValue(response.coordParts)
        _colorParts.postValue(response.colorParts)
    }

}
