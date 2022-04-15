package com.example.loadscroll.home.trending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loadscroll.data.model.Gif
import com.example.loadscroll.databinding.ItemRecyclerviewBinding
import com.example.loadscroll.databinding.ItemRecyclerviewLoadingBinding

class MyAdapter2 : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val items = mutableListOf<Gif?>()

    // 아이템뷰에 게시물이 들어가는 경우
    inner class GifViewHolder(private val binding: ItemRecyclerviewBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: Gif){
            binding.imageView.minimumHeight = data.height.toInt()
            Glide.with(binding.root)
                .load(data.url)
                .into(binding.imageView)
//            binding.switch1.text = data.created.substring(0, 10)
        }
    }

    // 아이템뷰에 프로그레스바가 들어가는 경우
    inner class LoadingViewHolder(private val binding: ItemRecyclerviewLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    // 뷰의 타입을 정해주는 곳이다.
    override fun getItemViewType(position: Int): Int {
        // 게시물과 프로그레스바 아이템뷰를 구분할 기준이 필요하다.
        return when (items[position] == null) {
            true -> VIEW_TYPE_LOADING
            false -> VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is GifViewHolder){
            items[position]?.let { holder.bind(it) }
        }else{

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRecyclerviewBinding.inflate(layoutInflater, parent, false)
                GifViewHolder(binding)
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRecyclerviewLoadingBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(data: MutableList<Gif>){
        items.addAll(data)
        items.add(null)
    }

    fun deleteLoading(){
        items.removeAt(items.lastIndex) // 로딩이 완료되면 프로그레스바를 지움
    }
}