package com.ishevel.filmapp.ui

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.ishevel.filmapp.data.ActorsRepository
import com.ishevel.filmapp.data.FilmsRepository

class FilmsListViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val repository: FilmsRepository
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(FilmsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilmsListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class FilmDetailsViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val filmsRepository: FilmsRepository,
    private val actorsRepository: ActorsRepository
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(FilmDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilmDetailsViewModel(filmsRepository, actorsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}