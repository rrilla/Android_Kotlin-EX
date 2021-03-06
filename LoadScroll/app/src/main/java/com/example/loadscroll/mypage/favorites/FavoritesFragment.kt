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

        //  RecyclerView??? ????????? ?????? ?????? - xml??? NestedScrollView ??????
//        recyclerView.addOnScrollListener(RvListener())

        //  NestedScrollView??? ????????? ?????? ??????
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
                    //  ?????? ????????? ????????? ?????? ?????? UI
                }
                else -> {}
            }
        }
    }

    inner class RvListener: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

//            val spanCount = myStaggeredGridLayoutManager.spanCount
////                    ???????????? ???????????? ????????? ????????? ?????? ?????? ?????????
//            val lastVisibleItems =
//                (recyclerView.layoutManager as StaggeredGridLayoutManager?)!!.findLastVisibleItemPositions(
//                    IntArray(spanCount)
//                )

            // ???????????? ?????? ??????????????? ?????? 1: ??????, -1 ??????
            if (!binding.recyclerView.canScrollVertically(1)) {
                if(viewModel.loadingStateData.value == LoadingState.Success){
                    Log.e("hjh", "rv????????? ?????? ??????, ????????? ??????")
//                    viewModel.getData()
                }else{
                    //  ????????? ????????? ui ?????????
                    Log.e("hjh", "????????? ?????????")
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
            //  ???????????? ?????? ??????????????? ??????
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                if(viewModel.loadingStateData.value == LoadingState.Success){
                    Log.e("hjh", "nv????????? ?????? ??????, ????????? ??????")
//                    viewModel.getData()
                }else{
                    //  ????????? ????????? ui ?????????
                    Log.e("hjh", "????????? ?????????")
                }
            }
        }
    }
}