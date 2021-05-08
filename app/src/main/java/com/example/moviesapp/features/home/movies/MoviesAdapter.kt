package com.example.moviesapp.features.home.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.Movie
import com.example.moviesapp.POSTER_SIZE
import com.example.moviesapp.R
import com.example.moviesapp.databinding.GridItemBinding
import com.example.moviesapp.setImageUrl

private fun createUtils() = object : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
}

class MovieViewHolder(
    private val binding: GridItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Movie) {
        binding.gridMovieBoaster.setImageUrl(POSTER_SIZE,item.poster)
    }
}

class MoviesPagingAdapter(
    private val doOnClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieViewHolder>(createUtils()) {

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = with(holder) {
        val item = getItem(position) ?: return@with
        bind(item)
        itemView.setOnClickListener { doOnClick(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LayoutInflater
        .from(parent.context)
        .inflate(R.layout.grid_item, parent, false)
        .let(GridItemBinding::bind)
        .let(::MovieViewHolder)
}