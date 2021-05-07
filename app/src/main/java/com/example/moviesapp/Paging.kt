package com.example.moviesapp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PagingLifeCycle(
    lifecycleOwner: LifecycleOwner,
    private val view: RecyclerView,
    private val scrollListener: RecyclerView.OnScrollListener
) : LifecycleEventObserver {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) = when (event) {
        Lifecycle.Event.ON_START -> view.addOnScrollListener(scrollListener)
        Lifecycle.Event.ON_STOP -> view.removeOnScrollListener(scrollListener)
        else -> Unit
    }

}

fun RecyclerView.createOnScrollListener(loadData: () -> Unit) =
    object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) = (layoutManager as LinearLayoutManager?)?.run {
            paginate(findLastVisibleItemPosition(), itemCount)
        } ?: Unit

        fun paginate(lastVisibleItem: Int, totalItems: Int) {
            if (lastVisibleItem + 1 >= totalItems) loadData()
        }

    }