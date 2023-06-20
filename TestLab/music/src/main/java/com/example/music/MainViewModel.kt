package com.example.music

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.music.common.MusicServiceConnection

class MainViewModel(
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {
    class Factory(
        private val musicServiceConnection: MusicServiceConnection
    ) : ViewModelProvider.NewInstanceFactory() {

        // TODO: 어노테이션 뭔지 봐야됨.
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(musicServiceConnection) as T
        }
    }
}