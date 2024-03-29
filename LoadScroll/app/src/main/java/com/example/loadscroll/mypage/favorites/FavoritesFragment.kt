package com.example.loadscroll.mypage.favorites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.loadscroll.R
import com.example.loadscroll.databinding.FragmentFavoritesBinding
import com.example.loadscroll.home.trending.LoadingState
import com.example.loadscroll.home.trending.MyAdapter
import com.example.loadscroll.home.trending.TrendingFragment
import com.example.loadscroll.home.trending.TrendingViewModel

class FavoritesFragment : Fragment() {
    lateinit var binding: FragmentFavoritesBinding
    lateinit var myStaggeredGridLayoutManager: StaggeredGridLayoutManager
    private val myAdapter = MyAdapter()
    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeData()
    }

    private fun initViews() = with(binding){
        recyclerView.apply {
            myStaggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            recyclerView.layoutManager = myStaggeredGridLayoutManager
            recyclerView.adapter = myAdapter
        }

        myAdapter.setItemClickListener { v, position, isChecked ->
            if(!isChecked){
                viewModel.deleteGifId(position)
                myAdapter.delItem(position)
            }
        }

        //  RecyclerView로 스크롤 하단 체크 - xml의 NestedScrollView 제거
//        recyclerView.addOnScrollListener(RvListener())

        //  NestedScrollView로 스크롤 하단 체크
        nestedScrollView.setOnScrollChangeListener(NvListener())
    }

    private fun observeData() {
        viewModel.giphyLiveData.observe(viewLifecycleOwner) {
            myAdapter.addItem(it)
        }

        viewModel.loadingStateData.observe(viewLifecycleOwner) {
            when (it) {
                is LoadingState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is LoadingState.Success -> binding.progressBar.visibility = View.GONE
                is LoadingState.None -> {
                    binding.progressBar.visibility = View.GONE
                    //  아직 좋아요 데이터 없음 표시 UI
                }
                else -> {}
            }
        }
    }

    inner class RvListener: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

//            val spanCount = myStaggeredGridLayoutManager.spanCount
////                    아이템의 최하단이 완전히 보이는 가장 밑의 아이템
//            val lastVisibleItems =
//                (recyclerView.layoutManager as StaggeredGridLayoutManager?)!!.findLastVisibleItemPositions(
//                    IntArray(spanCount)
//                )

            // 스크롤이 끝에 도달했는지 확인 1: 하단, -1 상단
            if (!binding.recyclerView.canScrollVertically(1)) {
                if(viewModel.loadingStateData.value == LoadingState.Success){
                    Log.e("hjh", "rv스크롤 하단 도착, 데이터 요청")
//                    viewModel.getData()
                }else{
                    //  마지막 페이지 ui 띄우기
                    Log.e("hjh", "마지막 페이지")
                }
            }
        }
    }

    inner class NvListener: NestedScrollView.OnScrollChangeListener{
        override fun onScrollChange(
            v: NestedScrollView,
            scrollX: Int,
            scrollY: Int,
            oldScrollX: Int,
            oldScrollY: Int
        ) {
            var count = 0
            //  스크롤이 끝에 도달했는지 확인
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                if(viewModel.loadingStateData.value == LoadingState.Success){
                    Log.e("hjh", "nv스크롤 하단 도착, 데이터 요청")
//                    viewModel.getData()
                }else{
                    //  마지막 페이지 ui 띄우기
                    Log.e("hjh", "마지막 페이지")
                }
            }
        }
    }
}