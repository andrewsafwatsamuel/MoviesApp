package com.example.moviesapp.subFeatures.movies

import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.Movie
import com.example.domain.useCases.StoreMovieNameUseCase
import com.example.moviesapp.POSTER_SIZE
import com.example.moviesapp.R
import com.example.moviesapp.drawPhoto
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.Serializable


const val ACTION_OPEN_DETAILS_SCREEN =
    "com.example.moviesapp.subFeatures.movies.ACTION_OPEN_DETAILS_SCREEN"
const val EXTRA_MOVIE = "com.example.moviesapp.subFeatures.movies.EXTRA_MOVIE"
const val LIST_MOVIE_ADAPTER = "listMovieAdapter"
const val GRID_MOVIE_ADAPTER = "gridMovieAdapter"

private fun onViewClicked(movie: Movie, view: View) {
    Intent(ACTION_OPEN_DETAILS_SCREEN)
        .putExtra(EXTRA_MOVIE, movie as Serializable)
        .also { view.context.sendBroadcast(it) }
}

abstract class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(movie: Movie)
}

abstract class MovieAdapter<VH : MovieViewHolder>(
    private val movieList: MutableList<Movie>
) : RecyclerView.Adapter<VH>() {

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(movieList[position])

    fun addItems(results: List<Movie>) {
        results.forEach { movieList.add(it) }
        notifyDataSetChanged()
    }
}

class ListViewHolder(
    private val view: View,
    private val movieInsert: StoreMovieNameUseCase = StoreMovieNameUseCase()
) : MovieViewHolder(view) {

    private val posterImageView by lazy { view.findViewById<ImageView>(R.id.poster_image_view)!! }
    private val nameTextView by lazy { view.findViewById<TextView>(R.id.movie_name_text_view)!! }
    private val dateTextView by lazy { view.findViewById<TextView>(R.id.release_date_text_view)!! }
    private val overviewTextView by lazy { view.findViewById<TextView>(R.id.overview_text_view)!! }
    private val movieCardView by lazy { view.findViewById<CardView>(R.id.movie_card_view)!! }

    override fun bind(movie: Movie) {
        drawPhoto(POSTER_SIZE, movie.poster ?: "", posterImageView)
        nameTextView.text = movie.title
        dateTextView.text =
            if (!movie.releaseDate.isNullOrEmpty()) movie.releaseDate else "--"
        overviewTextView.text =
            if (!movie.overView.isNullOrEmpty()) movie.overView else "no available overView ..."
        movieCardView.setOnClickListener { onViewClicked(movie, view);storeMovieName(movie.title?:"haha") }
    }
    private fun storeMovieName(movieName: String) {
        Single.fromCallable { movieInsert(movieName) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}

class ListAdapter(movieList: MutableList<Movie>) : MovieAdapter<ListViewHolder>(movieList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
            .let { ListViewHolder(it) }
}

class GridViewHolder(private val view: View) : MovieViewHolder(view) {

    private val boaster by lazy { view.findViewById<ImageView>(R.id.grid_movie_boaster)!! }
    private val movieName by lazy { view.findViewById<TextView>(R.id.grid_movie_name)!! }
    private val container by lazy { view.findViewById<CardView>(R.id.grid_item)!! }

    override fun bind(movie: Movie) {
        movieName.text = movie.title?.replace("\n", "")
        drawPhoto(POSTER_SIZE, movie.poster, boaster)
        container.setOnClickListener { onViewClicked(movie, view) }
    }
}

class GridAdapter(movieList: MutableList<Movie>) : MovieAdapter<GridViewHolder>(movieList) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.grid_item, parent, false)
            .let { GridViewHolder(it) }
}

class AdapterFactory(private val adapter: String) {
    fun create(movieList: MutableList<Movie>): MovieAdapter<*> =
        when (adapter) {
            LIST_MOVIE_ADAPTER -> ListAdapter(movieList)
            GRID_MOVIE_ADAPTER -> GridAdapter(movieList)
            else -> throw Resources.NotFoundException("No adapter with $adapter name")
        }
}
