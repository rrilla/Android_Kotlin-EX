package com.example.loadscroll.data

import com.example.loadscroll.data.model.GiphyListModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("/v1/gifs/trending")
    fun getList(
        @Query("api_key") api_key: String?,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Call<GiphyListModel>
}