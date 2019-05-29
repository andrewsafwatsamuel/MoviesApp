package com.example.moviesapp.features.search

import android.widget.AbsListView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class QueryParameters(
    val pageNumber: Int,
    val pageCount: Int,
    val movieName: String
)

data class UiParameters(
    val layoutManager: LinearLayoutManager,
    val movieAdapter: MovieAdapter
)

class PaginationScrollListener(
    private val retrieve: (QueryParameters) -> Unit,
    private val queryParameters: MutableLiveData<QueryParameters>,
    private val lifecycleOwner: LifecycleOwner,
    private val uiParameters: UiParameters,
    private var scrolling: Boolean = false,
    private var loading: Boolean = false
) : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        scrolling = newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val currentItems = uiParameters.layoutManager.childCount
        val totalItems = uiParameters.layoutManager.itemCount
        val scrollOutItems = uiParameters.layoutManager.findFirstVisibleItemPosition()
        if (!loading && scrolling && (currentItems + scrollOutItems == totalItems)) {
            queryParameters.observe(lifecycleOwner, Observer { paginate(it) })
        }
    }

    private fun paginate(
        parameters: QueryParameters
    ) {
        if (parameters.pageNumber <= parameters.pageCount) {
            loadData(parameters)
        }
    }

    private fun loadData(parameters: QueryParameters) {
        loading = true
        retrieve(parameters)
        loading = false
    }
}