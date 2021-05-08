package com.example.moviesapp.features.movies

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.Movie
import com.example.MovieResponse
import com.example.domain.useCases.Error
import com.example.domain.useCases.Loading
import com.example.domain.useCases.MovieParams
import com.example.domain.useCases.MovieState
import com.example.domain.useCases.Success
import com.example.moviesapp.*
import com.example.moviesapp.databinding.ActivityMoviesBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoviesActivity : AppCompatActivity() {

    private val params by lazy { MovieParams("popular", 1, INIT_LOADING) }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MoviesViewModelFactory(params, checkConnectivity())
        )[MoviesViewModel::class.java]
    }

    private lateinit var binding: ActivityMoviesBinding

    private val adapter by lazy { MoviesPagingAdapter(::doOnItemClicked) }

    private val scrollListener by lazy {
        binding.moviesRecyclerView.createOnScrollListener {
            viewModel.getMovies(
                true,
                params.copy(
                    loadingType = PAGED_LOADING,
                    pageNumber = viewModel.pagingFlow.value.first
                )
            )
        }
    }

    private val loadOnType = mapOf<String, () -> Unit>(
        Pair(INIT_LOADING, ::drawInitialLoading),
        Pair(PAGED_LOADING, ::drawPagedLoading)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.state.observe(this, Observer(::observerOnStates))
        binding.moviesRecyclerView.adapter = adapter
        PagingLifeCycle(this, binding.moviesRecyclerView, scrollListener)
        viewModel.viewModelScope.launch {
            viewModel.pagingFlow.collectLatest { adapter.submitList(it.second) }
        }
        binding.moviesSwipeRefresh.setOnRefreshListener {
            viewModel.getMovies(
                checkConnectivity(),
                params.copy(loadingType = REFRESH_LOADING)
            )
        }
    }

    private fun observerOnStates(state: MovieState) = when (state) {
        is Loading -> onLoading(state.type)
        is Success -> onSuccess(state.response)
        is Error -> onError()
    }

    private fun onLoading(loadingType: String) = with(binding) {
        loadOnType[loadingType]?.invoke()
        moviesSwipeRefresh.isEnabled = false
    }

    private fun onSuccess(data: MovieResponse) = with(binding) {
        viewModel.viewModelScope.launch { viewModel.submitPage(data) }
        hideLoading()
        moviesSwipeRefresh.isEnabled = true
    }

    private fun onError() = with(binding) {
        moviesSwipeRefresh.isEnabled = true
        hideLoading()
    }

    private fun drawInitialLoading() {
        binding.moviesProgressBar.isVisible = true
    }

    private fun drawPagedLoading() = with(binding) {
        pagingProgressBar.isVisible = true
    }

    private fun hideLoading() = with(binding) {
        pagingProgressBar.isVisible = false
        moviesProgressBar.isVisible = false
        moviesSwipeRefresh.isRefreshing = false
    }

    private fun doOnItemClicked(movie: Movie) {
        if (viewModel.state.value is Loading) return
        Toast.makeText(this, movie.title, Toast.LENGTH_LONG).show()
    }
}