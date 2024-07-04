package com.hansung.sherpa.ui.main

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

/**
 * 메인 화면 Icon Event 처리 (임시)
 */
class FloatIconEvent() {
    /**
     *  Icon 클릭 시 내용 확장/축소
     */
    fun setOnClick(icon: ExtendedFloatingActionButton) {
        icon.shrink()
        var isExtend = false
        icon.setOnClickListener {
            if (isExtend) {
                icon.shrink()
            } else {
                icon.extend()
            }
            isExtend = !isExtend
        }
    }
}