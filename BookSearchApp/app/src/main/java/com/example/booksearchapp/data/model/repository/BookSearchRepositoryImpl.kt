package com.example.booksearchapp.data.model.repository

import com.example.booksearchapp.data.model.SearchResponse
import com.example.booksearchapp.data.model.api.RetrofitInstance.api
import retrofit2.Response

class BookSearchRepositoryImpl : BookSearchRepository {

    override suspend fun searchBooks(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): Response<SearchResponse> {
        return api.searchBooks(query, sort, page, size)
    }
}