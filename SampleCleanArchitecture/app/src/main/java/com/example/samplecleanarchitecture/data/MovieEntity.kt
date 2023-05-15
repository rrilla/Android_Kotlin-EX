package com.example.samplecleanarchitecture.data

import com.example.samplecleanarchitecture.domain.model.Movie

data class MovieEntity(private val id: Int, private val poster: String) {
    fun toMovie() = Movie(id, poster)
}