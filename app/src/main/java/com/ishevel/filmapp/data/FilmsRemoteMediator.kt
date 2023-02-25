package com.ishevel.filmapp.data

import androidx.paging.*
import androidx.room.withTransaction
import com.ishevel.filmapp.api.ApiService
import com.ishevel.filmapp.data.model.Configuration
import com.ishevel.filmapp.data.model.Film
import com.ishevel.filmapp.data.model.Genres
import com.ishevel.filmapp.data.remoteKeys.FilmRemoteKeys
import com.ishevel.filmapp.local.AppDatabase
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class FilmsRemoteMediator(
    private val database: AppDatabase,
    private val service: ApiService
) : RemoteMediator<Int, Film>(){

    private val genres: Genres = InMemoryGenres(service)

    private val configuration: Configuration = Configuration()

    private val imageBaseUrl = configuration.imageBaseUrl
    private val listPosterSize = configuration.posterSizeList
    private val detailsPosterSize = configuration.posterSizeDetails
    private val backdropSize = configuration.backdropSize

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

        return if (System.currentTimeMillis() - (database.getFilmsRemoteKeysDao().getCreationTime() ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Film>): MediatorResult {

        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val apiresponse = service.getLatestFilms(page = page)
            val films = apiresponse.results?.mapNotNull { apiFilm ->
                with(apiFilm) {
                    if (id != null &&
                        title != null && title.isNotBlank() &&
                        posterPath != null && posterPath.isNotBlank()
                    ) {
                        val genreIds = genreIds ?: emptyList()
                        Film(
                            id = id,
                            title = title,
                            overview = overview ?: "",
                            listPosterUrl = "$imageBaseUrl$listPosterSize$posterPath",
                            detailsPosterUrl = "$imageBaseUrl$detailsPosterSize/$posterPath",
                            backdropUrl = "$imageBaseUrl$backdropSize$backdropPath",
                            averageVote = voteAverage ?: 0.0,
                            genres = genres.get().filter { genre -> genre.id in genreIds },
                            releaseDate = releaseDate ?: "",
                            page = page
                        )
                    } else {
                        null
                    }
                }
            }

            val endOfPaginationReached = films!!.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.getFilmsRemoteKeysDao().clearRemoteKeys()
                    database.getFilmsDao().clearAll()
                }
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = films.map {
                    FilmRemoteKeys(filmId = it.id, prevKey = prevKey, currentPage = page, nextKey = nextKey)
                }

                remoteKeys.let { database.getFilmsRemoteKeysDao().insertAll(it) }
                films.let { database.getFilmsDao().insertAll(it) }
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Film>): FilmRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.getFilmsRemoteKeysDao().getRemoteKeyByMovieID(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Film>): FilmRemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { movie ->
            database.getFilmsRemoteKeysDao().getRemoteKeyByMovieID(movie.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Film>): FilmRemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { movie ->
            database.getFilmsRemoteKeysDao().getRemoteKeyByMovieID(movie.id)
        }
    }
}