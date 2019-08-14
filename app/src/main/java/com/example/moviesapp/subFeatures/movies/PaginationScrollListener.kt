package com.example.moviesapp.subFeatures.movies

import android.app.Activity
import android.widget.AbsListView
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.hideKeyboard
import com.example.nullLayoutManager
import com.example.nullLifeCycleOwner
import com.example.nullQueryParameters
import com.example.nullRetrieve

data class QueryParameters<I>(
    val pageNumber: Int,
    val pageCount: Int,
    val parameters: I
)

class PaginationScrollListener<I> private constructor(
    private val queryParameters: MutableLiveData<QueryParameters<I>>,
    private val lifecycleOwner: LifecycleOwner,
    private var layoutManager: LinearLayoutManager?,
    private var loading: Boolean,
    private var scrolling: Boolean,
    private var activity: Activity?,
    private val retrieve: (QueryParameters<I>) -> Unit
) : RecyclerView.OnScrollListener(), LifecycleObserver {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    data class Builder<I>(
        private var queryParameters: MutableLiveData<QueryParameters<I>>? = null,
        private var lifecycleOwner: LifecycleOwner? = null,
        private var layoutManager: LinearLayoutManager? = null,
        private var loading: Boolean = false,
        private var scrolling: Boolean = false,
        private var activity: Activity? = null,
        private var retrieve: ((QueryParameters<I>) -> Unit)? = null
    ) {
        fun queryParameters(queryParameters: MutableLiveData<QueryParameters<I>>) =
            apply { this.queryParameters = queryParameters }

        fun lifecycleOwner(lifecycleOwner: LifecycleOwner) = apply { this.lifecycleOwner = lifecycleOwner }

        fun layoutManager(layoutManager: LinearLayoutManager) = apply { this.layoutManager = layoutManager }

        fun activity(activity: Activity) = apply { this.activity = activity }

        fun retrieve(retrieve: (QueryParameters<I>) -> Unit) = apply { this.retrieve = retrieve }

        fun build(): PaginationScrollListener<I> = with(this) {
            if (queryParameters == null) nullQueryParameters()
            if (lifecycleOwner == null) nullLifeCycleOwner()
            if (layoutManager == null) nullLayoutManager()
            if (retrieve == null) nullRetrieve()
        }.run { start() }

        private fun start() = PaginationScrollListener(
            queryParameters!!, lifecycleOwner!!, layoutManager!!, loading, scrolling, activity, retrieve!!
        )
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        scrolling = newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (scrolling) activity?.let { hideKeyboard(it) }
        val currentItems = layoutManager!!.childCount
        val totalItems = layoutManager!!.itemCount
        val scrollOutItems = layoutManager!!.findFirstVisibleItemPosition()

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

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun clear() {
        activity = null
        layoutManager = null
    }
}