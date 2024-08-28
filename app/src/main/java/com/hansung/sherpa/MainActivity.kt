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
import androidx.navigation.NavHostController
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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hansung.sherpa.fcm.MessageViewModel
import com.hansung.sherpa.fcm.PermissionDialog
import com.hansung.sherpa.fcm.RationaleDialog
import com.hansung.sherpa.fcm.ScheduleViewModel
import com.hansung.sherpa.itemsetting.SubPath
import com.hansung.sherpa.itemsetting.SubPathAdapter
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.sendInfo.CaregiverViewModel
import com.hansung.sherpa.sendInfo.CaretakerViewModel
import com.hansung.sherpa.sendInfo.PartnerViewModel
import com.hansung.sherpa.sendInfo.receive.ReceivePos
import com.hansung.sherpa.ui.account.login.LoginScreen
import com.hansung.sherpa.ui.account.signup.SignupScreen
import com.hansung.sherpa.ui.common.MessageAlam
import com.hansung.sherpa.ui.common.ScheduleAlam
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

    lateinit var navController: NavHostController

    // TODO: 여기 있는게 "알림" topic으로 FCM 전달 받는 뷰모델 ※ FCM pakage 참고
    private val messageViewModel: MessageViewModel by viewModels()
    private val scheduleViewModel: ScheduleViewModel by viewModels()
    private val partnerViewModel: PartnerViewModel by viewModels()
    private val caretakerViewModel: CaretakerViewModel by viewModels()
    private val caregiverViewModel: CaregiverViewModel by viewModels()

    private val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val head = intent?.getStringExtra("title") ?: ""
            val body = intent?.getStringExtra("body") ?: ""

            //Log.i("FCM Log: Success", "branch 메서드: 수신 완료")
            //Log.i("FCM Log: Data", "$head, $body")

            val parts = head.split("/")
            val topic = parts[0]
            val title = parts[1]

            when (topic) {
                "알림" -> messageViewModel.updateValue(title, body)
                "일정" -> scheduleViewModel.updateSchedule(title, body)
                //"예약경로" -> caregiverViewModel.receivePos(title, body)
                "위치" -> partnerViewModel.getLatLng(title, body)
                "경로이동" -> {
                    val response = Gson().fromJson(body, ReceivePos::class.java)
                    partnerViewModel.updateLatLng(response.pos)
                    caregiverViewModel.updatePassedRoute(response.passedRoute)
                }
                "재탐색" -> {
                    Log.d("FCM LOG", "재탐색")
                    caregiverViewModel.devateRoute(title, body)
                }
                "시작" -> {
                    Log.d("FCM LOG", "시작 전송")
                    caregiverViewModel.startNavigation(title, body)
                    navController.navigate(SherpaScreen.SpecificRoute.name)
                }

                else -> Log.e("FCM Log: Error", "FCM: message 형식 오류")
            }
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


        // TODO TransportRoute 역직렬화 flow 가져간 후 지울 것! -----
        val responseJson = "\"7b22696e666f223a7b22746f74616c44697374616e6365223a313333352e302c22746f74616c54696d65223a31322c22746f74616c57616c6b223a3135352c227472616e73666572436f756e74223a317d2c2273756250617468223a5b7b2273656374696f6e496e666f223a7b22636f6e74656e7473223a5b22ecb69cebb09ceca780ec9790ec849c3138ebafb8ed84b0ec9db4eb8f99ed95a9eb8b88eb8ba42e222c22ed9884ec9eacec9c84ecb998ec9790ec849ceca28cecb8a1ec9cbceba19c3839ebafb8ed84b0ec9db4eb8f99ed95a9eb8b88eb8ba42e222c22ebaaa9eca081eca780eab98ceca780ec9db4eb8f99ed95a9eb8b88eb8ba42e225d2c2264697374616e6365223a38382e302c22656e644e616d65223a22ed959cec84b1eb8c80ec9e85eab5acec97ad34ed98b8ec84a0222c22656e6458223a302e302c22656e6459223a302e302c2273656374696f6e54696d65223a312c2273746172744e616d65223a22222c22737461727458223a302e302c22737461727459223a302e307d2c2273656374696f6e526f757465223a7b22726f7574654c697374223a5b7b226c61746974756465223a33372e3538323530372c226c6f6e676974756465223a3132372e3031303235327d2c7b226c61746974756465223a33372e3538323434322c226c6f6e676974756465223a3132372e3031303235347d2c7b226c61746974756465223a33372e3538323334312c226c6f6e676974756465223a3132372e3031303235367d2c7b226c61746974756465223a33372e3538323334312c226c6f6e676974756465223a3132372e3031303235367d2c7b226c61746974756465223a33372e3538323334312c226c6f6e676974756465223a3132372e3031303338397d2c7b226c61746974756465223a33372e3538323334332c226c6f6e676974756465223a3132372e3031303432387d2c7b226c61746974756465223a33372e3538323334382c226c6f6e676974756465223a3132372e30313035387d2c7b226c61746974756465223a33372e3538323335372c226c6f6e676974756465223a3132372e3031303737347d2c7b226c61746974756465223a33372e3538323336332c226c6f6e676974756465223a3132372e3031303932327d2c7b226c61746974756465223a33372e3538323336332c226c6f6e676974756465223a3132372e30313039377d2c7b226c61746974756465223a33372e3538323336332c226c6f6e676974756465223a3132372e3031313030387d2c7b226c61746974756465223a33372e3538323336342c226c6f6e676974756465223a3132372e3031313136327d2c7b226c61746974756465223a33372e35383233372c226c6f6e676974756465223a3132372e30313132377d2c7b226c61746974756465223a33372e35383233372c226c6f6e676974756465223a3132372e30313132377d2c7b226c61746974756465223a33372e35383233372c226c6f6e676974756465223a3132372e30313132377d5d7d2c227472616666696354797065223a337d2c7b2273656374696f6e496e666f223a7b2264697374616e6365223a313138302e302c22656e644944223a3130353330392c22656e644c6f63616c53746174696f6e4944223a22313037303030313437222c22656e644e616d65223a22ec82bcec84a0eab5902eec84b1ebb681ebacb8ed9994ec9b90222c22656e6453746174696f6e43697479436f6465223a313030302c22656e6453746174696f6e50726f7669646572436f6465223a31302c22656e6458223a3132372e3030353830362c22656e6459223a33372e3538393032362c226c616e65223a5b7b226275734e6f223a22ec84b1ebb6813032222c2274797065223a332c226275734944223a313534312c226275734c6f63616c426c4944223a3130373930303030332c2262757350726f7669646572436f6465223a31307d5d2c2273656374696f6e54696d65223a31302c2273746172744944223a3132333835372c2273746172744c6f63616c53746174696f6e4944223a22313037393030323536222c2273746172744e616d65223a22ed959cec84b1eb8c80eca095ebacb8222c22737461727453746174696f6e43697479436f6465223a313030302c22737461727453746174696f6e50726f7669646572436f6465223a31302c22737461727458223a3132372e3031313237322c22737461727459223a33372e3538323336382c2273746174696f6e436f756e74223a372c2273746174696f6e4e616d6573223a5b22ed959cec84b1eb8c80eca095ebacb8222c22ed959cec9691ec8a88ed8dbcec959e222c2228eab5ac29ebb295ed9994ec82ac222c22ed959cec84b1eb8c80ec9e85eab5ac33eab1b0eba6ac222c22ec84b1ebb681ec84b8ebacb4ec849cec959e222c22ec82bcec84a0ec8b9cec9ea5222c22ed959cec84b1eb8c80ec9e85eab5acec97ad2eec84b1ebb681ecb29cebb684ec8898eba788eba3a8222c22ec82bcec84a0eab5902eec84b1ebb681ebacb8ed9994ec9b90225d7d2c2273656374696f6e526f757465223a7b22726f7574654c697374223a5b7b226c61746974756465223a33372e3538323336382c226c6f6e676974756465223a3132372e3031313237327d2c7b226c61746974756465223a33372e3538323339362c226c6f6e676974756465223a3132372e3031313330367d2c7b226c61746974756465223a33372e3538323431342c226c6f6e676974756465223a3132372e3031313333397d2c7b226c61746974756465223a33372e3538323435392c226c6f6e676974756465223a3132372e30313133357d2c7b226c61746974756465223a33372e3538323530342c226c6f6e676974756465223a3132372e3031313333387d2c7b226c61746974756465223a33372e35383235342c226c6f6e676974756465223a3132372e3031313331357d2c7b226c61746974756465223a33372e3538323534382c226c6f6e676974756465223a3132372e3031313235387d2c7b226c61746974756465223a33372e3538323736342c226c6f6e676974756465223a3132372e3031313139397d2c7b226c61746974756465223a33372e3538333536362c226c6f6e676974756465223a3132372e3031313134337d2c7b226c61746974756465223a33372e3538333536362c226c6f6e676974756465223a3132372e3031313134337d2c7b226c61746974756465223a33372e3538343532392c226c6f6e676974756465223a3132372e3031313037347d2c7b226c61746974756465223a33372e3538343636352c226c6f6e676974756465223a3132372e3031313131377d2c7b226c61746974756465223a33372e3538353037392c226c6f6e676974756465223a3132372e3031313131327d2c7b226c61746974756465223a33372e3538353037392c226c6f6e676974756465223a3132372e3031313131327d2c7b226c61746974756465223a33372e3538353430332c226c6f6e676974756465223a3132372e3031313039367d2c7b226c61746974756465223a33372e3538353937392c226c6f6e676974756465223a3132372e3031313034337d2c7b226c61746974756465223a33372e3538363534362c226c6f6e676974756465223a3132372e3031303931317d2c7b226c61746974756465223a33372e3538363534362c226c6f6e676974756465223a3132372e3031303931317d2c7b226c61746974756465223a33372e3538383038342c226c6f6e676974756465223a3132372e3031303536337d2c7b226c61746974756465223a33372e3538383038342c226c6f6e676974756465223a3132372e3031303536337d2c7b226c61746974756465223a33372e3538383231392c226c6f6e676974756465223a3132372e3031303532377d2c7b226c61746974756465223a33372e3538383331372c226c6f6e676974756465223a3132372e30313034387d2c7b226c61746974756465223a33372e3538383337312c226c6f6e676974756465223a3132372e3031303433347d2c7b226c61746974756465223a33372e3538383339382c226c6f6e676974756465223a3132372e3031303338397d2c7b226c61746974756465223a33372e3538383339372c226c6f6e676974756465223a3132372e3031303333327d2c7b226c61746974756465223a33372e3538383339372c226c6f6e676974756465223a3132372e3031303237357d2c7b226c61746974756465223a33372e3538383333322c226c6f6e676974756465223a3132372e3031303036317d2c7b226c61746974756465223a33372e3538383236372c226c6f6e676974756465223a3132372e3030393833357d2c7b226c61746974756465223a33372e3538383233382c226c6f6e676974756465223a3132372e3030393630397d2c7b226c61746974756465223a33372e3538383230392c226c6f6e676974756465223a3132372e3030393430367d2c7b226c61746974756465223a33372e35383831392c226c6f6e676974756465223a3132372e3030393232357d2c7b226c61746974756465223a33372e35383831382c226c6f6e676974756465223a3132372e3030393135377d2c7b226c61746974756465223a33372e35383831382c226c6f6e676974756465223a3132372e3030393135377d2c7b226c61746974756465223a33372e3538383136312c226c6f6e676974756465223a3132372e3030393034347d2c7b226c61746974756465223a33372e3538383133342c226c6f6e676974756465223a3132372e3030383935347d2c7b226c61746974756465223a33372e3538383036392c226c6f6e676974756465223a3132372e3030383739367d2c7b226c61746974756465223a33372e3538373836382c226c6f6e676974756465223a3132372e3030383437317d2c7b226c61746974756465223a33372e3538373736382c226c6f6e676974756465223a3132372e3030383331337d2c7b226c61746974756465223a33372e3538373731332c226c6f6e676974756465223a3132372e3030383230317d2c7b226c61746974756465223a33372e3538373637362c226c6f6e676974756465223a3132372e3030383037377d2c7b226c61746974756465223a33372e3538373635372c226c6f6e676974756465223a3132372e3030373937357d2c7b226c61746974756465223a33372e3538373634362c226c6f6e676974756465223a3132372e3030373830367d2c7b226c61746974756465223a33372e3538373636332c226c6f6e676974756465223a3132372e3030373633367d2c7b226c61746974756465223a33372e35383736382c226c6f6e676974756465223a3132372e3030373535367d2c7b226c61746974756465223a33372e3538373639382c226c6f6e676974756465223a3132372e3030373438387d2c7b226c61746974756465223a33372e3538373734322c226c6f6e676974756465223a3132372e3030373339377d2c7b226c61746974756465223a33372e3538373831332c226c6f6e676974756465223a3132372e30303732367d2c7b226c61746974756465223a33372e3538373932382c226c6f6e676974756465223a3132372e3030373032317d2c7b226c61746974756465223a33372e3538373932382c226c6f6e676974756465223a3132372e3030373032317d2c7b226c61746974756465223a33372e3538383133322c226c6f6e676974756465223a3132372e3030363633337d2c7b226c61746974756465223a33372e3538383137372c226c6f6e676974756465223a3132372e3030363536347d2c7b226c61746974756465223a33372e3538383234382c226c6f6e676974756465223a3132372e3030363531387d2c7b226c61746974756465223a33372e3538383338322c226c6f6e676974756465223a3132372e3030363431347d2c7b226c61746974756465223a33372e35383836362c226c6f6e676974756465223a3132372e3030363139367d2c7b226c61746974756465223a33372e3538383738352c226c6f6e676974756465223a3132372e3030363131357d2c7b226c61746974756465223a33372e35383839312c226c6f6e676974756465223a3132372e3030353935347d2c7b226c61746974756465223a33372e3538393032362c226c6f6e676974756465223a3132372e3030353830367d5d7d2c227472616666696354797065223a327d2c7b2273656374696f6e496e666f223a7b22636f6e74656e7473223a5b22ecb69cebb09ceca780ec9790ec849c3236ebafb8ed84b0ec9db4eb8f99ed95a9eb8b88eb8ba42e222c22ed9884ec9eacec9c84ecb998ec9790ec849ceca28cecb8a1ec9cbceba19c3338ebafb8ed84b0ec9db4eb8f99ed95a9eb8b88eb8ba42e222c22eb8f84eba19ceb819dec9790ec849ceca28cecb8a1ec9cbceba19c3830ebafb8ed84b0ec9db4eb8f99ed95a9eb8b88eb8ba42e222c22ed9884ec9eacec9c84ecb998ec9790ec849cec9ab0ecb8a1ec9cbceba19c3131ebafb8ed84b0ec9db4eb8f99ed95a9eb8b88eb8ba42e222c22ed9884ec9eacec9c84ecb998ec9790ec849ceca1b0eab888ec9ab0ecb8a1ec9cbceba19c36ebafb8ed84b0ec9db4eb8f99ed95a9eb8b88eb8ba42e222c22ebaaa9eca081eca780eab98ceca780ec9db4eb8f99ed95a9eb8b88eb8ba42e225d2c2264697374616e6365223a36372e302c22656e644e616d65223a22ed959cec84b1eb8c80ec9e85eab5acec97ad34ed98b8ec84a0222c22656e6458223a302e302c22656e6459223a302e302c2273656374696f6e54696d65223a312c2273746172744e616d65223a22222c22737461727458223a302e302c22737461727459223a302e307d2c2273656374696f6e526f757465223a7b22726f7574654c697374223a5b7b226c61746974756465223a33372e3538393032342c226c6f6e676974756465223a3132372e3030353830337d2c7b226c61746974756465223a33372e35383930352c226c6f6e676974756465223a3132372e3030353736367d2c7b226c61746974756465223a33372e35383931382c226c6f6e676974756465223a3132372e30303535387d2c7b226c61746974756465223a33372e35383931382c226c6f6e676974756465223a3132372e30303535387d2c7b226c61746974756465223a33372e3538393132312c226c6f6e676974756465223a3132372e3030353439317d2c7b226c61746974756465223a33372e3538393036312c226c6f6e676974756465223a3132372e3030353430317d2c7b226c61746974756465223a33372e3538383935382c226c6f6e676974756465223a3132372e3030353234387d2c7b226c61746974756465223a33372e3538383935382c226c6f6e676974756465223a3132372e3030353234387d2c7b226c61746974756465223a33372e3538383931382c226c6f6e676974756465223a3132372e3030353330327d2c7b226c61746974756465223a33372e3538383737312c226c6f6e676974756465223a3132372e3030353531397d2c7b226c61746974756465223a33372e3538383730382c226c6f6e676974756465223a3132372e3030353630377d2c7b226c61746974756465223a33372e3538383630392c226c6f6e676974756465223a3132372e3030353733337d2c7b226c61746974756465223a33372e3538383538332c226c6f6e676974756465223a3132372e3030353735337d2c7b226c61746974756465223a33372e35383835372c226c6f6e676974756465223a3132372e3030353831387d2c7b226c61746974756465223a33372e3538383535392c226c6f6e676974756465223a3132372e3030353837317d2c7b226c61746974756465223a33372e3538383537372c226c6f6e676974756465223a3132372e3030353935397d2c7b226c61746974756465223a33372e3538383537372c226c6f6e676974756465223a3132372e3030353935397d2c7b226c61746974756465223a33372e3538383532312c226c6f6e676974756465223a3132372e3030363036357d2c7b226c61746974756465223a33372e3538383532312c226c6f6e676974756465223a3132372e3030363036357d2c7b226c61746974756465223a33372e3538383439372c226c6f6e676974756465223a3132372e3030363038347d2c7b226c61746974756465223a33372e3538383437332c226c6f6e676974756465223a3132372e3030363130347d2c7b226c61746974756465223a33372e3538383437332c226c6f6e676974756465223a3132372e3030363130347d2c7b226c61746974756465223a33372e3538383437332c226c6f6e676974756465223a3132372e3030363130347d5d7d2c227472616666696354797065223a337d5d7d\""

        val json = responseJson
            .substring(1, responseJson.length - 1)
            .chunked(2)
            .map { Integer.parseInt(it, 16).toByte() }
            .toByteArray()
            .toString(Charsets.UTF_8)

        Log.d("API Log", "반환: $json")
        val gson = GsonBuilder()
            .registerTypeAdapter(SubPath::class.java, SubPathAdapter())
            .create()
        val response = gson.fromJson(json, TransportRoute::class.java)

        StaticValue.transportRoute = response
        // TODO TransportRoute 역직렬화 flow 가져간 후 지울 것! -----

        setContent {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                RequestNotificationPermissionDialog()
            }
            SherpaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // TODO: 임시로 설정해둠

                    // 화면 간 이동에 대한 함수
                    // https://developer.android.com/codelabs/basic-android-kotlin-compose-navigation?hl=ko#0
                    navController = rememberNavController()
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
                            MessageAlam(messageViewModel)
                            ScheduleAlam(scheduleViewModel)
                            HomeScreen(navController, Modifier.padding(innerPadding), partnerViewModel)
                        }
                        composable(route = "${SherpaScreen.Search.name}/{destinationValue}",
                            arguments = listOf(navArgument("destinationValue"){type = NavType.StringType})
                        ){
                            val destinationValue = it.arguments?.getString("destinationValue")!!
                            SearchScreen(navController, destinationValue, Modifier.padding(innerPadding))
                        }
                        composable(route = SherpaScreen.SpecificRoute.name){
                            SpecificRouteScreen(navController, StaticValue.transportRoute, partnerViewModel, caregiverViewModel, caretakerViewModel)
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