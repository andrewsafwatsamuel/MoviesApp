package com.example.moviesapp.features.popularMovies

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.features.search.SearchActivity
import com.example.moviesapp.subFeatures.movies.MovieAdapter
import com.example.moviesapp.subFeatures.movies.MoviesFragment
import com.example.moviesapp.subFeatures.movies.PaginationScrollListener
import com.example.moviesapp.subFeatures.movies.QueryParameters
import kotlinx.android.synthetic.main.activity_popular_movies.*

class PopularMovies : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this)[PopularViewModel::class.java]
    }

    private val fragment by lazy { popular_fragment as MoviesFragment }

    private val recyclerView by lazy {
        fragment.view?.findViewById<RecyclerView>(R.id.movies_recycler_view)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_movies)
        supportActionBar?.hide()

        viewModel.getPopularMovies(fragment.onConnectivityCheck())

        val layoutManager = LinearLayoutManager(this)

        val adapter = MovieAdapter(viewModel.movies)

        val scrollListener =
            PaginationScrollListener(viewModel.parameters, this, layoutManager) {
                viewModel.getPopularMovies(fragment.onConnectivityCheck(), it.pageNumber + 1)
            }

        with(viewModel) {
            loading.observe(this@PopularMovies, Observer {
                if (it) fragment.onStartLoading() else finishLoading()
            })
            result.observe(this@PopularMovies, Observer {
                adapter.addItems(it.results)
                parameters.value = QueryParameters(it.pageNumber, it.pageCount, Unit)
            })
        }

        drawRecycler(layoutManager, adapter, scrollListener)

        search_activity_button.setOnClickListener { startSearchScreen() }

        first_item_button.setOnClickListener { fragment.getToTop() }

        popular_swipe_refresh.setOnRefreshListener { swipeRefresh() }
    }

    private val searchIntent by lazy { Intent(this, SearchActivity::class.java) }
    private fun startSearchScreen() = startActivity(searchIntent)

    private fun drawRecycler(
        manager: LinearLayoutManager,
        movieAdapter: MovieAdapter,
        scrollListener: PaginationScrollListener<Unit>
    ) = with(recyclerView) {
        layoutManager = manager
        adapter = movieAdapter
        addOnScrollListener(scrollListener)
    }

    private fun finishLoading() {
        fragment.onFinishLoading()
        popular_swipe_refresh.isRefreshing = false
    }

    private fun swipeRefresh() = fragment.onConnectivityCheck()
        .also { viewModel.getPopularMovies(it) }
        .also { if (!it) popular_swipe_refresh.isRefreshing = false }

}