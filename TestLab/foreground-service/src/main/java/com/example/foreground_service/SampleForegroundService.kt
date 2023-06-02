package com.example.foreground_service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.foreground_service.MainActivity.Companion.ACTION_STOP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class SampleForegroundService : Service() {
    private val TAG = this::class.java.simpleName

    companion object {
        private val CHANNEL_ID = "test-channel"
        private val CHANNEL_NAME = "test-name"
        private const val NOTIFICATION_DOWNLOAD_ID = 1
        private const val NOTIFICATION_COMPLETE_ID = 2
    }

    private val notificationManager
        get() = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    override fun onCreate() {
        Log.e(TAG, "onCreate")
        registerDefaultNotificationChannel()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_DOWNLOAD_ID, createDownloadingNotification(0))
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..1000) {
                Log.e(TAG, "hjh $i")
                Thread.sleep(100)
                updateProgress(i)
            }
            stopForeground(true)
            stopSelf()
            notificationManager.notify(NOTIFICATION_COMPLETE_ID, createCompleteNotification())
        }
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        super.onDestroy()
    }

    private fun registerDefaultNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("HJH", "26이상임")
            notificationManager.createNotificationChannel(createDefaultNotificationChannel())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDefaultNotificationChannel() =
        NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW).apply {
            Log.e("HJH", "noti 채널 생성")
            this.setShowBadge(true)
            this.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        }

    private fun updateProgress(progress: Int) {
        notificationManager.notify(NOTIFICATION_DOWNLOAD_ID, createDownloadingNotification(progress))
    }

    private fun createDownloadingNotification(progress: Int) =
        NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setOngoing(true)    //  알림 못 닫게
            setContentTitle("Download video...")
            setContentText("Wait!")
            setSmallIcon(R.drawable.ic_launcher_background)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            setContentIntent(
                PendingIntent.getActivity(
                    this@SampleForegroundService,
                    0,
                    Intent(this@SampleForegroundService, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    },
                    PendingIntent.FLAG_MUTABLE or 0
                )
            )

            setProgress(1000, progress, false)
        }.build()

    private fun createCompleteNotification() = NotificationCompat.Builder(this, CHANNEL_ID).apply {
        setContentTitle("Download complete!")
        setContentText("Nice 🚀")
        setSmallIcon(R.drawable.ic_launcher_background)
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        setContentIntent(
            PendingIntent.getActivity(
                this@SampleForegroundService,
                0,
                Intent(this@SampleForegroundService, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                },
                PendingIntent.FLAG_MUTABLE or 0
            )
        )
    }.build()
}