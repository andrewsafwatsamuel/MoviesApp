package com.example.moviesapp.subFeatures.movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.features.popularMovies.PopularMovies

const val MOVIE_CATEGORY = "com.example.moviesapp.subFeatures.movies.movieCategory"

class HorizontalMovieFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_horizontal_movies, container, false)

    fun setCategory(text: String) {
        category()?.text = text
    }

    fun category() = view?.findViewById<TextView>(R.id.category_text_view)

    fun showMoreOnClick(text: String) = showMore()?.setOnClickListener { startMovieScreen(text) }

    fun showMore() = view?.findViewById<TextView>(R.id.show_more_text_view)

    private fun startMovieScreen(text: String) = Intent(context, PopularMovies::class.java)
        .apply { putExtra(MOVIE_CATEGORY, text) }
        .let { startActivity(it) }

    fun recyclerView() = view?.findViewById<RecyclerView>(R.id.horizontal_movie_recycler_view)

}