package com.example.mvvm_ex.model

import com.example.mvvm_ex.model.enum.KakaoSearchSortEnum
import com.example.mvvm_ex.model.response.ImageSearchResponse
import io.reactivex.Single

interface DataModel {
    fun getData(query:String, sort: KakaoSearchSortEnum, page:Int, size:Int): Single<ImageSearchResponse>
}