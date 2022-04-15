package com.example.loadscroll.mypage.favorites

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
import com.example.loadscroll.home.trending.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesViewModel: ViewModel() {

    private val count = 10

    private var _loadingStateData = MutableLiveData<LoadingState>(LoadingState.Last)
    val loadingStateData: LiveData<LoadingState> = _loadingStateData

    private var _giphyLiveData = MutableLiveData<GiphyListModel>()
    val giphyLiveData: LiveData<GiphyListModel> = _giphyLiveData

    fun getData(): Job = viewModelScope.launch {
        setLoadingState(LoadingState.Loading)
        val ids: String = selectAllDbFavorite()
        MyApplication.networkService.getListById(
            BuildConfig.GIPHY_API_KEY, ids)
            .enqueue(object: Callback<GiphyListModel> {
                override fun onResponse(call: Call<GiphyListModel>, response: Response<GiphyListModel>) {
                    val giphyData: GiphyListModel = response.body()!!
                    for(giphy in giphyData.data){
                        giphy.images.fixed_width.isFavorite = true
                    }
                    setGiphyData(giphyData)
                    //  count가 요청 데이터(limit)보다 작을때 마지막페이지
                    Log.e("hjh", "Giphy Api 호출" +
                            "\n offset(시작 위치) : ${giphyData.pagination.offset}" +
                            "\n count(반환 데이터 수) : ${giphyData.pagination.count}"
                    )
                    setLoadingState(LoadingState.Success)
                }
                override fun onFailure(call: Call<GiphyListModel>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun setGiphyData(data: GiphyListModel) {
        _giphyLiveData.postValue(data);
    }

    private fun setLoadingState(state: LoadingState) {
        _loadingStateData.postValue(state)
    }

    fun insertGifId(gifId: String, position: Int) = viewModelScope.launch{
        insertDbFavorite(gifId)
    }

    fun deleteGifId(gifId: String, position: Int) = viewModelScope.launch{
        deleteDbFavorite(gifId)
    }

    private suspend fun insertDbFavorite(gifId: String) = withContext(Dispatchers.IO) {
        return@withContext MyApplication.dataBase.favoriteDao().insert(Favorite(gifId))
    }

    private suspend fun deleteDbFavorite(gifId: String) = withContext(Dispatchers.IO) {
        return@withContext MyApplication.dataBase.favoriteDao().delete(gifId)
    }

    private suspend fun selectAllDbFavorite() = withContext(Dispatchers.IO){
        val data = MyApplication.dataBase.favoriteDao().getAll()
        var ids = ""
        for(item in data){
            ids += "${item.gifId},"
        }
        Log.e("hjh", ids)
        return@withContext ids
    }
}