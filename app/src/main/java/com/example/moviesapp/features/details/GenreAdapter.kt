package com.example.moviesapp.features.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R

class GenreViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val genreItemText: TextView = view.findViewById(R.id.genre_item_text_view)
    fun bind(genre: String) = genreItemText.let { it.text = genre }
}

class GenreAdapter(private val genres: List<String>) : RecyclerView.Adapter<GenreViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GenreViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.genre_item, parent, false)
        )

    override fun getItemCount() = genres.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) =
        holder.bind(genres[position])
}