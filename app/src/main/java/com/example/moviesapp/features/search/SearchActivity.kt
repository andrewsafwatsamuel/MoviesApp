package com.example.moviesapp.features.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.BaseTextWatcher
import com.example.moviesapp.R
import com.example.moviesapp.features.details.RecentSearchesAdapter
import com.example.moviesapp.subFeatures.movies.*
import kotlinx.android.synthetic.main.activity_search.*

private const val NO_RESULTS = "It seems that there are no movies with that name"

class SearchActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this)[SearchViewModel::class.java] }
    private val fragment by lazy { movies_fragment as MoviesFragment }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()
        if (viewModel.movieList.isEmpty()) retrieveRecents()

        val listAdapter = AdapterFactory(LIST_MOVIE_ADAPTER).create(viewModel.movieList)
        val manager = LinearLayoutManager(this)
        val scrollListener = PaginationScrollListener(
            viewModel.parameterLiveData, this, manager
        ) {
            viewModel.retrieveMovies(fragment.onConnectivityCheck(), it.parameters, it.pageNumber + 1)
        }

        fragment.drawRecycler(manager, listAdapter, scrollListener)

        recent_searches_recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = RecentSearchesAdapter(viewModel.storedMovieNames, this@SearchActivity)
        }

        back_image_view.setOnClickListener { finish() }

        search_edit_text.run {
            addTextChangedListener(onTextChanged())
            setOnClickListener { retrieveRecents() }
        }

        with(viewModel) {
            result.observe(this@SearchActivity, Observer {
                listAdapter.addItems(it.results)
                parameterLiveData.value = QueryParameters(it.pageNumber, it.pageCount, search_edit_text.text.toString())
                if (search_edit_text.text.isEmpty()) retrieveRecents() else hideSearches()
                fragment.run { if (movieList.isEmpty()) onEmptyState(NO_RESULTS) else onNonEmptyState() }
            })
            loading.observe(this@SearchActivity, Observer {
                fragment.run { if (it) onStartLoading() else onFinishLoading() }
            })
        }
    }

    private fun hideSearches() {
        recent_searches_recyclerView.visibility = View.GONE
        movies_container.visibility = View.VISIBLE
    }

    private fun onTextChanged(): BaseTextWatcher = object : BaseTextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) =
            with(viewModel) {
                movieList.clear()
                retrieveMovies(fragment.onConnectivityCheck(), s.toString())
                if (s.isEmpty() || s.isBlank()) retrieveRecents() else hideSearches()
            }
    }

    private fun retrieveRecents() {
        viewModel.retrieveMovieNames()
        showSearches()
    }

    private fun showSearches() {
        recent_searches_recyclerView.visibility = View.VISIBLE
        movies_container.visibility = View.GONE
    }
}