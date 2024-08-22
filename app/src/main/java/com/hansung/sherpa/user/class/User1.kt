package com.hansung.sherpa.user.`class`

/**
 * ※ MariaDB에서는 enum과 timeStamp를 클래스로 반환받지 못한다. (String 처리)
 *
 */
data class User1 (
    val userId:Int? = null,
    val name:String? = null,
    val telNum:String? = null,
    val role1:String? = null,
    val userSetting: UserSetting? = null,
    val userAccount: UserAccount? = null,
)