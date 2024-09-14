package com.hansung.sherpa.ui.main

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hansung.sherpa.SherpaScreen
import com.hansung.sherpa.ui.preference.Divider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 메인화면 사이드 네비게이션
 *
 * @param modifier
 * @param navController 아이템들 클릭시 이동을 위한 Controller
 * @param drawerState 네비게이션 open/close 상태
 * @param scope
 * @param content
 */
@Composable
fun CustomNavigationDrawer(
    modifier: Modifier = Modifier,
    navController: NavController    = rememberNavController(),
    drawerState: DrawerState        = rememberDrawerState(initialValue = DrawerValue.Closed),
    scope: CoroutineScope           = rememberCoroutineScope(),
    content: @Composable () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    // 드로어가 열려있고 드로어 시트 바깥을 클릭했을 때 닫기
                    if (drawerState.isOpen) {
                        scope.launch { drawerState.close() }
                    }
                }
            }
    ) {
        ModalNavigationDrawer(
            modifier        = modifier,
            drawerState     = drawerState,
            gesturesEnabled = false,
            drawerContent   = {
                ModalDrawerSheet(
                    modifier = Modifier.fillMaxWidth(0.6F)
                ) {
                    Text("메뉴 이동하기", modifier = Modifier.padding(16.dp))
                    Divider("")
                    NavigationDrawerItem(
                        label       = { Text(text = "캘린더") },
                        selected    = false,
                        icon        = { Icon(Icons.Filled.CalendarMonth, contentDescription = "캘린더 아이콘") },
                        onClick     = { navController.navigate(SherpaScreen.CALENDAR.name) }
                    )
                    NavigationDrawerItem(
                        label       = { Text(text = "설정") },
                        selected    = false,
                        icon        = { Icon(Icons.Filled.Settings, contentDescription = "설정 아이콘") },
                        onClick     = { navController.navigate(SherpaScreen.Preference.name) }
                    )
                }
            }
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun PreviewCustomNavigationDrawer() {
    CustomNavigationDrawer()
}