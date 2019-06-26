package com.example.moviesapp.features.search

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MovieResponse
import com.example.moviesapp.R
import com.example.moviesapp.subFeatures.movies.*
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private val viewModel
            by lazy { ViewModelProviders.of(this)[SearchViewModel::class.java] }
    private val moviesFragment
            by lazy { movies_fragment as MoviesFragment }
    private val recyclerView
            by lazy { moviesFragment.view?.findViewById<RecyclerView>(R.id.movies_recycler_view)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()

        val adapter = AdapterFactory(viewModel.movieList).create(LIST_MOVIE_ADAPTER)

        val layoutManager = LinearLayoutManager(this)

        viewModel
            .also { it.retrieveMovieNames() }
            .takeUnless { it.movieList.isEmpty() }
            ?.also { previous_searches_layout.visibility = View.GONE }


        search_image_view.setOnClickListener {
            searchOnClick(adapter, search_edit_text.text.toString())
        }

        search_edit_text.setOnClickListener {
            viewModel.retrieveMovieNames()
            previous_searches_layout.visibility = View.VISIBLE
        }

        previous_search_results_list_view.emptyView = previous_searches_empty_text_view
            .also { it.text = resources.getString(R.string.empty_previous_searches) }

        drawRecycler(layoutManager, adapter)

        viewModel.storedMovieNames.observe(this, Observer {
            previousSearchesAdapter(it)
            previous_search_results_list_view.onItemClickListener = onClick(it, adapter)
        })

        viewModel.loading.observe(this, Observer {
            if (it) onStartLoading(moviesFragment) else moviesFragment.onFinishLoading()
        })

        viewModel.emptyResult.observe(this, Observer {
            if (it.isBlank()) moviesFragment.onNonEmptyState() else moviesFragment.onEmptyState(it)
        })
    }

    private fun searchOnClick(movieAdapter: MovieAdapter<*>, name: String) {
        viewModel.movieList.clear()
        viewModel.retrieveMovies(1, name) { onResultRetrieved(movieAdapter, it, name) }
    }

    private fun onResultRetrieved(movieAdapter: MovieAdapter<*>, response: MovieResponse, name: String) {
        viewModel.movieList.clear()
        movieAdapter.addItems(response.results)
        viewModel.parameterLiveData.value =
            QueryParameters(response.pageNumber + 1, response.pageCount, name)
    }

    private fun drawRecycler(
        recyclerLayoutManager: LinearLayoutManager,
        movieAdapter: MovieAdapter<*>
    ) = with(recyclerView) {
        layoutManager = recyclerLayoutManager
        adapter = movieAdapter
        addOnScrollListener(setPagination(recyclerLayoutManager, movieAdapter))
    }

    private fun onClick(
        names: List<String>, adapter: MovieAdapter<*>
    ): AdapterView.OnItemClickListener? =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            searchOnClick(adapter, names[position])
            search_edit_text.setText(names[position])
        }

    private fun setPagination(layoutManager: LinearLayoutManager, adapter: MovieAdapter<*>) =
        PaginationScrollListener(
            viewModel.parameterLiveData, this, layoutManager
        ) { searchOnScroll(it, adapter) }

    private fun searchOnScroll(
        parameters: QueryParameters<String>, movieAdapter: MovieAdapter<*>
    ) = viewModel.retrieveMovies(parameters.pageNumber, parameters.parameters) {
        movieAdapter.addItems(it.results)
        viewModel.emptyResult.value = ""
        viewModel.parameterLiveData.value = QueryParameters(
            parameters.pageNumber + 1,
            parameters.pageCount, parameters.parameters
        )
    }

    private fun previousSearchesAdapter(searches: List<String>) =
        with(previous_search_results_list_view) {
            adapter = ArrayAdapter<String>(
                this@SearchActivity,
                R.layout.previous_search_result_item,
                R.id.search_item, searches
            )
        }

    private fun onStartLoading(moviesFragment: MoviesFragment) {
        previous_searches_layout.visibility = View.GONE
        hideKeyboard()
        moviesFragment.onStartLoading()
    }

    private fun hideKeyboard() = this.getSystemService(Activity.INPUT_METHOD_SERVICE)
        .let { it as InputMethodManager }
        .also { it.hideSoftInputFromWindow(currentFocus?.windowToken, 0) }
}