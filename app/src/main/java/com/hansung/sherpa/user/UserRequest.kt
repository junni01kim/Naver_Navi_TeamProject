package com.hansung.sherpa.user

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

/**
 * - UserRequest -
 * compose.user 패키지에서 다루는 request 클래스를 모아둔 파일
 *
 */

/**
 * user 정보를 생성하는 클래스
 *
 */
class CreateUserRequest (
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String,
    @SerializedName("telNum") val telNum: String,
    @SerializedName("address") val address: String,
    @SerializedName("detailAddress") val detailAddress: String = "지정안함",
    @SerializedName("fcmToken") val fcmToken: String,
    @SerializedName("caregiverId") val caregiverId: Int = -1,
    @SerializedName("caregiverRelation") val caregiverRelation: String = "지정안함",
    @SerializedName("createAt") val createdAt: Timestamp,
    @SerializedName("updateAt") val updatedAt: Timestamp,
)

/**
 * user로 접속하기 위한 클래스
 *
 */
class LoginRequest (
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)