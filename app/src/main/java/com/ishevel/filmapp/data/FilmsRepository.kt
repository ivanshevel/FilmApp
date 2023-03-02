package com.ishevel.filmapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ishevel.filmapp.api.ApiService
import com.ishevel.filmapp.data.model.Film
import com.ishevel.filmapp.data.model.FilmFavorite
import com.ishevel.filmapp.local.AppDatabase
import kotlinx.coroutines.flow.Flow

class FilmsRepository(
    private val database: AppDatabase,
    private val service: ApiService
    ) {

    private var selectedFilm: Film? = null

    @OptIn(ExperimentalPagingApi::class)
    fun getApiLatestFilmsFlow(): Flow<PagingData<Film>> {

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_FILMS_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = NETWORK_FILMS_PAGE_SIZE
            ),
            pagingSourceFactory = { database.getFilmsDao().getFilms() },
            remoteMediator = FilmsRemoteMediator(database, service)
        ).flow
    }

    fun getFavoriteFilmsFlow(): Flow<List<Film>> {
        return database.getFilmsFavoritesDao().getFavoriteFilms()
    }

    fun setSelectedFilm(film: Film) {
        selectedFilm = film
    }

    fun getSelectedFilm(): FilmData {
        return selectedFilm?.let { FilmData.Ok(it) } ?: FilmData.Error
    }

    suspend fun addFavoriteFilm(id: Int) {
        val favoriteFilm = FilmFavorite(filmId = id)
        database.getFilmsFavoritesDao().addFavoriteFilm(favoriteFilm)
    }

    suspend fun deleteFavoriteFilm(id: Int) {
        database.getFilmsFavoritesDao().deleteFavoriteFilm(id)
    }

    fun isFavoriteFilm(id: Int): Flow<Boolean> {
        return database.getFilmsFavoritesDao().isFavorite(id)
    }

    companion object {
        const val NETWORK_FILMS_PAGE_SIZE = 50
    }
}

sealed class FilmData {
    class Ok(val film: Film): FilmData()
    object Error: FilmData()
}