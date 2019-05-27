package com.example.moviesapp.features.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MovieResponse

import kotlinx.android.synthetic.main.activity_search.*

private var pageNumber = 1
private var pageCount = 0
private var totalItemCount = 0
private var movieName = ""
private const val VISIBLE_THRESHOLD = 1

class SearchActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this)[SearchViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.moviesapp.R.layout.activity_search)
        supportActionBar?.hide()

        val movieAdapter = MovieAdapter(viewModel.movieList)

        val layoutManager = LinearLayoutManager(this)

        result_recycler_view
            .also { it.layoutManager = layoutManager }
            .also { it.adapter = movieAdapter }
            .also { it.addOnScrollListener(OnScrollListener(layoutManager, viewModel, movieAdapter)) }

        search_image_view.setOnClickListener {
            movieName = search_edit_text.text.toString()
            pageNumber = 1
            viewModel.retrieveMovies({ response ->
                onResultRetrieved(movieAdapter, response)
            }, pageNumber, movieName)
        }

        viewModel.loading.observe(this, Observer {
            if (it) onStartLoading() else onFinishLoading()
        })

        viewModel.emptyResult.observe(this, Observer {
            if (it.isBlank()) onNonEmptyState() else onEmptyState(it)
        })
    }

    private fun onResultRetrieved(movieAdapter: MovieAdapter, movieResponse: MovieResponse) {
        viewModel.movieList.clear()
        movieAdapter.addItems(movieResponse.results)
        pageCount = movieResponse.pageCount
        totalItemCount = movieResponse.resultCount
    }

    private fun onStartLoading() {
        onNonEmptyState()
        search_progress_bar.visibility = View.VISIBLE
    }

    private fun onNonEmptyState() {
        empty_list_imageView.visibility = View.GONE
        empty_list_text_view.visibility = View.GONE
    }

    private fun onFinishLoading() {
        search_progress_bar.visibility = View.GONE
    }

    private fun onEmptyState(emptyStateText: String) {
        empty_list_imageView.visibility = View.VISIBLE
        empty_list_text_view.visibility = View.VISIBLE
        empty_list_text_view.text = emptyStateText
    }

    class OnScrollListener(
        private val layoutManager: LinearLayoutManager,
        private val viewModel: SearchViewModel,
        private val movieAdapter: MovieAdapter
    ) : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val totalItemCount: Int = layoutManager.itemCount
            val lastItemPosition: Int = layoutManager.findLastCompletelyVisibleItemPosition()

            if (dy > 0) totalItemCount
                .takeIf { it == (lastItemPosition + VISIBLE_THRESHOLD) }
                .also { paginate() }
        }

        private fun paginate() = pageNumber
            .takeIf { it <= pageCount }
            .also { pageNumber++ }
            .also { loadData() }

        private fun loadData() =
            viewModel.retrieveMovies({ movieAdapter.addItems(it.results) }, pageNumber, movieName)
    }
}