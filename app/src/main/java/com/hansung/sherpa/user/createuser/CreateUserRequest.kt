package com.hansung.sherpa.user.createuser

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

class CreateUserRequest (
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String,
    @SerializedName("telNum") val telNum: String,
    @SerializedName("address") val address: String,
    @SerializedName("detailAddress") val detailAddress: String = "지정안함",
    @SerializedName("fcmToken") val fcmToken: String,
    @SerializedName("caregiverId") val caregiverId: Int = 0,
    @SerializedName("caregiverRelation") val caregiverRelation: String = "지정안함",
    @SerializedName("createAt") val createdAt: Timestamp,
    @SerializedName("updateAt") val updatedAt: Timestamp,
)
