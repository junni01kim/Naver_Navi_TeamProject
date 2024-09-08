package com.hansung.sherpa.sendInfo.receive

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.gson.Gson
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.sendInfo.CaregiverViewModel
import com.hansung.sherpa.sendInfo.PartnerViewModel
import com.hansung.sherpa.user.table.Role1
import com.naver.maps.geometry.LatLng

/**
 * 이용자가 보호자인지 판단하는 함수
 */
fun isCareGiver() = StaticValue.userInfo.role1 == Role1.CAREGIVER.toString()

/**
 * Firebase의 Realtime Database의 값이 변경되는 이벤트를 감지하는 리스너
 * @param caregiverId 필터링할 ID
 * @param partnerViewModel 전체 이용자 업데이트 객체
 * @param caregiverViewModel 보호자 업데이트 객체
 */
fun onChangedRTDBListener(
    caregiverId: String?,
    partnerViewModel: PartnerViewModel,
    caregiverViewModel: CaregiverViewModel
) {
    // [Validation Check]:  caregiverId null 값이면, 종료
    if (caregiverId == null) return

    // [init]: Static Class에 저장된 DatabaseReference 불러오기
    // [reference]: MainActivity에서 database로 초기화
    val db = StaticValue.ref

    /**
     * FB_RTDB 로직 설명
     * - db                             : Firebase Realtime Database
     * - .child (Table Name)            : 특정 테이블을 선택한다.
     * - .child (Unique Key)            : 고유 ID로 특정 Row 데이터를 선택한다.
     * - .addValueEventListener()       : 해당 Row 데이터의 값 변경에 대한 이벤트 리스너를 추가한다.
     */

    // 현재 위치 : current_position
    // 저장되는 데이터 샘플 : {"latitude":37.582763,"longitude":127.0106868}
    db
        .child("current_position")
        .child(caregiverId)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<String>()
                Log.d("RTDB_CP", "current_position Value is: $value")
                if(value == null) return
                // 변경시 수행되는 로직 * getLatLng() 내부에서 역직렬화 수행 *
                partnerViewModel.getLatLng(value)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("RTDB_CP", "Failed to read value.", error.toException())
            }
        })

    // 경로 안내 : moving_position
    // 저장되는 데이터 샘플 : {"latitude":37.5820805,"longitude":127.007308}
    db
        .child("moving_position")
        .child(caregiverId)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<String>()
                Log.d("RTDB_MP", "moving_position Value is: $value")
                if(value == null) return
                // 변경시 수행되는 로직 *역직렬화 후 전달*
                partnerViewModel.updateLatLng(Gson().fromJson(value, LatLng::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("RTDB_MP", "Failed to read value.", error.toException())
            }
        })

    // 지나온 정도(0~1) : passed_route
    // 저장되는 데이터 샘플 : [0.03329178393931409, 0, 0]
    db
        .child("passed_route")
        .child(caregiverId)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value == null) return

                val snapShotList = dataSnapshot.children.mapNotNull {
                    it.getValue(Double::class.java) // 각 자식을 Double로 변환
                }
                Log.d("RTDB_PR", "passed_route Value is: $snapShotList")

                // List -> SnapshotStateList
                val snapshotStateList: SnapshotStateList<Double> = mutableStateListOf()
                snapshotStateList.addAll(snapShotList)

                // 변경시 수행되는 로직
                caregiverViewModel.updatePassedRoute(snapshotStateList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("RTDB_PR", "Failed to read value.", error.toException())
            }
        })
}