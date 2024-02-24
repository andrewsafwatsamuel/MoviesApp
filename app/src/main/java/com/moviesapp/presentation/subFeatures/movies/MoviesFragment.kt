package com.moviesapp.presentation.subFeatures.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.moviesapp.presentation.adapters.MovieAdapter
import com.moviesapp.presentation.databinding.FragmentMoviesBinding
import com.moviesapp.presentation.features.details.DetailsActivity

class MoviesFragment : Fragment() {

    private var binding: FragmentMoviesBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DetailsStarter(activity!!, DetailsActivity::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    fun onStartLoading() {
        onNonEmptyState()
        binding?.moviesProgressBar?.visibility = View.VISIBLE
    }

    fun onFinishLoading() {
        binding?.moviesProgressBar?.visibility = View.GONE
    }

    fun onEmptyState(emptyStateText: String) {
        binding?.apply {
            moviesRecyclerView.visibility = View.GONE
            emptyMoviesTextView.visibility = View.VISIBLE
            emptyMoviesTextView.text = emptyStateText
        }
    }

    fun onNonEmptyState() {
        binding?.apply {
            emptyMoviesTextView.visibility = View.GONE
            moviesRecyclerView.visibility = View.VISIBLE
        }
    }

    fun getToTop() {
        binding?.moviesRecyclerView?.smoothScrollToPosition(0)
    }

    fun drawRecycler(
        manager: LinearLayoutManager,
        movieAdapter: MovieAdapter<*>,
        scrollListener: PaginationScrollListener<*>
    ) = binding?.moviesRecyclerView?.run {
        layoutManager = manager
        adapter = movieAdapter
        addOnScrollListener(scrollListener)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}