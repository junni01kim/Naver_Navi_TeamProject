package com.hansung.sherpa.ui.main

import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.core.view.marginRight
import com.hansung.sherpa.MainActivity
import com.hansung.sherpa.R

class FlipperEvent{
    val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.MATCH_PARENT
    )
    val inAnimationRes = androidx.appcompat.R.anim.abc_slide_in_bottom
    val outAnimationRes = androidx.appcompat.R.anim.abc_slide_out_top

    val locationText = "도착지: 한성대입구 1번 출구"
    val destinationText = "내 위치: 서울 중구 세종대로 110 서울특별시 그다음에 뭘말해야 될까"

    fun onFlip(c: MainActivity) {
        val locationTextView = TextView(c)
        locationTextView.text = locationText
        setTextViewStyle(locationTextView)

        val destinationTextView = TextView(c)
        destinationTextView.text = destinationText
        setTextViewStyle(destinationTextView)

        val vFlipper: ViewFlipper = c.findViewById(R.id.v_flipper)
        vFlipper.addView(locationTextView)
        vFlipper.addView(destinationTextView)

        vFlipper.inAnimation = AnimationUtils.loadAnimation(c, inAnimationRes)
        vFlipper.outAnimation = AnimationUtils.loadAnimation(c, outAnimationRes)
        vFlipper.flipInterval = 4000
        vFlipper.isAutoStart = true
    }

    private fun setTextViewStyle(t: TextView) {
        t.setBackgroundColor(Color.WHITE)
        t.layoutParams = params
        t.gravity = Gravity.CENTER_VERTICAL
        t.isSingleLine = true
        t.ellipsize = TextUtils.TruncateAt.END
    }
}