package com.ishevel.filmapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ishevel.filmapp.api.ApiService
import com.ishevel.filmapp.data.model.Film
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilmsRepository @Inject constructor(
    private val service: ApiService
    ) {

    private var selectedFilm: Film? = null

    fun getApiLatestFilmsFlow(): Flow<PagingData<Film>> {

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_FILMS_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { FilmsPagingSource(service) }
        ).flow
    }

    fun setSelectedFilm(film: Film) {
        selectedFilm = film
    }

    fun getSelectedFilm(): FilmData {
        return selectedFilm?.let { FilmData.Ok(it) } ?: FilmData.Error
    }

    companion object {
        const val NETWORK_FILMS_PAGE_SIZE = 50
    }
}

sealed class FilmData {
    class Ok(val film: Film): FilmData()
    object Error: FilmData()
}