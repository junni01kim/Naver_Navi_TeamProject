package com.hansung.sherpa.user.login

import java.sql.Timestamp


data class LoginResponse (
    val userId:String? = null,
    val name:String? = null,
    val telNum:String? = null,
    val role:Role1? = null,
    val userSetting:UserSetting? = null,
    val userAccount:UserAccount? = null,
)

enum class Role1 {
    ADMIN,
    CAREGIVER,
    CARETAKER
}

data class UserSetting (
    val userSettingId: String? = null,
    val voiceGuide: Boolean? = null,
    val alertRoute: Boolean? = null,
    val alertDanger: Boolean? = null,
    val lowBus: Boolean? = null,
    val elevatorFirst: Boolean? = null
)

data class UserAccount (
    val userAccountId:Int? = null,
    val email: String? = null,
    val password: String? = null,
    val passwordSalt: String? = null,
    val hashAlgorithmId: String? = null,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
    val address: String? = null,
    val detailAddress: String? = null
)