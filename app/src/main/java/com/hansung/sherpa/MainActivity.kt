package com.hansung.sherpa

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.hansung.sherpa.fcm.MessageViewModel
import com.hansung.sherpa.fcm.PermissionDialog
import com.hansung.sherpa.fcm.RationaleDialog
import com.hansung.sherpa.ui.account.login.LoginScreen
import com.hansung.sherpa.ui.account.signup.SignupScreen
import com.hansung.sherpa.ui.preference.AlarmSettingsActivity
import com.hansung.sherpa.ui.preference.PreferenceScreen
import com.hansung.sherpa.ui.preference.PreferenceScreenOption
import com.hansung.sherpa.ui.preference.calendar.CalendarActivity
import com.hansung.sherpa.ui.preference.caregiver.CaregiverSyncActivity
import com.hansung.sherpa.ui.preference.emergency.EmergencySettingsActivity
import com.hansung.sherpa.ui.preference.policyinformation.PolicyInfoActivity
import com.hansung.sherpa.ui.preference.updateinformation.UpdateInfoActivity
import com.hansung.sherpa.ui.preference.usersetting.UserSettingActivity
import com.hansung.sherpa.ui.searchscreen.SearchScreen
import com.hansung.sherpa.ui.specificroute.SpecificRouteScreen
import com.hansung.sherpa.ui.start.StartScreen
import com.hansung.sherpa.ui.theme.SherpaTheme
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.util.FusedLocationSource

class MainActivity : ComponentActivity() {

    private lateinit var naverMap: NaverMap

    private lateinit var locationSource: FusedLocationSource

    // TODO: 여기 있는게 "알림" topic으로 FCM 전달 받는 뷰모델 ※ FCM pakage 참고
    private val viewModel: MessageViewModel by viewModels()

    private val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val title = intent?.getStringExtra("title") ?: ""
            val body = intent?.getStringExtra("body") ?: ""
            viewModel.onMessageReceived(title, body)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // FCM SDK 초기화
        FirebaseApp.initializeApp(this);

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    Log.w("FCMLog", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.d("FCMLog", "Current token: $token")

                StaticValue.FcmToken = token
            }

        // BroadcastReceiver 등록
        registerReceiver(messageReceiver, IntentFilter("FCM_MESSAGE"), RECEIVER_NOT_EXPORTED)

        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.CLIENT_ID)



        setContent {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                RequestNotificationPermissionDialog()
            }
            SherpaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // TODO: 임시로 설정해둠

                    // 화면 간 이동에 대한 함수
                    // https://developer.android.com/codelabs/basic-android-kotlin-compose-navigation?hl=ko#0
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = SherpaScreen.Start.name
                    ){
                        composable(route = SherpaScreen.Start.name){
                            StartScreen(navController, Modifier.padding(innerPadding))
                        }
                        composable(route = SherpaScreen.Login.name){
                            LoginScreen(navController, Modifier.padding(innerPadding))
                        }
                        composable(route = SherpaScreen.SignUp.name) {
                            SignupScreen(navController, Modifier.padding(innerPadding))
                        }
                        composable(route = SherpaScreen.Home.name){
                            ExampleAlam(viewModel)
                            HomeScreen(navController, Modifier.padding(innerPadding))
                        }
                        composable(route = "${SherpaScreen.Search.name}/{destinationValue}",
                            arguments = listOf(navArgument("destinationValue"){type = NavType.StringType})
                        ){
                            val destinationValue = it.arguments?.getString("destinationValue")!!
                            SearchScreen(navController, destinationValue, Modifier.padding(innerPadding))
                        }
                        composable(route = SherpaScreen.SpecificRoute.name){
                            SpecificRouteScreen(navController, StaticValue.transportRoute)
                        }
                        composable(route = SherpaScreen.Preference.name){
                            PreferenceScreen { screenName ->
                                when(screenName){
                                    PreferenceScreenOption.CALENDAR -> {
                                        val intent = Intent(this@MainActivity, CalendarActivity::class.java)
                                        startActivity(intent)
                                    }
                                    PreferenceScreenOption.USER -> {
                                        val intent = Intent(this@MainActivity, UserSettingActivity::class.java)
                                        startActivity(intent)
                                    }
                                    PreferenceScreenOption.EMERGENCY -> {
                                        val intent = Intent(this@MainActivity, EmergencySettingsActivity::class.java)
                                        startActivity(intent)
                                    }
                                    PreferenceScreenOption.CAREGIVER -> {
                                        val intent = Intent(this@MainActivity, CaregiverSyncActivity::class.java)
                                        startActivity(intent)
                                    }
                                    PreferenceScreenOption.NOTIFICATION -> {
                                        val intent = Intent(this@MainActivity, AlarmSettingsActivity::class.java)
                                        startActivity(intent)
                                    }
                                    PreferenceScreenOption.APP_INFORMATION -> {
                                        val intent = Intent(this@MainActivity, UpdateInfoActivity::class.java)
                                        startActivity(intent)
                                    }
                                    PreferenceScreenOption.PRIVACY_POLICY -> {
                                        val intent = Intent(this@MainActivity, PolicyInfoActivity::class.java)
                                        startActivity(intent)
                                    }
                                    else -> { } // TODO: 처리
                                }
                            }
                        }
                        composable(route = SherpaScreen.CALENDAR.name){
                            val intent = Intent(this@MainActivity, CalendarActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun RequestNotificationPermissionDialog() {
        val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

        if (!permissionState.status.isGranted) {
            if (permissionState.status.shouldShowRationale) RationaleDialog()
            else PermissionDialog { permissionState.launchPermissionRequest() }
        }
    }

    // 권한 확인
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(messageReceiver)
    }
}