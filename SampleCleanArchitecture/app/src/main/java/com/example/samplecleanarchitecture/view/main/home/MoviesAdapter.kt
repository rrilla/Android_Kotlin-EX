package com.example.samplecleanarchitecture.view.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.samplecleanarchitecture.databinding.ItemRowMovieBinding
import javax.inject.Inject
import kotlin.properties.Delegates

class MoviesAdapter
@Inject constructor() : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    internal var collection: List<HomeUiState.UiMovie> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    internal var clickListener: (HomeUiState.UiMovie) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemRowMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(collection[position], clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(val binding: ItemRowMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieView: HomeUiState.UiMovie, clickListener: (HomeUiState.UiMovie) -> Unit) {
            binding.moviePoster.apply {
                Glide.with(this.context.applicationContext)
                    .load(movieView.poster)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
            itemView.setOnClickListener {
                clickListener(
                    movieView
                )
            }
        }
    }
}