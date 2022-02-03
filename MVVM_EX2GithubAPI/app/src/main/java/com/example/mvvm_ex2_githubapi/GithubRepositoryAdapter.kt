package com.example.mvvm_ex2_githubapi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_ex2_githubapi.databinding.ItemGithubRepositoryBinding
import com.example.mvvm_ex2_githubapi.data.remote.model.GithubRepositoryModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class GithubRepositoryAdapter(private var repositories: List<GithubRepositoryModel>) : RecyclerView.Adapter<GithubRepositoryItemHolder>() {
    interface OnGithubRepositoryClickListener {
        fun onItemClick(position: Int)
    }

    var listener: OnGithubRepositoryClickListener? = null

    override fun getItemCount(): Int = repositories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubRepositoryItemHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_github_repository, parent, false)
        val layoutInflater = LayoutInflater.from(parent.context)
        return GithubRepositoryItemHolder(ItemGithubRepositoryBinding.inflate(layoutInflater), listener)
    }

    override fun onBindViewHolder(holder: GithubRepositoryItemHolder, position: Int) {
        holder.bind(repositories[position])
    }

    fun update(updated : List<GithubRepositoryModel>) {
        CoroutineScope(Dispatchers.Main).launch {
            val diffResult = async(Dispatchers.IO) {
                getDiffResult(updated)
            }
            repositories = updated
            diffResult.await().dispatchUpdatesTo(this@GithubRepositoryAdapter) } }

    private fun getDiffResult(updated: List<GithubRepositoryModel>): DiffUtil.DiffResult {
        val diffCallback = GithubRepositoryDiffCallback(repositories, updated)
        return DiffUtil.calculateDiff(diffCallback)
    }

    fun getItem(position: Int) = repositories[position]
}
