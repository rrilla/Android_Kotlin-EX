package com.example.testwearapp.complication

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.testwearapp.R
import com.example.testwearapp.complication.dto.FcmCallNotification
import com.example.testwearapp.complication.handler.CallIntentService
import com.example.testwearapp.presentation.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService2: FirebaseMessagingService() {
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

        // TODO: 알림 수신됬다고 API쏴야됨.
        mNotificationManagerCompat = NotificationManagerCompat.from(applicationContext)

        if (mNotificationManagerCompat.areNotificationsEnabled()) {
            // TODO: FCM 알림 구분해야됨. 1:손님 콜 발생, 2:러너 콜 수락.
            try {
                val fcmNotification = FcmCallNotification.fromMap(message.data)


                Log.e("HJH", "fcm data : $fcmNotification")

                if (fcmNotification.data.approvedBy == null) {
                    // 손님 콜 발생. 노티 발생.
                    generateBigTextStyleNotification()
                } else {
                    // 러너 콜 수락. 노티 제거. 포그라운드 시 데이터 갱신.
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // TODO: firebase Crash 등록. custom key = data
            }

        } else {
            // TODO: 알림 권한 미승인 시 토스트. 포그라운드에서만 동작. 백그라운드 필요하면 추가 구현 필요.
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(this@MyFirebaseMessagingService2, "알림 설정을 수락해주세요.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // TODO: param 으로 data 받아서 알림에 출력,
    private fun generateBigTextStyleNotification() {
        Log.d(TAG, "generateBigTextStyleNotification()")
        val notificationCode = 30


        // 0. Get your data (everything unique per Notification).
//        val bigTextStyleReminderAppData: MockDatabase.BigTextStyleReminderAppData =
//            MockDatabase.getBigTextStyleData()

        // 1. Create/Retrieve Notification Channel for O and beyond devices (26+).
        val notificationChannelId: String = Util.createChannel(this)

        // 2. Build the BIG_TEXT_STYLE
        val bigTextStyle =
            NotificationCompat.BigTextStyle() // Overrides ContentText in the big form of the template.
                .bigText("bigText-제목") // Overrides ContentTitle in the big form of the template.
                .setBigContentTitle("bigContentTitle-내용") // Summary line after the detail section in the big form of the template
                // Note: To improve readability, don't overload the user with info. If Summary Text
                // doesn't add critical information, you should skip it.
                .setSummaryText("summaryText-요약 내용")

        // 3. Set up main Intent for notification.
        val mainIntent = Intent(this, MainActivity::class.java)
        val mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val mainAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
            null,
            "Open",
            mainPendingIntent
        ).build()


        // 4. Create additional Actions (Intents) for the Notification.

        // Snooze Action.
        val acceptIntent = Intent(this, CallIntentService::class.java).apply {
            action = CallIntentService.ACTION_ACCEPT
            putExtra(CallIntentService.ARG_PARAM_ID, notificationCode)
        }
        val acceptPendingIntent = PendingIntent.getService(this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val acceptAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
            null,
            "Accept",
            acceptPendingIntent
        ).build()

        // Dismiss Action.
        val dismissIntent = Intent(this, CallIntentService::class.java).apply {
            action = CallIntentService.ACTION_DISMISS
            putExtra(CallIntentService.ARG_PARAM_ID, notificationCode)
        }
        dismissIntent.putExtra(CallIntentService.ARG_PARAM_ID, notificationCode)
        val dismissPendingIntent = PendingIntent.getService(this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val dismissAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
            null,
            "Dismiss",
            dismissPendingIntent
        ).build()


        // 5. Build and issue the notification.
        val notificationCompatBuilder = NotificationCompat.Builder(
            applicationContext, notificationChannelId
        )
        notificationCompatBuilder // BIG_TEXT_STYLE sets title and content.
            .setStyle(bigTextStyle)
            .setContentTitle("zzzzzz제목")
            .setContentText("zzzzzzz컨텐츠")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    com.google.firebase.messaging.ktx.R.drawable.common_google_signin_btn_icon_dark_focused
                )
            )
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Set primary color (important for Wear 2.0 Notifications).
//            .setColor(ContextCompat.getColor(applicationContext, ))
            .setCategory(Notification.CATEGORY_REMINDER)
            .addAction(acceptAction)
            .addAction(dismissAction)
            .addAction(mainAction)

        val notification = notificationCompatBuilder.build()

        mNotificationManagerCompat.notify(
            notificationCode,
            notification)
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