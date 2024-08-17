package com.hansung.sherpa.user.updateFcm

import com.hansung.sherpa.StaticValue

data class UpdateFcmRequest(val userId : Int = StaticValue.userInfo.userId!!, val fcmToken: String = StaticValue.FcmToken)
