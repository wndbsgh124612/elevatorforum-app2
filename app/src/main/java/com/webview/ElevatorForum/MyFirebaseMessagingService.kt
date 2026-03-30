package com.webview.ElevatorForum

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // 토큰 저장은 MainActivity에서 처리
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        createChannel()

        val title = message.data["title"]
            ?: message.notification?.title
            ?: getString(R.string.app_name)

        val body = message.data["body"]
            ?: message.notification?.body
            ?: "새 알림이 도착했습니다."

        val targetUrl = message.data["url"]
            ?: getString(R.string.start_url)

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("push_url", targetUrl)
            data = Uri.parse(targetUrl)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        if (
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this)
                .notify((System.currentTimeMillis() % 100000).toInt(), notification)

            playCustomSound()
        }
    }

    private fun playCustomSound() {
        try {
            val player = MediaPlayer.create(this, R.raw.elevator_alert)
            player?.setOnCompletionListener {
                it.release()
            }
            player?.setOnErrorListener { mp, _, _ ->
                mp.release()
                true
            }
            player?.start()
        } catch (_: Exception) {
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager.deleteNotificationChannel(CHANNEL_ID)

            val channel = NotificationChannel(
                CHANNEL_ID,
                "엘리베이터포럼 알림",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "엘리베이터포럼 푸시 알림"
                enableVibration(true)
                enableLights(true)
                // 채널 기본 소리는 끔. 소리는 MediaPlayer로 직접 재생
                setSound(null, null)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            }

            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "elevator_forum_alert_v10"
    }
}
