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
import com.hansung.sherpa.database.CaregiverDao
import com.hansung.sherpa.database.CaregiverData
import com.hansung.sherpa.database.UserDatabase


class CaregiverFragment : PreferenceFragmentCompat() {
    private var count : Int = 0
    private lateinit var caregiverDao : CaregiverDao
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        caregiverDao = UserDatabase.getInstance().caregiverDao()
        setPreferencesFromResource(R.xml.preferences, rootKey)
        setUserServices()
        setRouteServices()
        setInitialSavedCaregiverState()
        findPreference<CaregiverPreferencesCategory>("caregiver")?.setTextClickListener(View.OnClickListener {
            run {
                showDialogToAddCaregiver()
            }
        })
    }

    private fun setInitialSavedCaregiverState(){
        val caregiverDataList : List<CaregiverData> = caregiverDao.getAll()
        if(caregiverDataList.isEmpty())
            return
        for (caregiverData : CaregiverData in caregiverDataList){
            val preference = CaregiverPreference(requireContext(), caregiverData.relation, caregiverData.telnum)
            findPreference<PreferenceCategory>("caregiver")?.addPreference(preference)
        }
    }
    /**
     * Caregiver 정보 추가를 위함 Dialog 생성
     */
    private fun showDialogToAddCaregiver() {
        var relation: String = ""
        var telnum: String = ""

        val constraint = View.inflate(
            context,
            R.layout.setting_caregiver_dialog,
            null
        ) as ConstraintLayout

        AlertDialog.Builder(requireContext())
            .setView(constraint)
            .setPositiveButton(
                "확인"
            ) { dialog, _ ->
                val relationEditText = constraint.findViewById<EditText>(R.id.relation)
                val telnumEditText = constraint.findViewById<EditText>(R.id.telnum)
                relation = relationEditText.text.toString()
                telnum = telnumEditText.text.toString()
                val newPreference: CaregiverPreference =
                    CaregiverPreference(requireContext(), relation, telnum).apply {
                        key = "caregiver${count++}"
                    }
                newPreference.setRemovePreferenceClickListener(object : CaregiverPreference.PreferenceRemoveOnClickListener{
                    override fun removePreference() {
                        findPreference<PreferenceCategory>("caregiver")?.removePreference(newPreference)
                    }
                })
                findPreference<PreferenceCategory>("caregiver")?.addPreference(newPreference)
                caregiverDao.insertCaregiverData(CaregiverData(relation, telnum))
                dialog.dismiss()
            }
            .setNegativeButton(
                "취소"
            ) { dialog, whichButton ->
                dialog.dismiss()
            }
            .show()
    }

    // 사용자 서비스
    private fun setUserServices(){
        // 음성 길 안내
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
        // 경로 이탈 안내
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
        // 위험 지역 알림
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

    // 경로 설정
    private fun setRouteServices() {
        // 버스 우서
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
        // 엘리베이터 우선
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