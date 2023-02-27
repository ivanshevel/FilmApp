package com.ishevel.filmapp.ui

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.ishevel.filmapp.data.ActorsRepository
import com.ishevel.filmapp.data.FilmsRepository

class ViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val filmsRepository: FilmsRepository,
    private val actorsRepository: ActorsRepository
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(FilmsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilmsListViewModel(filmsRepository) as T
        } else if (modelClass.isAssignableFrom(FilmDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilmDetailsViewModel(filmsRepository, actorsRepository) as T
        } else if (modelClass.isAssignableFrom(FilmFavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilmFavoritesViewModel(filmsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}