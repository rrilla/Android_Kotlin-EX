package com.example.testwearapp.complication


import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.testwearapp.presentation.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "StandaloneMainActivity"

    private lateinit var mNotificationManagerCompat: NotificationManagerCompat;

    override fun onMessageReceived(message: RemoteMessage) {
        Log.e(
            "HJH",
            "onMessageReceived : " + message.data
        )
        Log.e("HJH", "uid1 : ${Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)}")


//        super.onMessageReceived(message)
        mNotificationManagerCompat = NotificationManagerCompat.from(
            this
        );
        generateBigTextStyleNotification()
    }


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

        val notificationChannelId: String = getString(com.example.testwearapp.R.string.channel_id)

        // 2. Build the BIG_TEXT_STYLE
        val bigTextStyle: Notification.BigTextStyle =
            Notification.BigTextStyle() // Overrides ContentText in the big form of the template.
                .bigText("BigText") // Overrides ContentTitle in the big form of the template.
                .setBigContentTitle("setBigContentTitle") // Summary line after the detail section in the big form of the template
                // Note: To improve readability, don't overload the user with info. If Summary Text
                // doesn't add critical information, you should skip it.
                .setSummaryText("setSummaryText")


        // 3. Set up main Intent for notification.
        val mainIntent = Intent(this, MainActivity::class.java)
        val mainPendingIntent = PendingIntent.getActivity(
            this,
            0,
            mainIntent,
            PendingIntent.FLAG_IMMUTABLE
        )


        // 4. Create additional Actions (Intents) for the Notification.

        // In our case, we create two additional actions: a Snooze action and a Dismiss action.

        // Snooze Action.
//        val snoozeIntent = Intent(this, BigTextIntentService::class.java)
//        snoozeIntent.action = BigTextIntentService.ACTION_SNOOZE
//        val snoozePendingIntent = PendingIntent.getService(this, 0, snoozeIntent, 0)
//        val snoozeAction: NotificationCompat.Action = Builder(
//            R.drawable.ic_alarm_white_48dp,
//            "Snooze",
//            snoozePendingIntent
//        )
//            .build()

//        // Dismiss Action.
//        val dismissIntent = Intent(this, BigTextIntentService::class.java)
//        dismissIntent.action = BigTextIntentService.ACTION_DISMISS
//        val dismissPendingIntent = PendingIntent.getService(this, 0, dismissIntent, 0)
//        val dismissAction: NotificationCompat.Action = Builder(
//            R.drawable.ic_cancel_white_48dp,
//            "Dismiss",
//            dismissPendingIntent
//        )
//            .build()


        // 5. Build and issue the notification.

        // Because we want this to be a new notification (not updating a previous notification), we
        // create a new Builder. Later, we use the same global builder to get back the notification
        // we built here for the snooze action, that is, canceling the notification and relaunching
        // it several seconds later.

        // Notification Channel Id is ignored for Android pre O (26).
        val notificationCompatBuilder: Notification.Builder = Notification.Builder(
            this, notificationChannelId
        )
        notificationCompatBuilder // BIG_TEXT_STYLE sets title and content.
            .setStyle(bigTextStyle)
            .setContentTitle("setContentTitle")
            .setContentText("setContentText")
            .setSmallIcon(R.drawable.btn_minus)
//            .setLargeIcon(
//                BitmapFactory.decodeResource(
//                    resources,
//                    R.drawable.ic_lock_idle_alarm
//                )
//            )
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Set primary color (important for Wear 2.0 Notifications).
            .setColor(ContextCompat.getColor(applicationContext, R.color.holo_purple))
            .setCategory(Notification.CATEGORY_REMINDER) // Sets priority for 25 and below. For 26 and above, 'priority' is deprecated for
            // 'importance' which is set in the NotificationChannel. The integers representing
            // 'priority' are different from 'importance', so make sure you don't mix them.
            .setPriority(Notification.PRIORITY_HIGH) // Sets lock-screen visibility for 25 and below. For 26 and above, lock screen
        // visibility is set in the NotificationChannel.

        /* REPLICATE_NOTIFICATION_STYLE_CODE:
         * You can replicate Notification Style functionality on Wear 2.0 (24+) by not setting the
         * main content intent, that is, skipping the call setContentIntent(). However, you need to
         * still allow the user to open the native Wear app from the Notification itself, so you
         * add an action to launch the app.
//         */if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//
//            // Enables launching app in Wear 2.0 while keeping the old Notification Style behavior.
//            val mainAction: NotificationCompat.Action = Notification.Builder(
//                R.drawable.btn_minus,
//                "Open",
//                mainPendingIntent
//            )
//                .build()
//            notificationCompatBuilder.addAction(mainAction)
//        } else {
//            // Wear 1.+ still functions the same, so we set the main content intent.
//            notificationCompatBuilder.setContentIntent(mainPendingIntent)
//        }
            val notification: Notification = notificationCompatBuilder.build()
            mNotificationManagerCompat.notify(33, notification)

            // Close app to demonstrate notification in steam.
//        finish()
        }

    }



}