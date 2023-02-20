package com.ishevel.filmapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ishevel.filmapp.api.ApiService
import com.ishevel.filmapp.data.model.Actor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActorsRepository @Inject constructor(
    private val service: ApiService
    ) {

    fun getApiCastForFilmFlow(filmId: Int) : Flow<PagingData<Actor>> {

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_ACTORS_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ActorsPagingSource(service, filmId) }
        ).flow
    }

    companion object {
        const val NETWORK_ACTORS_PAGE_SIZE = 20
    }
}