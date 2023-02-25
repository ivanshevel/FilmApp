package com.ishevel.filmapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "films")
data class Film(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val listPosterUrl: String,
    val detailsPosterUrl: String,
    val backdropUrl: String,
    val averageVote: Double,
    val genres: List<Genre>,
    val releaseDate: String,
    var page: Int
) {
    var genre1: String? = genres.getOrNull(0)?.name

    var genre2: String? = genres.getOrNull(1)?.name

    var rating: String = String.format("%.1f", averageVote)
}