package com.hansung.sherpa.routelist

import android.animation.Animator
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * expand item을 적용하기 위한 코드
 * https://dvlpseo.tistory.com/56 참고
 */
class ToggleAnimation {
    companion object{
        // 애니메이션 적용 함수
        fun toggleArrow(view: View, isExpanded: Boolean): Boolean{
            if(isExpanded){
                view.animate().setDuration(200).rotation(180f)
                return true
            }
            else{
                view.animate() .setDuration(200).rotation(0f)
                return false
            }
        }

        // expand_item 확장 코드
        fun expand(view:View){
            val animation = expandAction(view)
            view.startAnimation(animation)
        }

        private fun expandAction(view: View): Animation {
            view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val actualHeight = view.measuredHeight

            view.layoutParams.height = 0
            view.visibility = View.VISIBLE

            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    view.layoutParams.height =
                        if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT
                        else (actualHeight * interpolatedTime).toInt()
                    view.requestLayout()
                }
            }
            animation.duration = (actualHeight/view.context.resources.displayMetrics.density).toLong()
            view.startAnimation(animation)

            view.startAnimation(animation)

            return animation
        }

        // expand_item 축소 코드
        fun collapse(view: View){
            val actualHeight = view.measuredHeight

            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    if(interpolatedTime == 1f){
                        view.visibility = View.GONE
                    }
                    else {
                        view.layoutParams.height = (actualHeight - (actualHeight * interpolatedTime)).toInt()
                        view.requestLayout()
                    }
                }
            }
            animation.duration = (actualHeight/view.context.resources.displayMetrics.density).toLong()
            view.startAnimation(animation)
        }
    }
}