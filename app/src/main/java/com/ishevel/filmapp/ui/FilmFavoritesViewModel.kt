package com.ishevel.filmapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ishevel.filmapp.data.FilmsRepository
import com.ishevel.filmapp.data.model.Film

class FilmFavoritesViewModel(
    private val filmsRepository: FilmsRepository
) : ViewModel(){

    val filmFavoritesFlow: LiveData<List<Film>> = filmsRepository.getFavoriteFilmsFlow().asLiveData()

    private val _favoriteFilmSelected = MutableLiveData<UiAction.UiEvent<Unit>>()
    val favoriteFilmSelected: LiveData<UiAction.UiEvent<Unit>> by lazy { _favoriteFilmSelected }

    fun onFilmClicked(film: Film) {
        filmsRepository.setSelectedFavoriteFilm(film)
        _favoriteFilmSelected.value = UiAction.UiEvent(Unit)
    }
}