package com.example.samplecleanarchitecture.view.main.home

data class HomeUiState(
    val dd: Boolean = false,
    val data : List<UiMovie> = listOf()
) {
    data class UiMovie(
        val id: Int,
        val poster: String
    )
}