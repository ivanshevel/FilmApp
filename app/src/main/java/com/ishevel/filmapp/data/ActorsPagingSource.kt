package com.ishevel.filmapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ishevel.filmapp.api.ApiService
import com.ishevel.filmapp.data.model.Actor
import com.ishevel.filmapp.data.model.Configuration
import retrofit2.HttpException
import java.io.IOException

class ActorsPagingSource(
    private val service: ApiService,
    private val filmId: Int
) : PagingSource<Int, Actor>(){
    private val configuration: Configuration = Configuration()

    private val imageBaseUrl = configuration.imageBaseUrl
    private val actorProfileSize = configuration.profileSize

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Actor> {
        return try {
            val response = service.getCredits(filmId)
            val actors = response.cast?.mapNotNull { apiFilm ->
                with(apiFilm) {
                    if (id != null &&
                        name != null && name.isNotBlank() &&
                        profilePath != null && profilePath.isNotBlank()
                    ) {
                        Actor(
                            id = id,
                            name = name,
                            profilePath = "$imageBaseUrl$actorProfileSize$profilePath",
                            order = order ?: 0
                        )
                    } else {
                        null
                    }
                }
            }
            LoadResult.Page(
                data = actors ?: emptyList(),
                prevKey = null,
                nextKey = null
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Actor>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}