package com.example.mvvm_ex2_githubapi

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvm_ex2_githubapi.databinding.ItemGithubRepositoryBinding
import com.example.mvvm_ex2_githubapi.data.remote.model.GithubRepositoryModel

class GithubRepositoryItemHolder(private val binding: ItemGithubRepositoryBinding, listener: GithubRepositoryAdapter.OnGithubRepositoryClickListener?) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            listener?.onItemClick(adapterPosition)
        }
    }

    fun bind(model: GithubRepositoryModel) {
        model.run {
            Glide.with(binding.root)
                .load(owner.avatarUrl)
                .into(binding.avatarView)
            binding.fullNameView.text = fullName
            binding.descriptionView.text = description
            binding.starCountView.text = "Stars : $stargazersCount"
        }
    }
}
