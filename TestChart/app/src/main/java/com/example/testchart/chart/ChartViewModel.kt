package com.example.testchart.chart

import androidx.lifecycle.ViewModel
import com.example.testchart.common.StatsByTargetAdapter
import com.example.testchart.data.StatsTargetAll

class ChartViewModel : ViewModel() {

    private val mAdapter = StatsByTargetAdapter()

    fun getAdapter(): StatsByTargetAdapter {
        return mAdapter
    }

    fun setData(staData: StatsTargetAll) {
        mAdapter.setList(staData.data)
    }
}