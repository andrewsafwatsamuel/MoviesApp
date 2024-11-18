@file:OptIn(ExperimentalSerializationApi::class)

package com.moviesapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames
import java.io.Serializable
import kotlinx.serialization.Serializable as KTSerializable

@KTSerializable
data class MovieResponse(
    @JsonNames("page")
    val pageNumber: Int,
    @JsonNames("total_results")
    val resultCount: Int,
    @JsonNames("total_pages")
    val pageCount: Int,
    val results: List<Movie>
) : Serializable

@KTSerializable
data class Movie(
    @JsonNames("vote_count")
    val voteCount: Int?,
    val id: Long?,
    @JsonNames("video")
    val haveVideo: Boolean?,
    @JsonNames("vote_average")
    val voteAverage: Double?,
    val title: String?,
    val popularity: Float?,
    @JsonNames("poster_path")
    val poster: String?,
    @JsonNames("original_language")
    val language: String?,
    @JsonNames("original_title")
    val originalTitle: String?,
    @JsonNames("genre_ids")
    val genreIds: List<Int>?,
    @JsonNames("backdrop_path")
    val backDropPhoto: String?,
    @JsonNames("adult")
    val isAdult: Boolean?,
    @JsonNames("overview")
    val overView: String?,
    @JsonNames("release_date")
    val releaseDate: String?
) : Serializable

@KTSerializable
data class DetailsResponse(
    @JsonNames("adult")
    val isAdult: Boolean?,
    @JsonNames("backdrop_path")
    val backDropPhoto: String?,
    val budget: Int?,
    val genres: List<Genre>?,
    @JsonNames("homepage")
    val homePage: String?,
    val id: Long?,
    @JsonNames("imdb_id")
    val imdbId: String?,
    @JsonNames("original_language")
    val language: String?,
    @JsonNames("original_title")
    val originalTitle: String?,
    @JsonNames("overview")
    val overView: String?,
    val popularity: Float?,
    @JsonNames("poster_path")
    val poster: String?,
    @JsonNames("production_companies")
    val companies: List<Company>?,
    @JsonNames("production_countries")
    val countries: List<Country>?,
    @JsonNames("release_date")
    val releaseDate: String?,
    val revenue: Long?,
    @JsonNames("runtime")
    val runTime: Int?,
    @JsonNames("spoken_languages")
    val languages: List<MovieLanguage>?,
    val status: String?,
    @JsonNames("tagline")
    val tagLine: String?,
    val title: String?,
    @JsonNames("video")
    val haveVideo: Boolean?,
    @JsonNames("vote_average")
    val voteAverage: Double?,
    @JsonNames("vote_count")
    val voteCount: Int?,
    @JsonNames("videos")
    val trailers: Videos?
)

@KTSerializable
data class Genre(
    val id: Int?,
    val name: String?
) : Serializable

@KTSerializable
data class Company(
    val id: Int?,
    @JsonNames("logo_path")
    val logo: String?,
    val name: String?,
    @JsonNames("origin_country")
    val country: String?
)

@KTSerializable
data class Country(
    @JsonNames("iso_3166_1")
    val symbol: String?,
    val name: String?
)

@KTSerializable
data class MovieLanguage(
    @JsonNames("iso_639_1")
    val symbol: String?,
    val name: String?
)

@KTSerializable
data class Videos(
    @JsonNames("results")
    val videos: List<Video?>
) : Serializable

@KTSerializable
data class Video(
    val id: String?,
    @JsonNames("iso_639_1")
    val language: String?,
    @JsonNames("iso_3166_1")
    val country: String?,
    val key: String?,
    val name: String?,
    val site: String?,
    val size: Int?,
    val type: String?
) : Serializable

@KTSerializable
data class CreditsResponse(
    val cast: List<CastMember?>,
    val crew: List<CrewMember?>
) : Serializable

data class CreditsMember(val title: String?, val role: String?, val url: String?)

@KTSerializable
data class CastMember(
    @JsonNames("cast_id")
    val castId: Int?,
    val character: String?,
    @JsonNames("credit_id")
    val creditId: String?,
    val gender: Int?,
    val id: Long?,
    val name: String?,
    val order: Int?,
    @JsonNames("profile_path")
    val photo: String?
) : Serializable

@KTSerializable
data class CrewMember(
    @JsonNames("credit_id")
    val creditId: String?,
    val department: String?,
    val gender: Int?,
    val id: Long?,
    val job: String?,
    val name: String?,
    @JsonNames("profile_path")
    val photo: String?
) : Serializable

@Entity
data class SuccessfulQuery(
    @field:PrimaryKey val queryString: String
) : Serializable