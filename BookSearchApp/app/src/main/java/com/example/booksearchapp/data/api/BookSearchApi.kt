package com.example.booksearchapp.data.api

import com.example.booksearchapp.data.model.News
import com.example.booksearchapp.data.model.SearchResponse
import com.example.booksearchapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface BookSearchApi {

    @Headers("Authorization: KakaoAK ${Constants.API_KEY}")
    @GET("v3/search/book")
    suspend fun searchBooks(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<SearchResponse>

    @Headers("X-Naver-Client-Id: d4sXWoObYGrit1W4C_PL", "X-Naver-Client-Secret: y9GiJXvLd7")
    @GET("v1/search/news.json?sort=date")
    suspend fun searchNews(
        @Query("query") query: String,
    ): Response<News>
}