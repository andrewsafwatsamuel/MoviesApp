package com.example.moviesapp.subFeatures.movies

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class QueryParameters<I>(
    val pageNumber: Int,
    val pageCount: Int,
    val parameters:I
)

class PaginationScrollListener<I>(
    private val queryParameters: MutableLiveData<QueryParameters<I>>,
    private val lifecycleOwner: LifecycleOwner,
    private val layoutManager: LinearLayoutManager,
    private var loading: Boolean = false,
    private val retrieve: (QueryParameters<I>) -> Unit
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val currentItems = layoutManager.childCount
        val totalItems = layoutManager.itemCount
        val scrollOutItems = layoutManager.findFirstVisibleItemPosition()
        if (!loading && (currentItems + scrollOutItems == totalItems)) {
            queryParameters.observe(lifecycleOwner, Observer { paginate(it) })
        }
    }

    private fun paginate(
        parameters: QueryParameters<I>
    ) {
        if (parameters.pageNumber <= parameters.pageCount) {
            loadData(parameters)
        }
    }

    private fun loadData(parameters: QueryParameters<I>) {
        loading = true
        retrieve(parameters)
        loading = false
    }
}