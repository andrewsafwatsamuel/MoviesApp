package com.example.moviesapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Movie
import com.example.moviesapp.R
import com.example.moviesapp.features.movies.MainActivity

const val CATEGORY_EXTRA = "com.example.moviesapp.adapters.categoryExtra"

class CategoryHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val showMore by lazy { view.findViewById<TextView>(R.id.show_more_text_view) }
    private val category by lazy { view.findViewById<TextView>(R.id.category_text_view) }
    private val moviesRecycler by lazy { view.findViewById<RecyclerView>(R.id.horizontal_movie_recycler_view) }

    fun bind(input: Pair<String, List<Movie>>) = input
        .also { i -> showMore.setOnClickListener { showMoreOnClick(i.first) } }
        .also { category.text = it.first }
        .let { drawMovies(it.second) }

    private fun showMoreOnClick(category: String) = Intent(view.context, MainActivity::class.java)
        .apply { putExtra(CATEGORY_EXTRA, category) }
        .let { view.context.startActivity(it) }

    private fun drawMovies(movies: List<Movie>) = with(moviesRecycler) {
        layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        adapter = GridAdapter(movies.toMutableList(), R.layout.horizontal_movie_item)
    }

}

class CategoryAdapter(
    owner: LifecycleOwner
    //private val categories: MutableLiveData<CategoryList>
) : RecyclerView.Adapter<CategoryHolder>() {

    init {
      //  categories.observe(owner, Observer { notifyDataSetChanged()})
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context).inflate(R.layout.fragment_horizontal_movies, parent, false)
            .let { CategoryHolder(it) }

    override fun getItemCount() = 0

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {

    }
}