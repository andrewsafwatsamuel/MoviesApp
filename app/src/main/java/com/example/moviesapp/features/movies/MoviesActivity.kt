package com.example.moviesapp.features.movies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviesapp.*
import com.example.moviesapp.adapters.CATEGORY_EXTRA
import com.example.moviesapp.adapters.GridAdapter
import com.example.moviesapp.databinding.ActivityPopularMoviesBinding
import com.example.moviesapp.subFeatures.movies.MoviesFragment
import com.example.moviesapp.subFeatures.movies.PaginationScrollListener
import com.example.moviesapp.subFeatures.movies.QueryParameters
import com.example.moviesapp.subFeatures.movies.TopBarFragment

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