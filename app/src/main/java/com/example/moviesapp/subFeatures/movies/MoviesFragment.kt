package com.example.moviesapp.subFeatures.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.R
import com.example.moviesapp.adapters.MovieAdapter
import com.example.moviesapp.features.details.DetailsFragment
import kotlinx.android.synthetic.main.fragment_movies.*

class MoviesFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DetailsStarter(activity!!, DetailsFragment::class.java)
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

    fun onNonEmptyState() {
        empty_movies_text_view.visibility = View.GONE
        movies_recycler_view.visibility = View.VISIBLE
    }

    fun getToTop() {
        movies_recycler_view.smoothScrollToPosition(0)
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