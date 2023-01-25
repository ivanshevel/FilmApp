package com.ishevel.filmapp.data

import com.ishevel.filmapp.api.ApiService
import com.ishevel.filmapp.api.model.ApiGenres
import com.ishevel.filmapp.data.model.Genre
import com.ishevel.filmapp.data.model.Genres

class InMemoryGenres constructor(private val service: ApiService) : Genres {

    lateinit var genres: List<Genre>

    override suspend fun get(): List<Genre> {
        if (!::genres.isInitialized) {
            mapGenres(service.getGenres()).also { result ->
                genres = if (!result.isEmpty()) {
                    result
                } else {
                    emptyList()
                }
            }
        }
        return genres
    }

    private fun mapGenres(apiGenres: ApiGenres) =
        apiGenres.genres?.mapNotNull { genre ->
            if (genre.id != null && genre.name != null) {
                Genre(genre.id, genre.name)
            } else {
                null
            }
        } ?: emptyList()
}