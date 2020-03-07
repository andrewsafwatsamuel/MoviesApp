package com.example.moviesapp.features.search

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.*
import com.example.moviesapp.adapters.ACTION_SEARCH
import com.example.moviesapp.adapters.EXTRA_KEY
import com.example.moviesapp.adapters.ListAdapter
import com.example.moviesapp.adapters.RecentSearchesAdapter
import com.example.moviesapp.subFeatures.movies.MoviesFragment
import com.example.moviesapp.subFeatures.movies.PaginationScrollListener
import com.example.moviesapp.subFeatures.movies.QueryParameters
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit

private const val NO_RESULTS = "It seems that there are no movies with that name"

class SearchActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this)[SearchViewModel::class.java] }
    private val fragment by lazy { movies_fragment as MoviesFragment }
    private val searchReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            startSearch(intent.getStringExtra(EXTRA_KEY) ?: "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (viewModel.movieList.isEmpty()) retrieveRecents()

        val listAdapter = ListAdapter(viewModel.movieList)

        val manager = LinearLayoutManager(this)
        val scrollListener = PaginationScrollListener.Builder<String>()
            .queryParameters(viewModel.parameterLiveData)
            .lifecycleOwner(this)
            .layoutManager(manager)
            .retrieve {
                viewModel.retrieveMovies(onConnectivityCheck(), it.parameters, it.pageNumber + 1)
            }
            .activity(this)
            .build()

        reload { viewModel.retrieveMovies(it, search_edit_text.text.toString()) }

        fragment.drawRecycler(manager, listAdapter, scrollListener)

        recent_searches_recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter =
                RecentSearchesAdapter(viewModel.storedMovieNames, this@SearchActivity)
        }

        back_image_view.setOnClickListener { finish() }

        search_edit_text.run {
            addTextChangedListener(onTextChanged())
            setOnClickListener { retrieveRecents() }
        }

        with(viewModel) {
            result.observe(this@SearchActivity, Observer {
                listAdapter.addItems(it.results)
                setParameters(it.pageNumber, it.pageCount)
                if (search_edit_text.text.isEmpty()) retrieveRecents() else hideSearches()
                fragment.run { if (movieList.isEmpty()) onEmptyState(NO_RESULTS) else onNonEmptyState() }
            })
            loading.observe(this@SearchActivity, Observer {
                fragment.run { if (it) onStartLoading() else onFinishLoading() }
            })
            viewModel.errorLiveData.observe(this@SearchActivity, Observer { setErrorState(it) })
            searchSubject.subscribeOn(AndroidSchedulers.mainThread())
                .debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe { updateSearch(it) }/*,Throwable::printStackTrace)*/
                .also { disposables.add(it) }
        }
        registerReceiver(searchReceiver, IntentFilter(ACTION_SEARCH))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(searchReceiver)
    }

    private fun hideSearches() {
        recent_searches_recyclerView.visibility = View.GONE
        movies_container.visibility = View.VISIBLE
    }

    private fun onTextChanged(): BaseTextWatcher = object : BaseTextWatcher {
        override fun afterTextChanged(s: Editable)=viewModel.searchSubject.onNext(s)
    }

    private fun updateSearch(s: CharSequence) = retrieveMovies(s.toString())
        .also { if (s.isEmpty() || s.isBlank()) retrieveRecents() else hideSearches() }

    private fun retrieveMovies(string: String) =
        with(viewModel) {
            movieList.clear()
            retrieveMovies(onConnectivityCheck(), string)
        }

    private fun setParameters(pageNumber: Int, pageCount: Int) = viewModel.parameterLiveData
        .run {
            value =
                QueryParameters(pageNumber, pageCount(pageCount), search_edit_text.text.toString())
        }


    private fun retrieveRecents() {
        viewModel.retrieveMovieNames()
        showSearches()
    }

    private fun showSearches() {
        recent_searches_recyclerView.visibility = View.VISIBLE
        movies_container.visibility = View.GONE
    }

    private fun startSearch(searchKey: String) =
        search_edit_text.setText(searchKey)
            .also { hideKeyboard(this@SearchActivity) }
}