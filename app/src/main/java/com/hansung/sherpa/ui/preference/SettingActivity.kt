package com.hansung.sherpa.ui.preference

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hansung.sherpa.R
import com.hansung.sherpa.database.UserDatabase

class SettingActivity : AppCompatActivity() {
    private var caregiverFragment : CaregiverFragment = CaregiverFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)
        UserDatabase.createInstance(this.applicationContext)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, caregiverFragment).commit()
    }
}