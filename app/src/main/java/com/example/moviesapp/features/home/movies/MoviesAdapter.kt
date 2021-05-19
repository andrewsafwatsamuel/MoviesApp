package com.example.moviesapp.features.home.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.Movie
import com.example.moviesapp.POSTER_SIZE
import com.example.moviesapp.R
import com.example.moviesapp.databinding.GridItemBinding
import com.example.moviesapp.databinding.ItemPagingLoaderBinding
import com.example.moviesapp.setImageUrl

private fun createUtils() = object : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
}

class MovieViewHolder(
    private val binding: GridItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Movie) {
        binding.gridMovieBoaster.setImageUrl(POSTER_SIZE, item.poster)
    }
}

class MoviesPagingAdapter(
    private val doOnClick: (Movie) -> Unit
) : PagingDataAdapter<Movie, MovieViewHolder>(createUtils()) {

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

class LoadingStateHolder(
    private val binding: ItemPagingLoaderBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(retry: () -> Unit, state: LoadState) = with(binding) {
        pagingLoaderProgressBar.isVisible = state is LoadState.Loading
        errorLayout.isVisible = state is LoadState.Error
        if (state is LoadState.Error) drawErrorLayout(state.error.message, retry)
    }

    private fun drawErrorLayout(message: String?, retry: () -> Unit) = with(binding) {
        errorTextView.text = message ?: "Error Has Occurred Please Try Again"
        retryButton.setOnClickListener { retry() }
    }

}

class StateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateHolder>() {

    override fun onBindViewHolder(holder: LoadingStateHolder, loadState: LoadState) =
        holder.bind(retry, loadState)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = parent
        .createView(R.layout.item_paging_loader)
        .let(ItemPagingLoaderBinding::bind)
        .let(::LoadingStateHolder)

    private fun ViewGroup.createView(resource: Int) = LayoutInflater
        .from(context)
        .inflate(resource, this, false)

}