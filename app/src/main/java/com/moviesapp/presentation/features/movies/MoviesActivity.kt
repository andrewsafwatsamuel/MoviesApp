package com.moviesapp.presentation.features.movies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.moviesapp.presentation.R
import com.moviesapp.presentation.adapters.CATEGORY_EXTRA
import com.moviesapp.presentation.adapters.GridAdapter
import com.moviesapp.presentation.databinding.ActivityPopularMoviesBinding
import com.moviesapp.presentation.onConnectivityCheck
import com.moviesapp.presentation.pageCount
import com.moviesapp.presentation.reload
import com.moviesapp.presentation.setErrorState
import com.moviesapp.presentation.subFeatures.movies.MoviesFragment
import com.moviesapp.presentation.subFeatures.movies.PaginationScrollListener
import com.moviesapp.presentation.subFeatures.movies.QueryParameters
import com.moviesapp.presentation.subFeatures.movies.TopBarFragment

class MoviesActivity : AppCompatActivity() {

    private var binding: ActivityPopularMoviesBinding? = null

    private val viewModel by lazy {
        ViewModelProviders.of(this)[MoviesViewModel::class.java]
    }

    private val fragment by lazy { supportFragmentManager.findFragmentById(R.id.popular_fragment) as MoviesFragment }

    private val category by lazy { intent.getStringExtra(CATEGORY_EXTRA).orEmpty() }

    private val topBarFragment by lazy { supportFragmentManager.findFragmentById(R.id.movies_top_bar_fragment) as TopBarFragment }
    private val popularSwipeRefresh
        get() = binding?.popularSwipeRefresh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopularMoviesBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        viewModel.run {
            if (movies.isEmpty()) binding?.emptyStateLayout?.let {
                getMovies(onConnectivityCheck(it), category)
            }
        }

        binding?.emptyStateLayout?.let {
            reload(it) { viewModel.getMovies(it, category) }
        }

        val layoutManager = GridLayoutManager(this, 3)

        val adapter = GridAdapter(viewModel.movies)

        val scrollListener = PaginationScrollListener.Builder<Unit>()
            .queryParameters(viewModel.parameters)
            .lifecycleOwner(this)
            .layoutManager(layoutManager)
            .retrieve { params ->
                binding?.emptyStateLayout?.let { emptyStateLayoutBinding ->
                    viewModel.getMovies(
                        onConnectivityCheck(emptyStateLayoutBinding),
                        category,
                        params.pageNumber + 1
                    )
                }
            }.build()

        with(viewModel) {
            loading.observe(this@MoviesActivity) {
                if (it) fragment.onStartLoading() else finishLoading()
            }
            result.observe(this@MoviesActivity) {
                adapter.addItems(it.results)
                parameters.value = QueryParameters(it.pageNumber, pageCount(it.pageCount), Unit)
                disposable.clear()
            }
            errorLiveData.observe(this@MoviesActivity) {
                binding?.emptyStateLayout?.setErrorState(it)
            }
        }

        fragment.drawRecycler(layoutManager, adapter, scrollListener)

        topBarFragment
            .apply { activityTitle(category) }
            .backButton().setOnClickListener {
                if (layoutManager.findFirstVisibleItemPosition() != 0) fragment.getToTop() else finish()
            }

        popularSwipeRefresh?.setOnRefreshListener { swipeRefresh() }
    }

    private fun finishLoading() {
        fragment.onFinishLoading()
        popularSwipeRefresh?.isRefreshing = false
    }

    private fun swipeRefresh() = binding?.emptyStateLayout?.let {
        onConnectivityCheck(it).also { isConnected ->
            viewModel.movies.clear()
            viewModel.getMovies(isConnected, category)
            if (!isConnected) popularSwipeRefresh?.isRefreshing = false
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}