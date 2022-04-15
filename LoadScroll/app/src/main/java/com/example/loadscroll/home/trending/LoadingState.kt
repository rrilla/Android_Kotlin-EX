package com.example.loadscroll.home.trending

sealed class LoadingState {
    object Request: LoadingState()

    object Loading: LoadingState()

    object Success: LoadingState()

    object Last: LoadingState()
}