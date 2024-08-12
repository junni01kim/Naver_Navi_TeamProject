package com.hansung.sherpa.user

import com.google.gson.annotations.SerializedName
import java.security.Timestamp

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
) {
    fun getMap():Map<String, String> = mapOf(
            "email" to email,
            "password" to password,
            "name" to name,
            "telNum" to telNum,
            "address" to address,
            "detailAddress" to detailAddress,
            "fcmToken" to fcmToken,
            "caregiverId" to caregiverId.toString(),
            "caregiverRelation" to caregiverRelation,
            "createdAt" to createdAt.toString(),
            "updatedAt" to updatedAt.toString()
        )
}
