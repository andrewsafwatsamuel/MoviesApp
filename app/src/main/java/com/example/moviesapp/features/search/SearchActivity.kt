package com.example.moviesapp.features.search

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.MovieResponse
import com.example.moviesapp.R
import com.example.moviesapp.features.details.DetailsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

import kotlinx.android.synthetic.main.activity_search.*
import java.io.Serializable
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this)[SearchViewModel::class.java]
    }
    val showMovieDetails: PublishSubject<Serializable> = PublishSubject.create()

    private val resultsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            showMovieDetails.onNext(intent!!.getSerializableExtra(EXTRA_MOVIE))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()

        viewModel
            .also { it.retrieveMovieNames() }
            .takeUnless { it.movieList.isEmpty() }
            ?.also { previous_searches_layout.visibility = View.GONE }

        val movieAdapter = MovieAdapter(viewModel.movieList)
        val layoutManager = LinearLayoutManager(this)
        val uiParameters = UiParameters(layoutManager, viewModel, movieAdapter)
        result_recycler_view
            .also { it.layoutManager = layoutManager }
            .also { it.adapter = movieAdapter }
            .also {
                it.addOnScrollListener(
                    Paginator(
                        viewModel.parameterLiveData, this, uiParameters
                    )
                )
            }

        search_image_view.setOnClickListener {
            searchOnClick(movieAdapter, search_edit_text.text.toString())
        }

        search_edit_text.setOnClickListener {
            viewModel.retrieveMovieNames()
            previous_searches_layout.visibility = View.VISIBLE
        }

        previous_search_results_list_view.emptyView = previous_searches_empty_text_vie
            .also { it.text = resources.getString(R.string.empty_previous_searches) }

        viewModel.storedMovieNames.observe(this, Observer {
            previousSearchesAdapter(it)
            previous_search_results_list_view.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    searchOnClick(movieAdapter, it[position])
                    search_edit_text.setText(it[position])
                }
        })
        viewModel.loading.observe(this, Observer {
            if (it) onStartLoading() else onFinishLoading()
        })

        viewModel.emptyResult.observe(this, Observer {
            if (it.isBlank()) onNonEmptyState() else onEmptyState(it)
        })

        registerReceiver(resultsReceiver, IntentFilter(ACTION_OPEN_DETAILS_SCREEN))

        showMovieDetails.debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe { startForecastScreen(it) }
            .also { viewModel.disposables.addAll() }
    }

    private fun previousSearchesAdapter(searches: List<String>) {
        previous_search_results_list_view.adapter = ArrayAdapter<String>(
            this,
            R.layout.previous_search_result_item,
            R.id.search_item, searches
        )
    }

    private fun searchOnClick(movieAdapter: MovieAdapter, name: String) {
        viewModel.movieList.clear()
        viewModel.retrieveMovies({ response ->
            onResultRetrieved(movieAdapter, response, name)
        }, 1, name)
    }

    private fun onResultRetrieved(movieAdapter: MovieAdapter, response: MovieResponse, name: String) {
        viewModel.movieList.clear()
        movieAdapter.addItems(response.results)
        viewModel.parameterLiveData.value = QueryParameters(response.pageNumber+1, response.pageCount, name)
    }

    private fun onStartLoading() {
        onNonEmptyState()
        hideKeyboard()
        previous_searches_layout.visibility = View.GONE
        search_progress_bar.visibility = View.VISIBLE
    }

    private fun hideKeyboard() = this.getSystemService(Activity.INPUT_METHOD_SERVICE)
        .let { it as InputMethodManager }
        .also { it.hideSoftInputFromWindow(currentFocus?.windowToken, 0) }

    private fun onFinishLoading() {
        search_progress_bar.visibility = View.GONE
        empty_list_text_view.visibility = View.GONE
    }

    private fun onNonEmptyState() {
        empty_list_text_view.visibility = View.GONE
        result_recycler_view.visibility = View.VISIBLE
    }

    private fun onEmptyState(emptyStateText: String) {
        result_recycler_view.visibility = View.GONE
        empty_list_text_view.visibility = View.VISIBLE
        empty_list_text_view.text = emptyStateText
    }

    private fun startForecastScreen(movie: Serializable) {
        Intent(this, DetailsActivity::class.java)
            .putExtra(EXTRA_MOVIE, movie)
            .also { startActivity(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(resultsReceiver)
    }
}