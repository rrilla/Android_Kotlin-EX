package com.example.domain.model

data class Movie(val id: Int, val poster: String) {

    companion object {
        val empty = Movie(0, "")
    }
}