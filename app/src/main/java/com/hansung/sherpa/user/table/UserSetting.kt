package com.hansung.sherpa.user.table

data class UserSetting (
    val userSettingId: Int? = null,
    val voiceGuide: Boolean? = null,
    val alertRoute: Boolean? = null,
    val alertDanger: Boolean? = null,
    val lowBus: Boolean? = null,
    val elevatorFirst: Boolean? = null
)