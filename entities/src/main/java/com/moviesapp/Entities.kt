package com.moviesapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames
import java.io.Serializable

@kotlinx.serialization.Serializable
data class MovieResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("page")
    @field:SerializedName("page")
    val pageNumber: Int,
    @JsonNames("total_results")
    @field:SerializedName("total_results")
    val resultCount: Int,
    @JsonNames("total_pages")
    @field:SerializedName("total_pages")
    val pageCount: Int,
    val results: List<Movie>
) : Serializable

@kotlinx.serialization.Serializable
data class Movie @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("vote_count")
    @field:SerializedName("vote_count")
    val voteCount: Int?,
    val id: Long?,
    @JsonNames("video")
    @field:SerializedName("video")
    val haveVideo: Boolean?,
    @JsonNames("vote_average")
    @field:SerializedName("vote_average")
    val voteAverage: Double?,
    val title: String?,
    val popularity: Float?,
    @JsonNames("poster_path")
    @field:SerializedName("poster_path")
    val poster: String?,
    @JsonNames("original_language")
    @field:SerializedName("original_language")
    val language: String?,
    @JsonNames("original_title")
    @field:SerializedName("original_title")
    val originalTitle: String?,
    @JsonNames("genre_ids")
    @field:SerializedName("genre_ids")
    val genreIds: List<Int>?,
    @JsonNames("backdrop_path")
    @field:SerializedName("backdrop_path")
    val backDropPhoto: String?,
    @JsonNames("adult")
    @field:SerializedName("adult")
    val isAdult: Boolean?,
    @JsonNames("overview")
    val overView: String?,
    @JsonNames("release_date")
    @field:SerializedName("release_date")
    val releaseDate: String?
) : Serializable

@kotlinx.serialization.Serializable
data class DetailsResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("adult")
    @field:SerializedName("adult")
    val isAdult: Boolean?,
    @JsonNames("backdrop_path")
    @field:SerializedName("backdrop_path")
    val backDropPhoto: String?,
    val budget: Int?,
    val genres: List<Genre>?,
    @JsonNames("homepage")
    @field:SerializedName("homepage")
    val homePage: String?,
    val id: Long?,
    @JsonNames("imdb_id")
    @field:SerializedName("imdb_id")
    val imdbId: String?,
    @JsonNames("original_language")
    @field:SerializedName("original_language")
    val language: String?,
    @JsonNames("original_title")
    @field:SerializedName("original_title")
    val originalTitle: String?,
    @JsonNames("overview")
    @field:SerializedName("overview")
    val overView: String?,
    val popularity: Float?,
    @JsonNames("poster_path")
    @field:SerializedName("poster_path")
    val poster: String?,
    @JsonNames("production_companies")
    @field:SerializedName("production_companies")
    val companies: List<Company>?,
    @JsonNames("production_countries")
    @field:SerializedName("production_countries")
    val countries: List<Country>?,
    @JsonNames("release_date")
    @field:SerializedName("release_date")
    val releaseDate: String?,
    val revenue: Long?,
    @JsonNames("runtime")
    @field:SerializedName("runtime")
    val runTime: Int?,
    @JsonNames("spoken_languages")
    @field:SerializedName("spoken_languages")
    val languages: List<MovieLanguage>?,
    val status: String?,
    @JsonNames("tagline")
    @field:SerializedName("tagline")
    val tagLine: String?,
    val title: String?,
    @JsonNames("video")
    @field:SerializedName("video")
    val haveVideo: Boolean?,
    @JsonNames("vote_average")
    @field:SerializedName("vote_average")
    val voteAverage: Double?,
    @JsonNames("vote_count")
    @field:SerializedName("vote_count")
    val voteCount: Int?,
    @JsonNames("videos")
    @field:SerializedName("videos")
    val trailers: Videos?
)

@kotlinx.serialization.Serializable
data class Genre(
    val id: Int?,
    val name: String?
) : Serializable

@kotlinx.serialization.Serializable
data class Company @OptIn(ExperimentalSerializationApi::class) constructor(
    val id: Int?,
    @field: SerializedName("logo_path")
    @JsonNames("logo_path")
    val logo: String?,
    val name: String?,
    @field: SerializedName("origin_country")
    @JsonNames("origin_country")
    val country: String?
)

@kotlinx.serialization.Serializable
data class Country @OptIn(ExperimentalSerializationApi::class) constructor(
    @field: SerializedName("iso_3166_1")
    @JsonNames("iso_3166_1")
    val symbol: String?,
    val name: String?
)

@kotlinx.serialization.Serializable
data class MovieLanguage @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("iso_639_1")
    @field: SerializedName("iso_639_1")
    val symbol: String?,
    val name: String?
)

@kotlinx.serialization.Serializable
data class Videos @OptIn(ExperimentalSerializationApi::class) constructor(
    @field:SerializedName("results")
    @JsonNames("results")
    val videos: List<Video?>
) : Serializable

@kotlinx.serialization.Serializable
data class Video @OptIn(ExperimentalSerializationApi::class) constructor(
    val id: String?,
    @field:SerializedName("iso_639_1")
    @JsonNames("iso_639_1")
    val language: String?,
    @field:SerializedName("iso_3166_1")
    @JsonNames("iso_3166_1")
    val country: String?,
    val key: String?,
    val name: String?,
    val site: String?,
    val size: Int?,
    val type: String?
) : Serializable

@kotlinx.serialization.Serializable
data class CreditsResponse(
    val cast: List<CastMember?>,
    val crew: List<CrewMember?>
) : Serializable

data class CreditsMember(val title: String?, val role: String?, val url: String?)

@kotlinx.serialization.Serializable
data class CastMember @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("cast_id")
    @field:SerializedName("cast_id")
    val castId: Int?,
    val character: String?,
    @JsonNames("credit_id")
    @field:SerializedName("credit_id")
    val creditId: String?,
    val gender: Int?,
    val id: Long?,
    val name: String?,
    val order: Int?,
    @JsonNames("profile_path")
    @field:SerializedName("profile_path")
    val photo: String?
) : Serializable

@kotlinx.serialization.Serializable
data class CrewMember @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("credit_id")
    @field:SerializedName("credit_id")
    val creditId: String?,
    val department: String?,
    val gender: Int?,
    val id: Long?,
    val job: String?,
    val name: String?,
    @JsonNames("profile_path")
    @field:SerializedName("profile_path")
    val photo: String?
) : Serializable

@Entity
data class SuccessfulQuery(
    @field:PrimaryKey val queryString: String
) : Serializable