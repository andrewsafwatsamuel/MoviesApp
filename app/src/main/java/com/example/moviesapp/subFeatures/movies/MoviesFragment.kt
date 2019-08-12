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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.R
import com.example.moviesapp.adapters.ACTION_OPEN_DETAILS_SCREEN
import com.example.moviesapp.adapters.ID_EXTRA
import com.example.moviesapp.adapters.MovieAdapter
import com.example.moviesapp.checkConnectivity
import com.example.moviesapp.features.details.DetailsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.no_internet_connection.*
import java.util.concurrent.TimeUnit

class MoviesFragment : Fragment() {

    private val disposables = CompositeDisposable()

    private val showMovieDetails: PublishSubject<Long> = PublishSubject.create()

    private val resultsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            showMovieDetails.onNext(intent.getLongExtra(ID_EXTRA, 0))
        }
    }

    var close = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_movies, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showMovieDetails.debounce(300, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe { startDetailsScreen(it) }
            .also { disposables.add(it) }
    }

    override fun onResume() {
        super.onResume()
        activity?.registerReceiver(resultsReceiver, IntentFilter(ACTION_OPEN_DETAILS_SCREEN))
    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(resultsReceiver)
        //  disposables.clear()
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        disposables.dispose()
    }

    private fun startDetailsScreen(id: Long) {
        Intent(context, DetailsActivity::class.java)
            .putExtra(ID_EXTRA, id)
            .also { startActivity(it) }
            .also { if (close) activity?.finish() }
    }

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
        not_connected_layout.visibility=View.GONE
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