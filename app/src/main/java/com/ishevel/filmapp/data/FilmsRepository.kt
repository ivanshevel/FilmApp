package com.ishevel.filmapp.data

import android.content.Context
import android.util.Log
import com.ishevel.filmapp.MainActivity
import com.ishevel.filmapp.api.ApiService
import com.ishevel.filmapp.api.model.ApiFilm
import com.ishevel.filmapp.api.model.ApiLatestFilms
import com.ishevel.filmapp.data.model.Configuration
import com.ishevel.filmapp.data.model.Film
import com.ishevel.filmapp.data.model.Genres
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

private const val API_STARTING_PAGE_INDEX = 1

class FilmsRepository(private val service: ApiService) {

    private val genres: Genres = InMemoryGenres(service)
    private val configuration: Configuration = Configuration()

    private val inMemoryCache = mutableSetOf<Film>()

    private val searchResults = MutableSharedFlow<SearchResult>(replay = 1)

    private var lastRequestedPage = API_STARTING_PAGE_INDEX

    private var isRequestInProgress = false

    private var selectedFilm: Film? = null

    suspend fun getApiLatestFilmsStream(query: String): Flow<SearchResult> {
        Log.d("FilmsRepository", "New query: $query")
        lastRequestedPage = 1
        inMemoryCache.clear()
        requestAndSaveData(query)

        return searchResults
    }

    suspend fun requestMore(query: String) {
        if (isRequestInProgress) return
        val successful = requestAndSaveData(query)
        if (successful) {
            lastRequestedPage++
        }
    }

    fun setSelectedFilm(film: Film) {
        selectedFilm = film
    }

    fun getSelectedFilm(): FilmData =
        selectedFilm?.let { FilmData.Ok(it) } ?: FilmData.Error

    suspend fun retry(query: String) {
        if (isRequestInProgress) return
        requestAndSaveData(query)
    }

    private suspend fun requestAndSaveData(query: String): Boolean {
        val imageBaseUrl = configuration.imageBaseUrl
        val listPosterSize = configuration.posterSizeList
        val detailsPosterSize = configuration.posterSizeDetails

        isRequestInProgress = true
        var successful = false

        try {
            val response = service.getLatestFilms(query, lastRequestedPage)
            Log.d("FilmsRepository", "response $response")
            val films = response.results?.mapNotNull { apiMovie ->
                with(apiMovie) {
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
                            averageVote = voteAverage ?: 0.0,
                            genres = genres.get().filter { genre -> genre.id in genreIds }
                        )
                    } else {
                        null
                    }
                }
            }
            inMemoryCache.addAll(films ?: emptyList())
            val filmsInMemoryCache = inMemoryCache.toList()
            searchResults.emit(SearchResult.Success(filmsInMemoryCache))
            successful = true
        } catch (exception: IOException) {
            searchResults.emit(SearchResult.Error(exception))
        } catch (exception: HttpException) {
            searchResults.emit(SearchResult.Error(exception))
        }
        isRequestInProgress = false
        return successful
    }
}

sealed class SearchResult {
    data class Success(val data: List<Film>) : SearchResult()
    data class Error(val error: Exception) : SearchResult()
}

sealed class FilmData {
    object Error: FilmData()
    class Ok(val film: Film): FilmData()
}