package com.example.backgroundplayer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.backgroundplayer.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "channel_test"
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            sendNotification()
        }
    }

    fun sendNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder: NotificationCompat.Builder = if (Build.VERSION.SDK_INT >= 26) { //android 8.0 -> 26 / 7.0 -> 24  / 6.0 -> 23
                val notificationChannel = NotificationChannel(
                    CHANNEL_ID,
                    "channel_name",
                    NotificationManager.IMPORTANCE_MIN
                )
                notificationManager.createNotificationChannel(notificationChannel)
                NotificationCompat.Builder(this, notificationChannel.id)
            } else {
                NotificationCompat.Builder(this)
            }

        val mediaSession = MediaSessionCompat(applicationContext, "TAG")
        notificationBuilder
            // Show controls on lock screen even when user hides sensitive content.
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            // Add media control buttons that invoke intents in your media service
            .addAction(R.drawable.good, "Previous", null) // #0
            .addAction(R.drawable.prev6, "Previous", null) // #1
            .addAction(R.drawable.pause, "Pause", null) // #2
            .addAction(R.drawable.next, "Next", null) // #3
            .addAction(R.drawable.share, "Previous", null) // #4
            // Apply the media style template
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1,2,3 /* button index \*/)
                .setMediaSession(mediaSession.sessionToken))
            .setContentTitle("제목~~")
            .setContentText("내용~~~")
            .setLargeIcon(getBitmapFromURL("https://mblogthumb-phinf.pstatic.net/MjAxODA1MjlfMjc4/MDAxNTI3NTQ2ODY5MzI1.blQUy8e6nEW7g-6BZXayTSYKADVczhX8g84OWDj6v80g.F8UP3vnl7M1pIVEMyYZGZhz-uKuc9NWmxrjTHrywKeIg.JPEG.dew36/image_7905663351527546830100.jpg?type=w800"))

        notificationManager.notify(
            12,
            notificationBuilder.build()
        )
    }

    private fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            if (src == null) return null
            val url = URL(src)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}