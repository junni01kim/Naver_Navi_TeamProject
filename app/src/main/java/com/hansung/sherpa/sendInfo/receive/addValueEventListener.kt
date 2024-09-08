package com.hansung.sherpa.sendInfo.receive

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.sendInfo.CaregiverViewModel
import com.hansung.sherpa.sendInfo.PartnerViewModel
import com.hansung.sherpa.user.table.Role1
import com.hansung.sherpa.user.table.User1
import com.naver.maps.geometry.LatLng

/**
 * 이용자가 보호자인지 판단하는 함수
 */
fun isCareGiver() = StaticValue.userInfo.role1 == Role1.CAREGIVER.toString()

fun addValueEventListener(
    caregiverId: String?,
    partnerViewModel: PartnerViewModel,
    caregiverViewModel: CaregiverViewModel
) {
    if (caregiverId == null) return
    val db = StaticValue.ref
    // 현재 위치
    db
        .child("current_position")
        .child(caregiverId)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<String>()
                Log.d("RTDB_CP", "current_position Value is: $value")
                if(value == null) return
                partnerViewModel.getLatLng("", value)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("RTDB_CP", "Failed to read value.", error.toException())
            }
        })

    // 경로 안내
    db
        .child("moving_position")
        .child(caregiverId)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<String>()
                Log.d("RTDB_MP", "moving_position Value is: $value")
                if(value == null) return
                partnerViewModel.updateLatLng(Gson().fromJson(value, LatLng::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("RTDB_MP", "Failed to read value.", error.toException())
            }
        })

    // 지나온 정도
    db
        .child("passed_route")
        .child(caregiverId)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value == null) return

                val doubleList = dataSnapshot.children.mapNotNull {
                    it.getValue(Double::class.java) // 각 자식을 Double로 변환
                }
                Log.d("RTDB_PR", "passed_route Value is: $doubleList")

                val snapshotStateList: SnapshotStateList<Double> = mutableStateListOf()
                snapshotStateList.addAll(doubleList)

                caregiverViewModel.updatePassedRoute(snapshotStateList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("RTDB_PR", "Failed to read value.", error.toException())
            }
        })
}