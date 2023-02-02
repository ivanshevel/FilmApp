package com.ishevel.filmapp.api.model

import com.google.gson.annotations.SerializedName

data class ApiLatestFilms(

    @field:SerializedName("results") val results: List<ApiFilm>?,
    val nextPage: Int? = null
)

data class ApiFilm(

    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("title") val title: String?,
    @field:SerializedName("genre_ids") val genreIds: List<Int>?,
    @field:SerializedName("poster_path") val posterPath: String?,
    @field:SerializedName("backdrop_path") val backdropPath: String?,
    @field:SerializedName("overview") val overview: String?,
    @field:SerializedName("vote_average") val voteAverage: Double?,
    @field:SerializedName("release_date") val releaseDate: String?
)