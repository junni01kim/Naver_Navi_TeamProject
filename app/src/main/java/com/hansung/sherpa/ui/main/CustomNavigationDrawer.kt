package com.hansung.sherpa.ui.main

import android.annotation.SuppressLint
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CustomNavigationDrawer(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    scope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit = {},
) {

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.6F)
            ) {
                Text("메뉴 이동하기", modifier = Modifier.padding(16.dp))
                Divider("")
                NavigationDrawerItem(
                    label = { Text(text = "캘린더") },
                    selected = false,
                    icon = { Icon(Icons.Filled.CalendarMonth, contentDescription = "캘린더 아이콘") },
                    onClick = { navController.navigate(SherpaScreen.CALENDAR.name) }
                )
                NavigationDrawerItem(
                    label = { Text(text = "설정") },
                    selected = false,
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "설정 아이콘") },
                    onClick = { navController.navigate(SherpaScreen.Preference.name) }
                )
            }
        }
    ) {
        content()
    }

}

@Preview
@Composable
fun PreviewCustomNavigationDrawer() {
    CustomNavigationDrawer()
}