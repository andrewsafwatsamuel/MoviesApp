package com.example.moviesapp.features.details

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.Movie
import com.example.domain.useCases.retrieveGenres
import com.example.moviesapp.R
import com.example.moviesapp.features.search.BASE_POSTER_URL
import com.example.moviesapp.features.search.EXTRA_MOVIE
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val extraMovie = intent.getSerializableExtra(EXTRA_MOVIE) as Movie

        supportActionBar?.title = extraMovie.title
        loadCover(extraMovie.backDropPhoto ?: "")
        println(extraMovie.backDropPhoto)
        loadPoster(extraMovie.poster ?: "")
        votes_text_view.text = "votes: ${extraMovie.voteCount}"
        release_date_text_view.text = "released in: ${extraMovie.releaseDate}"
        showGenres(extraMovie.genreIds?: listOf())
        overview_text_view.text = extraMovie.overView
    }

    private fun loadPoster(posterUrl: String) = posterUrl
        .takeUnless { it.isBlank() }
        ?.also {
            Glide.with(view).load(BASE_POSTER_URL + posterUrl).into(details_poster_image_view)
        }

    private fun loadCover(coverUrl: String) = coverUrl
        .takeUnless { it.isBlank() }
        ?.also {
            Glide.with(view).load(BASE_POSTER_URL + coverUrl).into(cover_image_view)
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
