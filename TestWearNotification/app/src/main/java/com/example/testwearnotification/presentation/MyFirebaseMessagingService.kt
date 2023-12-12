package com.example.testwearnotification.presentation

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.testwearnotification.R
import com.example.testwearnotification.presentation.handler.CallIntentService
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

        // TODO: 알림 수신됬다고 API쏴야됨.  
        mNotificationManagerCompat = NotificationManagerCompat.from(this)

        mNotificationManagerCompat.areNotificationsEnabled()    // 알림 차단 여부. 확인 필요.

        // TODO: FCM 알림 구분해야됨. 1:손님 콜 발생, 2:러너 콜 수락.
        val flag = 1
        if (flag==1) {
            // 노티 발생.
            generateBigTextStyleNotification()
        } else {
            // 노티 제거. 포그라운드 시 데이터 갱신.
        }
    }

    // TODO: param 으로 data 받아서 알림에 출력,
    private fun generateBigTextStyleNotification() {
        Log.d(TAG, "generateBigTextStyleNotification()")

        // Main steps for building a BIG_TEXT_STYLE notification:
        //      0. Get your data
        //      1. Create/Retrieve Notification Channel for O and beyond devices (26+)
        //      2. Build the BIG_TEXT_STYLE
        //      3. Set up main Intent for notification
        //      4. Create additional Actions for the Notification
        //      5. Build and issue the notification

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
        val mainPendingIntent = PendingIntent.getActivity(
            this,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 4. Create additional Actions (Intents) for the Notification.

        // In our case, we create two additional actions: a Snooze action and a Dismiss action.

        // Snooze Action.
        val acceptIntent = Intent(this, CallIntentService::class.java).apply {
            action = CallIntentService.ACTION_ACCEPT
        }
        val acceptPendingIntent = PendingIntent.getService(this, 0, acceptIntent, PendingIntent.FLAG_IMMUTABLE)
        val acceptAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
            com.google.android.gms.base.R.drawable.common_full_open_on_phone,
            "Accept",
            acceptPendingIntent
        ).build()

        // Dismiss Action.
        val dismissIntent = Intent(this, CallIntentService::class.java)
        dismissIntent.action = CallIntentService.ACTION_DISMISS
        val dismissPendingIntent = PendingIntent.getService(this, 0, dismissIntent, PendingIntent.FLAG_IMMUTABLE)
        val dismissAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
            androidx.core.R.drawable.notification_bg,
            "Dismiss",
            dismissPendingIntent
        ).build()


        // 5. Build and issue the notification.

        // Because we want this to be a new notification (not updating a previous notification), we
        // create a new Builder. Later, we use the same global builder to get back the notification
        // we built here for the snooze action, that is, canceling the notification and relaunching
        // it several seconds later.

        // Notification Channel Id is ignored for Android pre O (26).
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
            .setCategory(Notification.CATEGORY_REMINDER) // Sets priority for 25 and below. For 26 and above, 'priority' is deprecated for
            // 'importance' which is set in the NotificationChannel. The integers representing
            // 'priority' are different from 'importance', so make sure you don't mix them.
            .setPriority(NotificationManager.IMPORTANCE_HIGH) // Sets lock-screen visibility for 25 and below. For 26 and above, lock screen
            // visibility is set in the NotificationChannel.
//            .setVisibility(bigTextStyleReminderAppData.getChannelLockscreenVisibility()) // Adds additional actions specified above.
            .addAction(acceptAction)
            .addAction(dismissAction)

        /* REPLICATE_NOTIFICATION_STYLE_CODE:
         * You can replicate Notification Style functionality on Wear 2.0 (24+) by not setting the
         * main content intent, that is, skipping the call setContentIntent(). However, you need to
         * still allow the user to open the native Wear app from the Notification itself, so you
         * add an action to launch the app.
         */if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            // Enables launching app in Wear 2.0 while keeping the old Notification Style behavior.
            val mainAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
                androidx.asynclayoutinflater.R.drawable.notification_bg,
                "Open",
                mainPendingIntent
            )
                .build()
            notificationCompatBuilder.addAction(mainAction)
        } else {
            // Wear 1.+ still functions the same, so we set the main content intent.
            notificationCompatBuilder.setContentIntent(mainPendingIntent)
        }
        val notification = notificationCompatBuilder.build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mNotificationManagerCompat.notify(
            33,
            notification
        )
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