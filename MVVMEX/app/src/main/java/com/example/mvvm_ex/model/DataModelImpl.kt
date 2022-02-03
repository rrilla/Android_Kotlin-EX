package com.example.mvvm_ex.model

import com.example.mvvm_ex.model.enum.KakaoSearchSortEnum
import com.example.mvvm_ex.model.response.ImageSearchResponse
import com.example.mvvm_ex.model.service.KakaoSearchService
import io.reactivex.Single

class DataModelImpl(private val service: KakaoSearchService): DataModel {

    private val KAKAO_APP_KEY = "YOUR_APP_KEY"

    override fun getData(query:String, sort: KakaoSearchSortEnum, page:Int, size:Int): Single<ImageSearchResponse> {
        return service.searchImage(auth = "KakaoAK $KAKAO_APP_KEY", query = query, sort = sort.sort, page = page, size = size)
    }
}