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
import com.hansung.sherpa.database.CaregiverDao
import com.hansung.sherpa.database.UserDatabase

class CaregiverPreference : Preference{
    constructor(context: Context, attributeSet: AttributeSet? = null) : super(context,attributeSet)
    constructor(context : Context, relation : String, telnum : String) : this(context, null){
        this.relation = relation
        this.telnum = telnum
        isIconSpaceReserved = false
    }

    private var relation : String = ""
    private var telnum: String = ""
    interface PreferenceRemoveOnClickListener {
        fun removePreference();
    }
    private lateinit var preferenceRemoveOnClickListener : PreferenceRemoveOnClickListener

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
            text = relation
            setTextColor(Color.BLACK)
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val textView2 = TextView(context).apply {
            id = View.generateViewId()
            text = telnum
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
                R.layout.setting_caregiver_dialog,
                null
            ) as ConstraintLayout

            constraint.findViewById<TextView>(R.id.caregiver_dialog_title).apply {
                setText("정보 수정")
            }

            AlertDialog.Builder(context)
                .setView(constraint)
                .setPositiveButton(
                    "확인"
                ) { dialog, _ ->
                    val caregiverDao = UserDatabase.getInstance().caregiverDao()
                    val id : Long = caregiverDao.getCaregiverID(relation, telnum)
                    constraint.findViewById<EditText>(R.id.relation).apply {
                        relation = text.toString()
                    }
                    constraint.findViewById<EditText>(R.id.telnum).apply {
                        telnum = text.toString()
                    }
                    notifyChanged()
                    caregiverDao.updateCaregiver(id,relation,telnum)
                    dialog.dismiss()
                }
                .setNegativeButton(
                    "취소"
                ) { dialog, whichButton ->
                    dialog.dismiss()
                }
                .show()
        }
        constraintLayout.setOnLongClickListener {
            AlertDialog.Builder(context)
                .setMessage("삭제하시겠습니까?")
                .setPositiveButton("삭제") { dialog, _ ->
                    val caregiverDao: CaregiverDao = UserDatabase.getInstance().caregiverDao()
                    val id: Long = caregiverDao.getCaregiverID(relation, telnum)
                    caregiverDao.deleteCaregiver(id)
                    dialog.dismiss()
                    preferenceRemoveOnClickListener.removePreference()
                    (holder.itemView as ViewGroup).removeView(constraintLayout)
                }
                .setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }.show()
            true
        }
        (holder.itemView as ViewGroup).removeAllViews()
        (holder.itemView as ViewGroup).addView(constraintLayout)
    }
    fun setRemovePreferenceClickListener(preferenceRemoveOnClickListener: PreferenceRemoveOnClickListener) {
        this.preferenceRemoveOnClickListener = preferenceRemoveOnClickListener
    }
}