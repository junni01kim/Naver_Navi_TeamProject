package com.hansung.sherpa.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hansung.sherpa.MainActivity
import com.hansung.sherpa.R
import kotlin.random.Random

/**
 * FCM(Firebase Cloud Messaging) 서비스 통신 매체
 *
 */
class SherpaFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * 메세지 서비스를 생성한다.
     *
     */
    override fun onCreate() {
        super.onCreate()
    }

    /**
     * FCM token이 만료되어 재발급이 되는 경우에 해당 메서드를 통해 새로운 token을 받게 된다.
     *
     */
    override fun onNewToken(token: String){
        Log.d("FCMLog", "Refreshed token: $token");
    }

    /**
     * 해당 클라이언트에 메세지가 도착할 때 실행되는 리스너 함수
     *
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCMLog", "onMessageReceived")

        remoteMessage.notification?.let { message ->
            // 앱 푸시 알림
            sendNotification(message)
            // 다이얼로그
            createMessageViewModel(message)
        }
    }

    private val random = Random
    companion object {
        const val CHANNEL_NAME = "FCM notification channel"
    }
    // 푸시 알림 채널을 빌드하는 함수
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

    // 알림 채널을 만드는 함수
    private fun createMessageViewModel(message: RemoteMessage.Notification) {

        Log.d("FCMLog", "Message received: ${message.title} ${message.body}")

        val title = message.title ?: "제목 없음"
        val body = message.body ?: "내용 없음"
        val intent = Intent("FCM_MESSAGE").setPackage("com.hansung.sherpa")

        intent.putExtra("title", title)
        intent.putExtra("body", body)

        // MainActivity로 전송
        sendBroadcast(intent)
    }
}