package com.ishevel.filmapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ishevel.filmapp.api.ApiService
import com.ishevel.filmapp.data.model.Actor
import kotlinx.coroutines.flow.Flow

class ActorsRepository(private val service: ApiService) {

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