package com.example.backgroundplayer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.backgroundplayer.databinding.ActivityMainBinding
import android.support.v4.app.NotificationCompat
import android.support.v4.media.app.NotificationCompat as MediaNotificationCompat

class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "channel_test"
    }

    private val binding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sendNotification()
    }

    fun sendNotification() {
        var notification = NotificationCompat.Builder(this, CHANNEL_ID)
            // Show controls on lock screen even when user hides sensitive content.
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            // Add media control buttons that invoke intents in your media service
            .addAction(com.google.android.material.R.drawable.material_ic_keyboard_arrow_previous_black_24dp, "Previous", null) // #0
            .addAction(R.drawable.pause, "Pause", null) // #1
            .addAction(com.google.android.material.R.drawable.material_ic_keyboard_arrow_next_black_24dp, "Next", null) // #2
            // Apply the media style template
            .setStyle(MediaNotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1 /* #1: pause button \*/))
//                .setMediaSession(mediaSession.getSessionToken()))
            .setContentTitle("Wonderful music")
            .setContentText("My Awesome Band")
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.background))
            .build()
    }
}