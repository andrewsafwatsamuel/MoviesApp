package com.example.moviesapp.features.home.movies

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.example.Movie
import com.example.moviesapp.databinding.LayoutMoviesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoviesScreen(
    private val category: String,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelStoreOwner: ViewModelStoreOwner,
    private val binding: LayoutMoviesBinding,
    private val context: Context = binding.root.context
) : LifecycleEventObserver {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_CREATE) doOnStart()
    }

    private val viewModel by lazy {
        ViewModelProvider(
            viewModelStoreOwner,
            MoviesViewModelFactory(category)
        )[MoviesViewModel::class.java]
    }

    private val adapter by lazy { MoviesPagingAdapter(::doOnItemClicked) }

    private fun doOnItemClicked(movie: Movie) {
        Toast.makeText(context, movie.title, Toast.LENGTH_LONG).show()
    }

    private fun doOnStart() {
        observeOnViewModel()
        binding.moviesRecyclerView.adapter = adapter

    }

    private fun observeOnViewModel() = lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
        viewModel.movieFlow.collectLatest { adapter.submitData(it) }
    }


}