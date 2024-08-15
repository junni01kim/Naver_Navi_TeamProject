package com.hansung.sherpa.ui.preference.emergency

// TODO: 해당 클래스들 디벨롭에 머지 후 삭제하기
data class Emergency (
    val emergencyId: Int,
    val userId: Int,
    val deleteYn: String,
    val name: String,
    val telNum: String,
    val address: String
)

data class User1 (
    val userId:Int? = null,
    val name:String? = null,
    val telNum:String? = null,
    val role1:String? = null,
    val userSetting: UserSetting? = null,
    val userAccount: UserAccount? = null,
)

data class UserSetting (
    val userSettingId: Int? = null,
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
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val address: String? = null,
    val detailAddress: String? = null
)

class UserRelation (
    val userRelationId:Int? = null,
    val caretakerId:Int? = null,
    val caregiverId:Int? = null,
    val relation: String? = null
)
