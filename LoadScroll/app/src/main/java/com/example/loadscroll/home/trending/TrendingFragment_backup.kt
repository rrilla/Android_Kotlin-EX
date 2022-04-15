package com.example.loadscroll.home.trending

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.loadscroll.databinding.FragmentTrendingBinding

class TrendingFragment_backup : Fragment() {
//    lateinit var binding: FragmentTrendingBinding
//    lateinit var myAdapter: MyAdapter
//    private val viewModel: TrendingViewModel by viewModels()
//
//    private var isLast = false
//    private val limit = 10
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentTrendingBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        initViews()
//        observeData()
//
//        viewModel.getData(limit)
//    }
//
//    private fun initViews() {
//        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
//        binding.recyclerView.layoutManager = staggeredGridLayoutManager
//
//        myAdapter = MyAdapter(requireContext())
//        binding.recyclerView.adapter = myAdapter
//
//        // -------------------------------------- //
//        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener {
//                v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            var count = 0
//            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
//                count++;
//
//                if(isLast){
//                    binding.progressBar.visibility = View.GONE
//                }else if (count < 10) {
//                    viewModel.getData(limit);
//                }
//            }
//        })
//    }
//
//    private fun observeData() {
//        viewModel.gifLiveData.observe(viewLifecycleOwner) {
//            viewModel.countLiveData.value?.let {
//                    it1 -> myAdapter.addItem(it, it1)
//            }
//        }
//
//        viewModel.countLiveData.observe(viewLifecycleOwner) {
//            if(it > limit) isLast = true
//        }
//    }
}