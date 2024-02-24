package com.moviesapp.presentation.features.search

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moviesapp.presentation.BaseTextWatcher
import com.moviesapp.presentation.R
import com.moviesapp.presentation.adapters.ACTION_SEARCH
import com.moviesapp.presentation.adapters.EXTRA_KEY
import com.moviesapp.presentation.adapters.ListAdapter
import com.moviesapp.presentation.adapters.RecentSearchesAdapter
import com.moviesapp.presentation.databinding.ActivitySearchBinding
import com.moviesapp.presentation.hideKeyboard
import com.moviesapp.presentation.onConnectivityCheck
import com.moviesapp.presentation.pageCount
import com.moviesapp.presentation.reload
import com.moviesapp.presentation.setErrorState
import com.moviesapp.presentation.subFeatures.movies.MoviesFragment
import com.moviesapp.presentation.subFeatures.movies.PaginationScrollListener
import com.moviesapp.presentation.subFeatures.movies.QueryParameters
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

private const val NO_RESULTS = "It seems that there are no movies with that name"

class SearchActivity : AppCompatActivity() {

    private var binding: ActivitySearchBinding? = null

    private val viewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .let { ViewModelProvider(this, it)[SearchViewModel::class.java] }
    }

    private val fragment by lazy { supportFragmentManager.findFragmentById(R.id.movies_fragment) as MoviesFragment }
    private val searchReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            startSearch(intent.getStringExtra(EXTRA_KEY) ?: "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        if (viewModel.movieList.isEmpty()) retrieveRecents()

        val listAdapter = ListAdapter(viewModel.movieList)

        val manager = LinearLayoutManager(this)
        binding?.apply {
            val scrollListener = PaginationScrollListener.Builder<String>()
                .queryParameters(viewModel.parameterLiveData)
                .lifecycleOwner(this@SearchActivity)
                .layoutManager(manager)
                .retrieve {
                    viewModel.retrieveMovies(
                        onConnectivityCheck(emptyStateLayout),
                        it.parameters,
                        it.pageNumber + 1
                    )
                }
                .activity(this@SearchActivity)
                .build()

            reload(emptyStateLayout) {
                viewModel.retrieveMovies(it, searchEditText.text?.toString().orEmpty())
            }
            fragment.drawRecycler(manager, listAdapter, scrollListener)
            recentSearchesRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@SearchActivity)
                adapter = RecentSearchesAdapter(viewModel.storedMovieNames, this@SearchActivity)
            }
            backImageView.setOnClickListener { finish() }
            searchEditText.run {
                addTextChangedListener(onTextChanged())
                setOnClickListener { retrieveRecents() }
            }
        }
        observeOnViewModel(listAdapter)
        registerReceiver(searchReceiver, IntentFilter(ACTION_SEARCH))
    }

    private fun startSearch(searchKey: String) =
        binding?.searchEditText?.setText(searchKey)
            .also { hideKeyboard(this@SearchActivity) }

    private fun retrieveRecents() {
        viewModel.retrieveMovieNames()
        showSearches()
    }

    private fun showSearches() {
        binding?.apply {
            recentSearchesRecyclerView.visibility = View.VISIBLE
            moviesContainer.visibility = View.GONE
        }
    }

    private fun onTextChanged(): BaseTextWatcher = object : BaseTextWatcher {
        override fun afterTextChanged(s: Editable) = viewModel.searchSubject.onNext(s)
    }

    private fun retrieveMovies(string: String) = with(viewModel) {
        movieList.clear()
        binding?.emptyStateLayout?.let { retrieveMovies(onConnectivityCheck(it), string) }
    }

    private fun observeOnViewModel(adapter: ListAdapter) = with(viewModel) {
        result.observe(this@SearchActivity) {
            adapter.addItems(it.results)
            setParameters(it.pageNumber, it.pageCount)
            if (binding?.searchEditText?.text?.isEmpty() == true) retrieveRecents() else hideSearches()
            fragment.run { if (movieList.isEmpty()) onEmptyState(NO_RESULTS) else onNonEmptyState() }
        }
        loading.observe(this@SearchActivity) {
            fragment.run { if (it) onStartLoading() else onFinishLoading() }
        }
        viewModel.errorLiveData.observe(this@SearchActivity){
            binding?.emptyStateLayout?.setErrorState(it)
        }
        searchSubject.subscribeOn(AndroidSchedulers.mainThread())
            .debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe { updateSearch(it) }/*,Throwable::printStackTrace)*/
            .also { disposables.add(it) }
    }

    private fun setParameters(
        pageNumber: Int,
        pageCount: Int
    ) = QueryParameters(
        pageNumber,
        pageCount(pageCount),
        binding?.searchEditText?.text?.toString().orEmpty()
    )
        .let { viewModel.updateParametersLiveData(it) }

    private fun updateSearch(s: CharSequence) = retrieveMovies(s.toString())
        .also { if (s.isEmpty() || s.isBlank()) retrieveRecents() else hideSearches() }

    private fun hideSearches() {
        binding?.apply {
            recentSearchesRecyclerView.visibility = View.GONE
            moviesContainer.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        binding = null
        unregisterReceiver(searchReceiver)
        super.onDestroy()
    }
}