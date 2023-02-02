package com.ishevel.filmapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ishevel.filmapp.api.ApiService
import com.ishevel.filmapp.data.model.Configuration
import com.ishevel.filmapp.data.model.Film
import com.ishevel.filmapp.data.model.Genres
import retrofit2.HttpException
import java.io.IOException

private const val API_STARTING_PAGE_INDEX = 1

class FilmsPagingSource(
    private val service: ApiService
) : PagingSource<Int, Film>() {

    private val genres: Genres = InMemoryGenres(service)

    private val configuration: Configuration = Configuration()

    private val imageBaseUrl = configuration.imageBaseUrl
    private val listPosterSize = configuration.posterSizeList
    private val detailsPosterSize = configuration.posterSizeDetails
    private val backdropSize = configuration.backdropSize

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        val position = params.key ?: API_STARTING_PAGE_INDEX
        return try {
            val response = service.getLatestFilms(position)
            val films = response.results?.mapNotNull { apiFilm ->
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
                            releaseDate = releaseDate ?: ""
                        )
                    } else {
                        null
                    }
                }
            }
            LoadResult.Page(
                data = films ?: emptyList(),
                prevKey = if (position == API_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Film>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}