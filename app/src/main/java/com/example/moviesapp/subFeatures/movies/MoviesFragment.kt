package com.example.moviesapp.subFeatures.movies

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moviesapp.R
import com.example.moviesapp.features.details.DetailsActivity
import com.example.moviesapp.features.search.ACTION_OPEN_DETAILS_SCREEN
import com.example.moviesapp.features.search.EXTRA_MOVIE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_movies.*
import java.io.Serializable
import java.util.concurrent.TimeUnit

class MoviesFragment : Fragment() {

    private val disposables = CompositeDisposable()

    private val showMovieDetails: PublishSubject<Serializable> = PublishSubject.create()

    private val resultsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            showMovieDetails.onNext(intent!!.getSerializableExtra(EXTRA_MOVIE))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_movies, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showMovieDetails.debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe { startDetailsScreen(it) }
            .also { disposables.addAll() }

        activity?.registerReceiver(resultsReceiver, IntentFilter(ACTION_OPEN_DETAILS_SCREEN))
    }

    private fun startDetailsScreen(movie: Serializable) {
        Intent(context, DetailsActivity::class.java)
            .putExtra(EXTRA_MOVIE, movie)
            .also { startActivity(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(resultsReceiver)
        disposables.dispose()
    }

    fun onStartLoading() {
        onNonEmptyState()
        movies_progress_bar.visibility = View.VISIBLE
    }

    fun onFinishLoading() {
        movies_progress_bar.visibility = View.GONE
        empty_movies_text_view.visibility = View.GONE
    }

    fun onNonEmptyState() {
        empty_movies_text_view.visibility = View.GONE
        movies_recycler_view.visibility = View.VISIBLE
    }

    fun onEmptyState(emptyStateText: String) {
        movies_recycler_view.visibility = View.GONE
        empty_movies_text_view.visibility = View.VISIBLE
        empty_movies_text_view.text = emptyStateText
    }
}