package com.hansung.sherpa.ui.preference.calendar

import java.util.Locale

data class CalendarConfig (
    val yearRange: IntRange = IntRange(1970, 2100),
    val locale: Locale = Locale.KOREAN
)