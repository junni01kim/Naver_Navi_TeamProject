package com.hansung.sherpa.sendInfo

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.itemsetting.SubPath
import com.hansung.sherpa.itemsetting.SubPathAdapter
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.sendInfo.receive.ReceiveManager
import com.hansung.sherpa.sendInfo.receive.ReceiveRouteResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ColorPart
import com.owlike.genson.GensonBuilder
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
        val responseJson = ReceiveManager().getTransportRoute().data!!

        val json = responseJson
            .substring(1, responseJson.length - 1)
            .chunked(2)
            .map { Integer.parseInt(it, 16).toByte() }
            .toByteArray()
            .toString(Charsets.UTF_8)

        Log.d("API Log", "반환: $json")
        val gson = GsonBuilder()
            .registerTypeAdapter(SubPath::class.java, SubPathAdapter())
            .create()
        val response = gson.fromJson(json, TransportRoute::class.java)

        StaticValue.transportRoute = response
    }

    fun devateRoute(title: String, body: String) {
        val responseJson = ReceiveManager().getRoute().data!!

        val json = responseJson
            .substring(1, responseJson.length - 1)
            .chunked(2)
            .map { Integer.parseInt(it, 16).toByte() }
            .toByteArray()
            .toString(Charsets.UTF_8)

        Log.d("API Log", "반환: ${json}")

        val response = Gson().fromJson(json,ReceiveRouteResponse::class.java)

        _coordParts.postValue(response.coordParts)
        _colorParts.postValue(response.colorParts)
    }
}
