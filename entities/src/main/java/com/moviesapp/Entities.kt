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

data class DetailsResponse(
    @field:SerializedName("adult") val isAdult: Boolean?,
    @field:SerializedName("backdrop_path") val backDropPhoto: String?,
    @field:SerializedName("budget") val budget: Int?,
    @field:SerializedName("genres") val genres: List<Genre>?,
    @field:SerializedName("homepage") val homePage: String?,
    @field:SerializedName("id") val id: Long?,
    @field:SerializedName("imdb_id") val imdbId: String?,
    @field:SerializedName("original_language") val language: String?,
    @field:SerializedName("original_title") val originalTitle: String?,
    @field:SerializedName("overview") val overView: String?,
    @field:SerializedName("popularity") val popularity: Float?,
    @field:SerializedName("poster_path") val poster: String?,
    @field:SerializedName("production_companies") val companies: List<Company>?,
    @field:SerializedName("production_countries") val countries: List<Country>?,
    @field:SerializedName("release_date") val releaseDate: String?,
    @field:SerializedName("revenue") val revenue: Long?,
    @field:SerializedName("runtime") val runTime: Int?,
    @field:SerializedName("spoken_languages") val languages: List<MovieLanguage>?,
    @field:SerializedName("status") val status: String?,
    @field:SerializedName("tagline") val tagLine: String?,
    @field:SerializedName("title") val title: String?,
    @field:SerializedName("video") val haveVideo: Boolean?,
    @field:SerializedName("vote_average") val voteAverage: Double?,
    @field:SerializedName("vote_count") val voteCount: Int?,
    @field:SerializedName("videos") val trailers: Videos?
)

data class Genre(
    @field: SerializedName("id") val id: Int?,
    @field:SerializedName("name") val name: String?
) : Serializable

data class Company(
    @field: SerializedName("id") val id: Int?,
    @field: SerializedName("logo_path") val logo: String?,
    @field: SerializedName("name") val name: String?,
    @field: SerializedName("origin_country") val country: String?
)

data class Country(
    @field: SerializedName("iso_3166_1") val symbol: String?,
    @field: SerializedName("name") val name: String?
)

data class MovieLanguage(
    @field: SerializedName("iso_639_1") val symbol: String?,
    @field: SerializedName("name") val name: String?
)

data class Videos(
    @field:SerializedName("results") val videos: List<Video?>
) : Serializable

data class Video(
    @field:SerializedName("id") val id: String?,
    @field:SerializedName("iso_639_1") val language: String?,
    @field:SerializedName("iso_3166_1") val country: String?,
    @field:SerializedName("key") val key: String?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("site") val site: String?,
    @field:SerializedName("size") val size: Int?,
    @field:SerializedName("type") val type: String?
) : Serializable

data class CreditsResponse(
    @field:SerializedName("cast") val cast: List<CastMember?>,
    @field:SerializedName("crew") val crew: List<CrewMember?>
) : Serializable

data class CreditsMember(val title: String?, val role: String?, val url: String?)

data class CastMember(
    @field:SerializedName("cast_id") val castId: Int?,
    @field:SerializedName("character") val character: String?,
    @field:SerializedName("credit_id") val creditId: String?,
    @field:SerializedName("gender") val gender: Int?,
    @field:SerializedName("id") val id: Long?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("order") val order: Int?,
    @field:SerializedName("profile_path") val photo: String?
) : Serializable

data class CrewMember(
    @field:SerializedName("credit_id") val creditId: String?,
    @field:SerializedName("department") val department: String?,
    @field:SerializedName("gender") val gender: Int?,
    @field:SerializedName("id") val id: Long?,
    @field:SerializedName("job") val job: String?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("profile_path") val photo: String?
) : Serializable

@Entity
data class SuccessfulQuery(
    @field:PrimaryKey val queryString: String
) : Serializable