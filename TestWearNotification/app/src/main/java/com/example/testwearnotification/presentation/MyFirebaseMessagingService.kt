package com.example.testwearnotification.presentation

import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG = this.javaClass.simpleName

    private lateinit var mNotificationManagerCompat: NotificationManagerCompat
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.e("HJH", "FCM token : $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.e(
            "HJH",
            "onMessageReceived : " + message.data
        )

        mNotificationManagerCompat = NotificationManagerCompat.from(this)

        mNotificationManagerCompat.areNotificationsEnabled()    // 알림 차단 여부. 확인 필요.
    }

    private fun openNotificationSettingsForApp() {
        // Links to this app's notification settings
        // 이 앱 알림 설정으로 이동.
        val intent = Intent()
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("app_package", packageName)
        intent.putExtra("app_uid", applicationInfo.uid)
        startActivity(intent)
    }
}