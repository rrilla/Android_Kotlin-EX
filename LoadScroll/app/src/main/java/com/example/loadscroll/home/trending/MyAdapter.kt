package com.example.loadscroll.home.trending

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loadscroll.data.model.Data
import com.example.loadscroll.data.model.GiphyListModel
import com.example.loadscroll.databinding.ItemRecyclerviewBinding

class MyAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val items = mutableListOf<Data>()
    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener {
        fun onChange(v: View, position: Int, isChecked: Boolean)
    }

    fun setItemClickListener(onItemClickListener: (v: View, position: Int, isChecked: Boolean)->Unit) {
        this.itemClickListener = object: OnItemClickListener {
            override fun onChange(v: View, position: Int, isChecked: Boolean) {
                onItemClickListener(v, position, isChecked)
            }
        }
    }

    override fun getItemCount(): Int{
        return items.size
    }

    fun addItem(data: GiphyListModel){
        items.addAll(data.data)
        val count = data.pagination.count
        //특정 위치에 아이템이 새로 삽입시 업데이트
        notifyItemRangeInserted(items.size-count, count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder).binding

        binding.switch1.isChecked = items[position].images.fixed_width.isFavorite
        binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            itemClickListener.onChange(buttonView, position, isChecked)
        }
        binding.imageView.minimumHeight = items[position].images.fixed_width.height.toInt()
        Glide.with(binding.root)
            .load(items[position].images.fixed_width.url)
            .into(binding.imageView)
    }

    class MyViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)
}