package com.hansung.sherpa.emergency

import com.google.gson.annotations.SerializedName

data class Emergency (
    @SerializedName("emergencyId") val emergencyId: Int = -1,
    @SerializedName("userId") val userId: Int,
    @SerializedName("deleteYn") val deleteYn: String = "n",
    @SerializedName("name") val name: String = "",
    @SerializedName("telNum") val telNum: String = "",
    @SerializedName("address") val address: String = ""
)