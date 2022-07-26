package com.example.booksearchapp.data.model


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Book(
    @Json(name = "authors")
    val authors: List<String>,
    @Json(name = "contents")
    val contents: String,
    @Json(name = "datetime")
    val datetime: String,
    @Json(name = "isbn")
    val isbn: String,
    @Json(name = "price")
    val price: Int,
    @Json(name = "publisher")
    val publisher: String,
    @field:Json(name = "sale_price")
    val salePrice: Int,
    @Json(name = "status")
    val status: String,
    @Json(name = "thumbnail")
    val thumbnail: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "translators")
    val translators: List<String>,
    @Json(name = "url")
    val url: String
) : Parcelable