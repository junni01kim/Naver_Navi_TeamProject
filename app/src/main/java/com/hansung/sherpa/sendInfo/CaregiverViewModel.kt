package com.hansung.sherpa.sendInfo

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.sendInfo.receive.ReceiveManager
import com.hansung.sherpa.sendInfo.receive.ReceiveRouteResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ColorPart

class CaregiverViewModel : ViewModel()  {
    private val _coordParts = MutableLiveData(mutableStateListOf(mutableListOf(LatLng(0.0,0.0), LatLng(0.0,0.0))))
    val coordParts: LiveData<SnapshotStateList<MutableList<LatLng>>> get() = _coordParts

    private val _colorParts = MutableLiveData(mutableListOf(ColorPart()))
    val colorPart: LiveData<MutableList<ColorPart>> get() = _colorParts

    private val _passedRoute = MutableLiveData(mutableStateListOf(0.0))
    val passedRoute: LiveData<SnapshotStateList<Double>> get() = _passedRoute

    fun updatePassedRoute(passedRoute:SnapshotStateList<Double>) {
        _passedRoute.postValue(passedRoute)
    }

    fun startNavigation(title: String, body: String) {
        val responseJson = ReceiveManager().getTransportRoute().data!!.replace("\\","")
        val trimmedJson = responseJson.substring(1, responseJson.length - 1)
        Log.d("API Log", "모든 \\삭제, 앞뒤 \"삭제: ${trimmedJson}")
        val response = Gson().fromJson(trimmedJson,TransportRoute::class.java)

        StaticValue.transportRoute = response
    }

    fun devateRoute(title: String, body: String) {
        val responseJson = ReceiveManager().getRoute().data!!.replace("\\","")
        val trimmedJson = responseJson.substring(1, responseJson.length - 1)
        Log.d("API Log", "모든 \\삭제, 앞뒤 \"삭제: ${trimmedJson}")
        val response = Gson().fromJson(trimmedJson,ReceiveRouteResponse::class.java)

        _coordParts.postValue(response.coordParts)
        _colorParts.postValue(response.colorParts)
    }
}
