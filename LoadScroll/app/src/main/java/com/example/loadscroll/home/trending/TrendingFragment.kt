package com.example.loadscroll.home.trending

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.loadscroll.databinding.FragmentTrendingBinding

class TrendingFragment : Fragment() {
    lateinit var binding: FragmentTrendingBinding
    lateinit var myAdapter: MyAdapter
    lateinit var myStaggeredGridLayoutManager: StaggeredGridLayoutManager
    private val viewModel: TrendingViewModel by viewModels()

    private var isLast = false
    private val limit = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrendingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getData(limit)

        initViews()
        observeData()

    }

    private fun initViews() = with(binding){
        recyclerView.apply {
            myStaggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            recyclerView.layoutManager = myStaggeredGridLayoutManager
            myAdapter = MyAdapter()
            recyclerView.adapter = myAdapter
        }


        // -------------------------------------- //
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

//            val spanCount = myStaggeredGridLayoutManager.spanCount
////                    아이템의 최하단이 완전히 보이는 가장 밑의 아이템
//            val lastVisibleItems =
//                (recyclerView.layoutManager as StaggeredGridLayoutManager?)!!.findLastVisibleItemPositions(
//                    IntArray(spanCount)
//                )

                // 스크롤이 끝에 도달했는지 확인 1: 하단, -1 상단
                if (!binding.recyclerView.canScrollVertically(1) && viewModel.loadingStateData.value == LoadingState.Success) {
                    Log.e("hjh", "rv스크롤 하단 도착, 데이터 요청")
                    viewModel.getData(limit)
                }
            }
        })
    }

    private fun observeData() {
        viewModel.gifLiveData.observe(viewLifecycleOwner) {
            myAdapter.addItem(it)
        }

        viewModel.loadingStateData.observe(viewLifecycleOwner) {
            when (it) {
                is LoadingState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is LoadingState.Success -> binding.progressBar.visibility = View.GONE
//                is LoadingState.Last -> handleSuccessState(it)
                else -> {}
            }
        }
    }
}