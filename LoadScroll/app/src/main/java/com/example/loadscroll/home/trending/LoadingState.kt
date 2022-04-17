package com.example.loadscroll.home.trending

sealed class LoadingState {
    object None: LoadingState()

    object Loading: LoadingState()

    object Success: LoadingState()

    object Last: LoadingState()
}