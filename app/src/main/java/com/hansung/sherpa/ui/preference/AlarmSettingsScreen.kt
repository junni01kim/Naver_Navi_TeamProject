package com.hansung.sherpa.ui.preference

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.jakewharton.threetenabp.AndroidThreeTen

class AlarmSettingsActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this);
        setContent {
            TopAppBarScreen( title = "사용자 설정",
                { finish() }, { AlarmSettingsScreen() }
            )
        }
    }
}

@Composable
fun AlarmSettingsScreen() {
        val detailedRouteVoiceChecked       = remember { mutableStateOf(false) } // 경로 상세 안내 토글
        val routeDeviationVoiceChecked      = remember { mutableStateOf(false) } // 경로 이탈 알림 토글
        val dangerZoneVoiceChecked          = remember { mutableStateOf(false) } // 위험 지역 알림 토글
        val dangerZoneVoiceExpanded         = remember { mutableStateOf(false) } // 위험 지역 알림 dropdown
        val crosswalkVoiceChecked           = remember { mutableStateOf(false) } // 횡단보도 알림 토글
        val constructionZoneVoiceChecked    = remember { mutableStateOf(false) } // 공사현장 알림 토글
        val accidentProneAreaVoiceChecked   = remember { mutableStateOf(false) } // 사고다발 지역 알림 토글
        val lowFloorBurPriorityChecked      = remember { mutableStateOf(false) } // 저상버스 우선 안내 토글
        val elevatorPriorityChecked         = remember { mutableStateOf(false) } // 엘레베이터 우선 안내 토글
        Column {
            Divider(text = "음성 길안내")
            DoubleLineListItem("경로 상세 안내", "세부적인 경로 안내를 진행합니다.", true, detailedRouteVoiceChecked)
            DoubleLineListItem("경로 이탈 알림", "경로 이탈 시 재탐색과 함께 경고음이 발생합니다.", true, routeDeviationVoiceChecked)
            AnimationListItem(
                "위험 지역 알림", "주의를 권하는 문구를 전달합니다.",
                dangerZoneVoiceExpanded, dangerZoneVoiceChecked
            ) {
                if(!dangerZoneVoiceChecked.value) { // 상위 메뉴가 false면, 하위 메뉴 전부 false
                    crosswalkVoiceChecked.value = false
                    constructionZoneVoiceChecked.value = false
                    accidentProneAreaVoiceChecked.value = false
                }
                InlineListItem("횡단보도", dangerZoneVoiceChecked.value, crosswalkVoiceChecked,  isChild = true)
                InlineListItem("공사현장", dangerZoneVoiceChecked.value, constructionZoneVoiceChecked,  isChild = true)
                InlineListItem("사고다발 지역", dangerZoneVoiceChecked.value, accidentProneAreaVoiceChecked,  isChild = true)
            }
            Divider(text = "본인이용 서비스(이용자 이용 가능)")
            InlineListItem("엘레베이터 우선 안내", true, lowFloorBurPriorityChecked)
            InlineListItem("저상버스 우선 안내" , true, elevatorPriorityChecked)
        }
}

@Preview
@Composable
fun PreviewAlarmSettingsScreen2() {
    AlarmSettingsScreen()
}


