package com.example.moviesapp.features.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.Movie
import com.example.moviesapp.R
import java.io.Serializable

const val BASE_POSTER_URL = "http://image.tmdb.org/t/p/w92"
const val ACTION_OPEN_DETAILS_SCREEN =
    "com.example.moviesapp.features.search.ACTION_OPEN_DETAILS_SCREEN"
const val EXTRA_MOVIE = "com.example.moviesapp.features.search.EXTRA_MOVIE"

class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val posterImageView by lazy { view.findViewById<ImageView>(R.id.poster_image_view) }
    private val nameTextView by lazy { view.findViewById<TextView>(R.id.movie_name_text_view) }
    private val dateTextView by lazy { view.findViewById<TextView>(R.id.release_date_text_view) }
    private val overviewTextView by lazy { view.findViewById<TextView>(R.id.overview_text_view) }
    private val movieCardView by lazy { view.findViewById<CardView>(R.id.movie_card_view) }

    fun bind(movie: Movie) {
        if (!movie.poster.isNullOrBlank())
            Glide.with(view).load(BASE_POSTER_URL + movie.poster).into(posterImageView)
        nameTextView.text = movie.title
        dateTextView.text =
            if (!movie.releaseDate.isNullOrEmpty()) movie.releaseDate else "--"
        overviewTextView.text =
            if (!movie.overView.isNullOrEmpty()) movie.overView else "no available overView ..."
        movieCardView.setOnClickListener { onViewClicked(movie) }
    }

    private fun onViewClicked(movie: Movie) {
        Intent(ACTION_OPEN_DETAILS_SCREEN)
            .putExtra(EXTRA_MOVIE, movie as Serializable)
            .also { view.context.sendBroadcast(it) }
    }
}

class MovieAdapter(
    private val movieList: ArrayList<Movie>
) : RecyclerView.Adapter<MovieViewHolder>(){

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
            .let { MovieViewHolder(it) }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
            holder.bind(movieList[position])

    fun addItems(results: List<Movie>) {
        results.forEach { movieList.add(it) }
        notifyDataSetChanged()
    }
}