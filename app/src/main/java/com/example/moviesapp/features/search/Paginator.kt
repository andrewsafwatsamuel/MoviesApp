package com.example.moviesapp.features.search

import android.widget.AbsListView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class QueryParameters(
    var pageNumber: Int,
    var pageCount: Int,
    var movieName: String
)

data class UiParameters(
     val layoutManager: LinearLayoutManager,
    val viewModel: SearchViewModel,
    val movieAdapter: MovieAdapter
)

class Paginator(
   private val queryParameters: MutableLiveData<QueryParameters>,
    lifecycleOwner: LifecycleOwner,
    private val uiParameters: UiParameters,
    private var scrolling: Boolean = false,
    private var loading: Boolean = false,
    private var pageCount: Int=0,
    private var pageNumber: Int=0,
    private var movieName: String=""
) : RecyclerView.OnScrollListener() {

    init {
        queryParameters.observe(lifecycleOwner, Observer {
            pageCount=it.pageCount
            pageNumber=it.pageNumber
            movieName=it.movieName
        })
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        scrolling = newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val currentItems = uiParameters.layoutManager.childCount
        val totalItems = uiParameters.layoutManager.itemCount
        val scrollOutItems =uiParameters.layoutManager.findFirstVisibleItemPosition()

        if (!loading && scrolling && (currentItems + scrollOutItems == totalItems)) {
            paginate()
        }
    }

    private fun paginate() = pageNumber
        .takeIf { it <= pageCount }
        .also { pageNumber++ }
        .also { queryParameters.value= QueryParameters(pageNumber,pageCount,movieName)}
        .also { loadData() }

    private fun loadData() = uiParameters.viewModel
        .also { loading = true }
        .also { retrieveMovies(it) }

    private fun retrieveMovies(viewModel: SearchViewModel) =
        viewModel.retrieveMovies({
            uiParameters.movieAdapter.addItems(it.results)
            viewModel.emptyResult.value = ""
            loading = false
        }, pageNumber, movieName)
}