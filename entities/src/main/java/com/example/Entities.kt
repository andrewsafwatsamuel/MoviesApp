package com.example

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MovieResponse(
    @field:SerializedName("page") val pageNumber: Int,
    @field:SerializedName("total_results") val resultCount: Int,
    @field:SerializedName("total_pages") val pageCount: Int,
    @field:SerializedName("results") val results: List<Movie>
) : Serializable

data class Movie(
    @field:SerializedName("vote_count") val voteCount: Int,
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("video") val haveVideo: Boolean,
    @field:SerializedName("vote_average") val voteAverage: Double,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("popularity") val popularity: Float,
    @field:SerializedName("poster_path") val poster: String,
    @field:SerializedName("original_language") val language: String,
    @field:SerializedName("original_title") val originalTitle: String,
    @field:SerializedName("genre_ids") val genreIds: List<Int>,
    @field:SerializedName("backdrop_path") val backDropPhoto: String,
    @field:SerializedName("adult") val isAdult: Boolean,
    @field:SerializedName("overview") val overView: String,
    @field:SerializedName("release_date") val releaseDate: String
) : Serializable

@Entity
data class Genre(
    @field:PrimaryKey val id: Int,
    val name: String
) : Serializable

@Entity
data class SuccessfulQuery(
    @field:PrimaryKey val queryString: String
) : Serializable