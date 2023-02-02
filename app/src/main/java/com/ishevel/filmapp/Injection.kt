package com.ishevel.filmapp

import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.ishevel.filmapp.api.ApiService
import com.ishevel.filmapp.data.ActorsRepository
import com.ishevel.filmapp.data.FilmsRepository
import com.ishevel.filmapp.ui.FilmDetailsViewModelFactory
import com.ishevel.filmapp.ui.FilmsListViewModelFactory

object Injection {

    private lateinit var filmsRepository: FilmsRepository
    private lateinit var actorsRepository: ActorsRepository

    private fun provideFilmsRepository(): FilmsRepository {
        if (!Injection::filmsRepository.isInitialized)
            filmsRepository = FilmsRepository(ApiService.create())
        return filmsRepository
    }

    private fun provideActorsRepository(): ActorsRepository {
        if (!Injection::actorsRepository.isInitialized)
            actorsRepository = ActorsRepository(ApiService.create())
        return actorsRepository
    }

    fun provideFilmsListViewModelFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return FilmsListViewModelFactory(owner, provideFilmsRepository())
    }

    fun provideFilmDetailsViewModelFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return FilmDetailsViewModelFactory(owner, provideFilmsRepository(), provideActorsRepository())
    }
}