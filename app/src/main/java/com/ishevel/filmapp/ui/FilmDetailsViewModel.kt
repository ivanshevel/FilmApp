package com.ishevel.filmapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ishevel.filmapp.data.ActorsRepository
import com.ishevel.filmapp.data.FilmData
import com.ishevel.filmapp.data.FilmsRepository
import com.ishevel.filmapp.data.model.Film

class FilmDetailsViewModel(
    private val filmsRepository: FilmsRepository,
    private val actorsRepository: ActorsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    val selectedFilm: LiveData<FilmData> by lazy { MutableLiveData(filmsRepository.getSelectedFilm()) }
}