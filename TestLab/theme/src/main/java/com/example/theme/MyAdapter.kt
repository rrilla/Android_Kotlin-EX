package com.example.theme

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.theme.databinding.ItemRecyclerTestBinding


class MyAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val asyncListDiffer: AsyncListDiffer<String> = AsyncListDiffer(this, MyDiffUtilItemCallback<String>())

    private var data = emptyList<String>()

    override fun getItemCount(): Int{
        return data.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyViewHolder(ItemRecyclerTestBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder).binding
//        holder.itemView.setOnClickWithSound { item ->
//            onClickListener.invoke(data[position])
//        }

        binding.textView.text = asyncListDiffer.currentList[position]
    }

//    fun setData(data: List<String>) {
//        val diffUtilCallback = MyDiffUtilCallback(this.data, data)
//        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
//
//        this.data.apply {
//            clear()
//            addAll(books)
//            diffResult.dispatchUpdatesTo(this)
//        }
//    }

    fun setData(data: List<String>) {
        asyncListDiffer.submitList(data)
    }

    inner class MyViewHolder(val binding: ItemRecyclerTestBinding): RecyclerView.ViewHolder(binding.root)
}