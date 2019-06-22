package com.example.moviesapp.features.details

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Movie
import com.example.domain.useCases.retrieveGenres
import com.example.moviesapp.*
import com.example.moviesapp.subFeatures.movies.EXTRA_MOVIE
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_details.overview_text_view
import kotlinx.android.synthetic.main.activity_details.release_date_text_view

class DetailsActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val extraMovie = intent.getSerializableExtra(EXTRA_MOVIE) as Movie
        supportActionBar?.title = extraMovie.title
        drawPhoto(BACK_DRAW_SIZE,extraMovie.backDropPhoto?:"",cover_image_view)
        drawPhoto(POSTER_SIZE,extraMovie.poster?:"",details_poster_image_view)
        votes_text_view.text = "votes: ${extraMovie.voteCount}"
        release_date_text_view.text = "released in: ${extraMovie.releaseDate}"
        showGenres(extraMovie.genreIds?: listOf())
        overview_text_view.text = extraMovie.overView
    }

    private fun showGenres(ids: List<Int>) = genres_recycler_view
        .also {
            it.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
        .also { retrieveGenres({ genre -> it.adapter = GenreAdapter(genre) }, ids) }

    private fun retrieveGenres(genres: (List<String>) -> Unit, ids: List<Int>) =
        Single.fromCallable { retrieveGenres(ids) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ genres(it) }, Throwable::printStackTrace)
            .also { disposable.addAll() }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
