package com.hansung.sherpa.user.`class`

/**
 * ※ MariaDB에서는 enum과 timeStamp를 클래스로 반환받지 못한다. (String 처리)
 *
 */
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