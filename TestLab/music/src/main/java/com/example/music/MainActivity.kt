package com.example.music

import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  음악 볼륨으로 볼륨 컨트롤 설정.
        volumeControlStream = AudioManager.STREAM_MUSIC



    }
}