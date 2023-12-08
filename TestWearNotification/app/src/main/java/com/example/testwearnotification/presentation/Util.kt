package com.example.testwearnotification.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.example.testwearnotification.R

class Util {

    companion object {
        private const val channelId = "channel_call"
        private const val channelName = "HJH"

        fun createChannel(context: Context): String {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(
                context
            ).createNotificationChannel(notificationChannel)

            return channelId
        }
    }
}