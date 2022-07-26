package com.example.booksearchapp.data.model.repository

import androidx.lifecycle.LiveData
import com.example.booksearchapp.data.model.Book
import com.example.booksearchapp.data.model.SearchResponse
import com.example.booksearchapp.data.model.api.RetrofitInstance.api
import com.example.booksearchapp.data.model.db.BookSearchDatabase
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class BookSearchRepositoryImpl(private val db: BookSearchDatabase) : BookSearchRepository {

    override suspend fun searchBooks(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): Response<SearchResponse> {
        return api.searchBooks(query, sort, page, size)
    }

    override suspend fun insertBooks(book: Book) {
        db.bookSearchDao().insertBook(book)
    }

    override suspend fun deleteBooks(book: Book) {
        db.bookSearchDao().deleteBook(book)
    }

//    override fun getFavoriteBooks(): LiveData<List<Book>> {
//        return db.bookSearchDao().getFavoriteBooks()
//    }
        override fun getFavoriteBooks(): Flow<List<Book>> {
        return db.bookSearchDao().getFavoriteBooks()
    }
}