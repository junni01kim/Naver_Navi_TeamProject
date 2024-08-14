package com.hansung.sherpa.FCM

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hansung.sherpa.MainActivity
import com.hansung.sherpa.R
import com.hansung.sherpa.StaticValue
import kotlin.random.Random

class SherpaFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var viewModel: MessageViewModel

    override fun onCreate() {
        super.onCreate()
        // ApplicationContext를 사용하여 ViewModel을 초기화
        viewModel = ViewModelProvider(StaticValue.mainActivity).get(MessageViewModel::class.java)
    }

    /**
     * FCM token이 만료되어 재발급이 되는 경우에 해당 메서드를 통해 새로운 token을 받게 된다.
     */
    override fun onNewToken(token: String){
        Log.d("FCMLog", "Refreshed token: "+token);
    }

    /**
     * 메세지가 들어오는 공간
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCMLog", "뭔가 전송됨")

        // notification
        if(remoteMessage.notification != null) { //포그라운드
            branch(remoteMessage.notification?.title!!, remoteMessage.notification?.body!!)
        }

        // data
        if (remoteMessage.data.isNotEmpty()) { //백그라운드
            remoteMessage.data["title"]?.let { branch(it, remoteMessage.data["body"]!!) }
        }

        remoteMessage.notification?.let { message ->
            sendNotification(message)
        }
    }

    private val random = Random
    companion object {
        const val CHANNEL_NAME = "FCM notification channel"
    }
    private fun sendNotification(message: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, FLAG_IMMUTABLE
        )

        val channelId = this.getString(R.string.default_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        manager.notify(random.nextInt(), notificationBuilder.build())
    }

    /**
     * 분기를 위한 함수
     * ※ 토큰 방식과 토픽 방식 같이 사용하지 못해서, title 머리를 토픽으로 사용하면 좋을 것 같아서 만듦
     */
    private fun branch(head: String, body: String) {
        Log.d("FCMLog", "branch 메서드: 수신 완료")

        Log.d("FCMLog", "$head, $body")
        val parts = head.split("/")
        val topic = parts[0]
        val title = parts[1]

        when(topic) {
            "알림" -> viewModel.updateValue(title, body)
            "예시" -> Log.d("FCMLog","FCM: title(${title}) body(${body})")
            "일정" -> Log.d("FCMLog","FCM: 일정 객체 ${body}")
            "예약경로" -> Log.d("FCMLog", "FCM: 경로 안내 객체 ${body}")
            "로그인" -> Log.d("FCMLog", "FCM: 로그인 성공 ${body}")
            "긴급 연락처" -> Log.d("FCMLog", "FCM: 긴급 연락처 받기 ${body}")
            else -> Log.d("FCMLog", "FCM: message 형식 오류")
        }
    }
}