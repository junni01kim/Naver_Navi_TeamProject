package com.hansung.sherpa.ui.preference

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.hansung.sherpa.R


class PreferenceFragment : PreferenceFragmentCompat() {
    private var count : Int = 0
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        setUserServcies()
        setRouteServices()
        findPreference<ProtectorPreferencesCategory>("protector")?.setTextClickListener(View.OnClickListener { view ->
            run {
                var name: String = ""
                var phone: String = ""

                val constraint = View.inflate(
                    context,
                    R.layout.setting_protector_dialog,
                    null
                ) as ConstraintLayout

                AlertDialog.Builder(requireContext())
                    .setView(constraint)
                    .setPositiveButton(
                        "확인"
                ) { dialog, _ ->
                   val nameEditText = constraint.findViewById<EditText>(R.id.name)
                    val phoneEditText = constraint.findViewById<EditText>(R.id.phone)
                    name = nameEditText.text.toString()
                    phone = phoneEditText.text.toString()
                    val newPreference: ProtectorPreference = ProtectorPreference(requireContext(), name, phone).apply {
                        key = "protector_${count++}"
                    }
                    findPreference<PreferenceCategory>("protector")?.addPreference(newPreference)
                    dialog.dismiss()
                }
                .setNegativeButton(
                    "취소"
                ) { dialog, whichButton ->
                    dialog.dismiss()
                }
                .show()
            }
        })
    }

    private fun setUserServcies(){
        findPreference<SwitchPreference>("service_voice")?.apply {
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                when (newValue as Boolean){
                    true -> {
                        Toast.makeText(requireContext(), "음성 안내가 설정되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    false -> {
                        Toast.makeText(requireContext(), "음성 안내가 해제되었습니다..", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }
        findPreference<SwitchPreference>("service_alert_route")?.apply {
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                when (newValue as Boolean){
                    true -> {
                        Toast.makeText(requireContext(), "경로 이탈 알림이 설정되었습니다.", Toast.LENGTH_SHORT).show()
                    }

                    false -> {
                        Toast.makeText(requireContext(), "경로 이탈 알림이 해제되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }
        findPreference<SwitchPreference>("service_alert_danger")?.apply {
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                when (newValue as Boolean){
                    true -> {
                        Toast.makeText(requireContext(), "위험 지역 알림이 설정되었습니다.", Toast.LENGTH_SHORT).show()
                    }

                    false -> {
                        Toast.makeText(requireContext(), "위험 지역 알림이 해제되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }
    }


    private fun setRouteServices() {
        findPreference<SwitchPreference>("service_prefer_bus")?.apply {
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                when (newValue as Boolean){
                    true -> {
                        Toast.makeText(requireContext(), "저상 버스 우선 안내가 설정되었습니다.", Toast.LENGTH_SHORT).show()
                    }

                    false -> {
                        Toast.makeText(requireContext(), "저상 버스 우선 안내가 해제되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }

        findPreference<SwitchPreference>("service_prefer_elevator")?.apply {
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                when (newValue as Boolean){
                    true -> {
                        Toast.makeText(requireContext(), "엘레베이터 우선 안내가 설정되었습니다.", Toast.LENGTH_SHORT).show()
                    }

                    false -> {
                        Toast.makeText(requireContext(), "엘레베이터 우선 안내가 해제되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }
    }
}