package com.example.test_bottomsheet.test

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_bottomsheet.ModalBottomSheet
import com.example.test_bottomsheet.databinding.ContentBinding
import com.example.test_bottomsheet.databinding.ItemRecyclerviewBinding

class TestModal: ModalBottomSheet<ContentBinding>() {

    override val scale = SMALL

    override val title = "제목"

    override val baseButton: BottomSheetButton
        get() = BottomSheetButton.OneButton("확인") {
            Log.e("hjh", "확인클릭")
        }

    override fun inflateViewBinding(inflater: LayoutInflater): ContentBinding = ContentBinding.inflate(layoutInflater)

    override fun initView() {
        val data = mutableListOf<String>()
        for(i in 1..30){
            data.add("Item $i")
        }
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = MyAdapter(data)
        }
    }
}


//  Test용 RecycleViewAdapter

//항목 View를 가지는 역활
class MyViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)
//항목 구성자. Adapter
class MyAdapter(val datas: MutableList<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    //항목 갯수를 판단하기 위해서 자동 호출
    override fun getItemCount(): Int{
        return datas.size
    }
    //항목의 뷰를 가지는 Holder 를 준비하기 위해 자동 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    //각 항목을 구성하기 위해서 호출
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder).binding
        //뷰에 데이터 출력
        binding.itemData.text= datas[position]
    }
}

