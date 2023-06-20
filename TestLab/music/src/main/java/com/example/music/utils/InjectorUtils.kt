package com.example.music.utils

import android.content.ComponentName
import android.content.Context
import com.example.music.MainViewModel
import com.example.music.common.MusicService
import com.example.music.common.MusicServiceConnection

object InjectorUtils {
    private fun provideMusicServiceConnection(context: Context): MusicServiceConnection {
        return MusicServiceConnection.getInstance(
            context,
            ComponentName(context, MusicService::class.java)
        )
    }

    fun provideMainActivityViewModel(context: Context): MainViewModel.Factory {
        val applicationContext = context.applicationContext
        val musicServiceConnection = provideMusicServiceConnection(applicationContext)
        return MainViewModel.Factory(musicServiceConnection)
    }
}