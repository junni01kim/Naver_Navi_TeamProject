package com.hansung.sherpa.ui.preference

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceViewHolder
import com.hansung.sherpa.R

class CaregiverPreferencesCategory : PreferenceCategory{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet): super(context,attrs)

    private var textClickListener: View.OnClickListener? = null
    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        val addTextView = holder.findViewById(R.id.add_caregiver) as TextView
        addTextView.setOnClickListener(textClickListener)
    }
    fun setTextClickListener(onClickListener: View.OnClickListener){
        this.textClickListener = onClickListener
    }
}