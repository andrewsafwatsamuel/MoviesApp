package com.example.moviesapp.subFeatures.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.R
import com.example.moviesapp.adapters.MovieAdapter
import com.example.moviesapp.checkConnectivity
import com.example.moviesapp.features.details.DetailsActivity
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.no_internet_connection.*

class MoviesFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DetailsStarter(activity!!, DetailsActivity::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_movies, container, false)!!

    fun onStartLoading() {
        onNonEmptyState()
        movies_progress_bar.visibility = View.VISIBLE
    }

    fun onFinishLoading() {
        movies_progress_bar.visibility = View.GONE

    }

    fun onEmptyState(emptyStateText: String) {
        movies_recycler_view.visibility = View.GONE
        empty_movies_text_view.visibility = View.VISIBLE
        empty_movies_text_view.text = emptyStateText
    }

    fun onConnectivityCheck(): Boolean =
        checkConnectivity(activity!!)
            .also { if (!it) notConnected() else onNonEmptyState() }

    fun onNonEmptyState() {
        empty_movies_text_view.visibility = View.GONE
        movies_recycler_view.visibility = View.VISIBLE
        not_connected_layout.visibility = View.GONE
    }

    private fun notConnected() {
        movies_progress_bar.visibility = View.GONE
        onEmptyState("")
        not_connected_layout.visibility = View.VISIBLE
    }

    fun getToTop() {
        movies_recycler_view.scrollToPosition(0)
    }

    fun drawRecycler(
        manager: LinearLayoutManager,
        movieAdapter: MovieAdapter<*>,
        scrollListener: PaginationScrollListener<*>
    ) = with(movies_recycler_view) {
        layoutManager = manager
        adapter = movieAdapter
        addOnScrollListener(scrollListener)
    }
}