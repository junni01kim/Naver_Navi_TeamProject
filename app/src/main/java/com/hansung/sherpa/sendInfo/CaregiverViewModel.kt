package com.hansung.sherpa.sendInfo

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.sendInfo.receive.ReceiveRouteResponse
import com.hansung.sherpa.sendInfo.receive.StartNavigationResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ColorPart
import java.nio.ByteBuffer

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
        val buff = ByteBuffer.allocate(body.length / 2)
        for (i in 0..body.length - 1 step 2) {
            buff.put(Integer.parseInt(body.substring(i, i + 2), 16).toByte())
        }
        buff.rewind()
        val decode = Charsets.UTF_8.decode(buff).toString()

        val response = Gson().fromJson(decode, TransportRoute::class.java)
        StaticValue.transportRoute = response
    }

    fun devateRoute(title: String, body: String) {
        val buff = ByteBuffer.allocate(body.length / 2)
        for (i in 0..body.length - 1 step 2) {
            buff.put(Integer.parseInt(body.substring(i, i + 2), 16).toByte())
        }
        buff.rewind()
        val decode = Charsets.UTF_8.decode(buff).toString()

        val response = Gson().fromJson(decode, ReceiveRouteResponse::class.java)
        _coordParts.postValue(response.coordParts)
        _colorParts.postValue(response.colorParts)
    }

}
