package com.example.loadscroll.home.trending

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loadscroll.BuildConfig
import com.example.loadscroll.MyApplication
import com.example.loadscroll.data.db.Favorite
import com.example.loadscroll.data.model.Gif
import com.example.loadscroll.data.model.GiphyListModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrendingViewModel: ViewModel() {

    private var page = 0  // page * limit = offset(조회 데이터 시작위치)
    private var favoriteList: List<Favorite>? = null

    private var _loadingStateData = MutableLiveData<LoadingState>(LoadingState.Last)
    val loadingStateData: LiveData<LoadingState> = _loadingStateData

    private var _giphyLiveData = MutableLiveData<GiphyListModel>()
    val giphyLiveData: LiveData<GiphyListModel> = _giphyLiveData

    fun getData(limit: Int): Job = viewModelScope.launch {
        setLoadingState(LoadingState.Loading)
        if(favoriteList == null){
            favoriteList = selectAllDbFavorite()
        }
        MyApplication.networkService.getTrendingList(
            BuildConfig.GIPHY_API_KEY,
            limit, page * limit)
            .enqueue(object: Callback<GiphyListModel> {
                override fun onResponse(call: Call<GiphyListModel>, response: Response<GiphyListModel>) {
                    val giphyData: GiphyListModel = response.body()!!
                    if(favoriteList!!.isNotEmpty()) {
                        for (giphy in giphyData.data) {
                            for (favorite in favoriteList!!) {
                                if (giphy.id == favorite.gifId) {
                                    giphy.images.fixed_width.isFavorite = true
                                }
                            }
                        }
                    }
                    setGiphyData(giphyData)
                    page++
                    //  count가 요청 데이터(limit)보다 작을때 마지막페이지
                    Log.e("hjh", "Giphy Api 호출" +
                            "\n offset(시작 위치) : ${giphyData.pagination.offset}" +
                            "\n count(반환 데이터 수) : ${giphyData.pagination.count}"
                    )
                    if(giphyData.pagination.count > limit) {
                        setLoadingState(LoadingState.Last)
                    }else{
                        setLoadingState(LoadingState.Success)
                    }
                }
                override fun onFailure(call: Call<GiphyListModel>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun setGiphyData(data: GiphyListModel) {
        _giphyLiveData.value?.let {
            val preData = it.data
            preData.addAll(data.data)
            data.data.clear()
            data.data.addAll(preData)
        }
        _giphyLiveData.postValue(data)
    }

    private fun setLoadingState(state: LoadingState) {
        _loadingStateData.postValue(state)
    }

    fun insertGifId(position: Int) = viewModelScope.launch{
        _giphyLiveData.value?.data?.get(position)?.let {
            insertDbFavorite(it.id)
            it.images.fixed_width.isFavorite = true
        }
    }

    fun deleteGifId(position: Int) = viewModelScope.launch{
        _giphyLiveData.value?.data?.get(position)?.let {
            deleteDbFavorite(it.id)
            it.images.fixed_width.isFavorite = false
        }
    }

    private suspend fun insertDbFavorite(gifId: String) = withContext(Dispatchers.IO) {
        return@withContext MyApplication.dataBase.favoriteDao().insert(Favorite(gifId))
    }

    private suspend fun deleteDbFavorite(gifId: String) = withContext(Dispatchers.IO) {
        return@withContext MyApplication.dataBase.favoriteDao().delete(gifId)
    }

    private suspend fun selectAllDbFavorite() = withContext(Dispatchers.IO){
        return@withContext MyApplication.dataBase.favoriteDao().getAll()
    }
}