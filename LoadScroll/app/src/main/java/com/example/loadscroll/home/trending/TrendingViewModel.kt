package com.example.loadscroll.home.trending

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loadscroll.MyApplication
import com.example.loadscroll.data.model.Gif
import com.example.loadscroll.data.model.GiphyListModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrendingViewModel: ViewModel() {

    private var page = 0  // result 시작 위치

    private var _loadingStateData = MutableLiveData<LoadingState>(LoadingState.Last)
    val loadingStateData: LiveData<LoadingState> = _loadingStateData

    private var _gifLiveData = MutableLiveData<GiphyListModel>()
    val gifLiveData: LiveData<GiphyListModel> = _gifLiveData

    fun getData(limit: Int): Job = viewModelScope.launch {
        setLoadingState(LoadingState.Loading)
        MyApplication.networkService.getList(
            MyApplication.API_KEY,
            limit, page * limit)
            .enqueue(object: Callback<GiphyListModel> {
                override fun onResponse(call: Call<GiphyListModel>, response: Response<GiphyListModel>) {
                    val giphyData: GiphyListModel = response.body()!!
                    setGiphyData(giphyData)
                    page++
                    //  count가 요청 페이지(limit)보다 낮을때 마지막페이지
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
        _gifLiveData.postValue(data);
    }

    private fun setLoadingState(state: LoadingState) {
        _loadingStateData.postValue(state)
    }
}