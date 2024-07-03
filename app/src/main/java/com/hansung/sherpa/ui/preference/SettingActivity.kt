package com.hansung.sherpa.ui.preference

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hansung.sherpa.R

class SettingActivity : AppCompatActivity() {
    private var count : Int = 0
    private var preferenceFragment : PreferenceFragment = PreferenceFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, preferenceFragment).commit()
    }
}