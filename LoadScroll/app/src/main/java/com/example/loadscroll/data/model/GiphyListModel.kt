package com.example.loadscroll.data.model

data class GiphyListModel(
    val data: List<Data>,
    val pagination: Pagination,
    var changeFavorite: Boolean = false,
)
