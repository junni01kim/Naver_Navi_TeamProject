package com.hansung.sherpa.fcm.getUserPos

import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket

/**
 * 뤼튼 발 UDP 통신
 */
val port = 8080
class UserPosReceiver(private val getUserPos: (LatLng) -> Unit) {
    private var job: Job? = null

    fun startReceiving() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val socket = DatagramSocket(8080)
            val buffer = ByteArray(1024)

            while (isActive) {
                val packet = DatagramPacket(buffer, buffer.size)
                socket.receive(packet)
                val userPosString = String(packet.data, 0, packet.length)
                val userPos = Gson().fromJson(userPosString, LatLng::class.java)
                getUserPos(userPos)
            }
            socket.close()
        }
    }

    fun stopReceiving() {
        job?.cancel()
    }
}