package com.example.moviesapp.features.popularMovies

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviesapp.R
import com.example.moviesapp.features.search.SearchActivity
import com.example.moviesapp.pageCount
import com.example.moviesapp.subFeatures.movies.*
import kotlinx.android.synthetic.main.activity_popular_movies.*

class PopularMovies : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this)[PopularViewModel::class.java]
    }

    private val fragment by lazy { popular_fragment as MoviesFragment }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_movies)
        supportActionBar?.hide()

        viewModel.getPopularMovies(fragment.onConnectivityCheck())

        val layoutManager = GridLayoutManager(this,3)

        val adapter = AdapterFactory(GRID_MOVIE_ADAPTER).create(viewModel.movies)

        val scrollListener = PaginationScrollListener.Builder<Unit>()
            .queryParameters(viewModel.parameters)
            .lifecycleOwner(this)
            .layoutManager(layoutManager)
            .retrieve { viewModel.getPopularMovies(fragment.onConnectivityCheck(), it.pageNumber + 1) }
            .build()

        with(viewModel) {
            loading.observe(this@PopularMovies, Observer {
                if (it) fragment.onStartLoading() else finishLoading()
            })
            result.observe(this@PopularMovies, Observer {
                adapter.addItems(it.results)
                parameters.value = QueryParameters(it.pageNumber, pageCount(it.pageCount), Unit)
            })
        }

        fragment.drawRecycler(layoutManager, adapter, scrollListener)

        search_activity_button.setOnClickListener { startSearchScreen() }

        first_item_button.setOnClickListener { fragment.getToTop() }

        popular_swipe_refresh.setOnRefreshListener { swipeRefresh() }
    }

    private val searchIntent by lazy { Intent(this, SearchActivity::class.java) }
    private fun startSearchScreen() = startActivity(searchIntent)

    private fun finishLoading() {
        fragment.onFinishLoading()
        popular_swipe_refresh.isRefreshing = false
    }

    private fun swipeRefresh() = fragment.onConnectivityCheck()
        .also { viewModel.movies.clear() }
        .also { viewModel.getPopularMovies(it) }
        .also { if (!it) popular_swipe_refresh.isRefreshing = false }
}