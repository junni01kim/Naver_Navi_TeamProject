package com.hansung.sherpa.sherpares

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CheckboxColors
import androidx.compose.ui.graphics.Color

val SherpaColor = Color(0xFF64FCD9)
val LightSherpaColor = Color(0xFFB2F9E1)
val DarkSherpaColor = Color(0xFF4ED1B3)

val SherpaCheckBoxColor = CheckboxColors(
    // Mark Color
    checkedCheckmarkColor = Color.White,
    uncheckedCheckmarkColor = Color.White,

    //Border Color
    checkedBorderColor = DarkSherpaColor,
    uncheckedBorderColor = SherpaColor,

    disabledBorderColor = Color.DarkGray,
    disabledUncheckedBorderColor = Color.DarkGray,
    disabledIndeterminateBorderColor = Color.DarkGray,

    //Box Color
    checkedBoxColor = SherpaColor,
    uncheckedBoxColor = Color.White,

    disabledUncheckedBoxColor = Color.White,
    disabledCheckedBoxColor = Color.Gray,
    disabledIndeterminateBoxColor = Color.Gray,
)

val SherpaButtonColor = ButtonColors(
    contentColor = Color.Black,
    containerColor = SherpaColor,
    disabledContentColor = Color.Black,
    disabledContainerColor =  SherpaColor
)
