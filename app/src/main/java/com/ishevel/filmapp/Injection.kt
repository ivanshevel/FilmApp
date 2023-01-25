package com.ishevel.filmapp

import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.ishevel.filmapp.api.ApiService
import com.ishevel.filmapp.data.ActorsRepository
import com.ishevel.filmapp.data.FilmsRepository
import com.ishevel.filmapp.ui.FilmDetailsViewModelFactory
import com.ishevel.filmapp.ui.FilmsListViewModelFactory

object Injection {

    private val filmsRepository: FilmsRepository by lazy {FilmsRepository(ApiService.create())}
    private lateinit var actorsRepository: ActorsRepository

    private fun provideFilmsRepository(): FilmsRepository {
        return filmsRepository
    }

    private fun provideActorsRepository():ActorsRepository {
        if (Injection::actorsRepository.isInitialized) {
            return actorsRepository
        }
        else {
            return ActorsRepository(ApiService.create())
        }
    }

    fun provideFilmsListViewModelFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return FilmsListViewModelFactory(owner, provideFilmsRepository())
    }

    fun provideFilmDetailsViewModelFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return FilmDetailsViewModelFactory(owner, provideFilmsRepository(), provideActorsRepository())
    }
}