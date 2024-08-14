package com.hansung.sherpa.ui.preference

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType.*
import androidx.room.util.TableInfo.*


/**
 * nnc_backend를 기반으로 만든 긴급 연락처 클래스
 *
 * @param emergency_id
 * @param userId
 * @param deleteYn
 * @param name
 * @param telNum
 * @param address
 */
data class Emergency (
    val emergencyId: Int,
    val userId: Int,
    val deleteYn: String,
    val name: String,
    val telNum: String,
    val address: String
)

class User1 {
    val userId = 0
    val name: String? = null
    val telNum: String? = null
    val role1: Role1? = null
    val userSetting: UserSetting? = null
    val userAccount: UserAccount? = null
}

@Composable
fun EmergencySettingsScreen() {
    // 최상단 보호자 (이름/주소/연락처)


    // 이후로 긴급 연락처

    // 긴급연락처 추가하기
    
    // 버튼 누를 시 전화하기 + 이동하기
}

@Preview
@Composable
fun EmergencySettingsPreview() {
    EmergencySettingsScreen()
}