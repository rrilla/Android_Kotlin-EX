package com.example.samplecleanarchitecture.domain.model

import com.example.samplecleanarchitecture.core.extension.empty

data class Movie(val id: Int, val poster: String) {

    companion object {
        val empty = Movie(0, String.empty())
    }
}