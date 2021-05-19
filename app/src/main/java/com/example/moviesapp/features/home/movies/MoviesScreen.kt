package com.example.moviesapp.features.home.movies

import android.content.Context
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.*
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.Movie
import com.example.moviesapp.R
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

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) = when (event) {
        Lifecycle.Event.ON_CREATE -> doOnStart()
        Lifecycle.Event.ON_DESTROY -> doOnDestroy()
        else -> Unit
    }

    private val viewModel by lazy {
        ViewModelProvider(
            viewModelStoreOwner,
            MoviesViewModelFactory(category)
        )[MoviesViewModel::class.java]
    }

    private val adapter by lazy { MoviesPagingAdapter(::doOnItemClicked) }

    private val loadStateListener: (CombinedLoadStates) -> Unit = { drawEmptyState(it.refresh) }

    private fun doOnItemClicked(movie: Movie) {
        Toast.makeText(context, movie.title, Toast.LENGTH_LONG).show()
    }

    private fun doOnStart() {
        drawMoviesRecycler()
        observeOnViewModel()
        binding.moviesErrorLayout.retryButton.setOnClickListener { adapter.retry() }
    }

    private fun drawMoviesRecycler() = with(adapter) {
        binding.moviesRecyclerView.adapter =
            withLoadStateHeaderAndFooter(createStateAdapter(), createStateAdapter())
        addLoadStateListener(loadStateListener)
    }

    private fun createStateAdapter() = StateAdapter { adapter.retry() }

    private fun observeOnViewModel() = lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
        viewModel.movieFlow.collectLatest { adapter.submitData(it) }
    }

    private fun drawEmptyState(state: LoadState) = with(binding) {
        moviesProgressBar.isVisible = (adapter.itemCount == 0) and (state is LoadState.Loading)
        val isErrorState = state is LoadState.Error
        val emptyError = (adapter.itemCount == 0) and isErrorState
        moviesErrorLayout.root.isVisible = emptyError
        emptyMoviesImageView.isVisible = emptyError
        if (state is LoadState.Error) moviesErrorLayout.errorTextView.text =
            state.error.message ?: context.getString(R.string.unknown_eror)
    }

    private fun doOnDestroy() {
        adapter.removeLoadStateListener(loadStateListener)
    }

}