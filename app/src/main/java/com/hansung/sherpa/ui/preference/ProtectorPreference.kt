package com.hansung.sherpa.ui.preference

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.hansung.sherpa.R

class ProtectorPreference : Preference{
    constructor(context: Context, attributeSet: AttributeSet? = null) : super(context,attributeSet)
    constructor(context : Context, name : String, phone : String) : this(context, null){
        this.name = name
        this.phone = phone
        isIconSpaceReserved = false
    }

    private var name : String = ""
    private var phone: String = ""

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val context = holder.itemView.context

        val constraintLayout = ConstraintLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val textView1 = TextView(context).apply {
            id = View.generateViewId()
            text = name
            setTextColor(Color.BLACK)
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val textView2 = TextView(context).apply {
            id = View.generateViewId()
            text = phone
            setTextColor(Color.BLACK)
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
        }

        constraintLayout.addView(textView1)
        constraintLayout.addView(textView2)

        val set = ConstraintSet().apply {
            clone(constraintLayout)
            connect(textView1.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 16)
            connect(textView1.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 16)
            connect(textView2.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 16)
            connect(textView2.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 16)
            applyTo(constraintLayout)
        }
        constraintLayout.setOnClickListener{
            val constraint = View.inflate(
                context,
                R.layout.setting_protector_dialog,
                null
            ) as ConstraintLayout

            constraint.findViewById<TextView>(R.id.protector_dialog_title).apply {
                setText("정보 수정")
            }

            AlertDialog.Builder(context)
                .setView(constraint)
                .setPositiveButton(
                    "확인"
                ) { dialog, _ ->
                    constraint.findViewById<EditText>(R.id.name).apply {
                        name = text.toString()
                    }
                    constraint.findViewById<EditText>(R.id.phone).apply {
                        phone = text.toString()
                    }
                    notifyChanged()
                    dialog.dismiss()
                }
                .setNegativeButton(
                    "취소"
                ) { dialog, whichButton ->
                    dialog.dismiss()
                }
                .show()
        }
        (holder.itemView as ViewGroup).removeAllViews()
        (holder.itemView as ViewGroup).addView(constraintLayout)
    }
}