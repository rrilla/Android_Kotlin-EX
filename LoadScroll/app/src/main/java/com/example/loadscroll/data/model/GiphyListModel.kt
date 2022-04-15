package com.example.loadscroll.data.model

data class GiphyListModel(
    val data: MutableList<Data>,
    val pagination: Pagination,
    var changeFavorite: Boolean = false,
)
