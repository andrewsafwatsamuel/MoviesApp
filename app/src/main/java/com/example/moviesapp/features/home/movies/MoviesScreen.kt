package com.example.moviesapp.features.home.movies

import android.content.Context
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.*
import com.example.Movie
import com.example.MovieResponse
import com.example.domain.useCases.Error
import com.example.domain.useCases.Loading
import com.example.domain.useCases.MovieState
import com.example.domain.useCases.Success
import com.example.moviesapp.PagingLifeCycle
import com.example.moviesapp.createOnScrollListener
import com.example.moviesapp.databinding.LayoutMoviesBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoviesScreen(
    private val category: String,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelStoreOwner: ViewModelStoreOwner,
    private val binding: LayoutMoviesBinding,
    private val context: Context = binding.root.context,
    private var totalPages: Int = 0
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

    private val scrollListener by lazy {
        binding.moviesRecyclerView.createOnScrollListener { viewModel.paginate() }
    }

    private fun MoviesViewModel.paginate() = createParams(PAGED_LOADING)
        .takeUnless { (it.pageNumber >= totalPages) or (state.value is Error) }
        ?.let { getMovies(it) }

    private val loadOnType = mapOf<String, () -> Unit>(
        Pair(INIT_LOADING, ::drawInitialLoading),
        Pair(PAGED_LOADING, ::drawPagedLoading)
    )

    private val adapter by lazy { MoviesPagingAdapter(::doOnItemClicked) }

    private fun doOnItemClicked(movie: Movie) {
        if (viewModel.state.value is Loading) return
        Toast.makeText(context, movie.title, Toast.LENGTH_LONG).show()
    }

    private fun doOnStart() {
        observeOnViewModel()
        drawRecyclerView()
        binding.moviesSwipeRefresh.setOnRefreshListener {
            viewModel.run { getMovies(createParams(REFRESH_LOADING, 1)) }
        }
        binding.errorLayout?.retryButton?.setOnClickListener {
            viewModel.getMovies(viewModel.createParams(INIT_LOADING))
        }
    }

    private fun observeOnViewModel() = with(viewModel) {
        state.observe(lifecycleOwner, Observer(::observerOnStates))
        viewModelScope.launch { viewModel.pagingFlow.collectLatest { adapter.submitList(it.second) } }
    }

    private fun observerOnStates(state: MovieState) = when (state) {
        is Loading -> onLoading(state.type)
        is Success -> onSuccess(state.response)
        is Error -> onError()
    }

    private fun onLoading(loadingType: String) = with(binding) {
        loadOnType[loadingType]?.invoke()
        binding.errorLayout?.root?.isVisible=false
        moviesSwipeRefresh.isEnabled = false
    }

    private fun onSuccess(data: MovieResponse) = with(binding) {
        totalPages = data.pageCount
        viewModel.viewModelScope.launch { viewModel.submitPage(data) }
        hideLoading()
        binding.errorLayout?.root?.isVisible=false
        moviesSwipeRefresh.isEnabled = true
    }

    private fun onError() = with(binding) {
        moviesSwipeRefresh.isEnabled = true
        binding.errorLayout?.root?.isVisible=true
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

    private fun drawRecyclerView() = binding.moviesRecyclerView.let {
        it.adapter = adapter
        PagingLifeCycle(lifecycleOwner, it, scrollListener)
    }

}