package com.ishevel.filmapp.ui

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.ishevel.filmapp.data.ActorsRepository
import com.ishevel.filmapp.data.FilmData
import com.ishevel.filmapp.data.FilmsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilmDetailsViewModel @Inject constructor(
    private val filmsRepository: FilmsRepository,
    actorsRepository: ActorsRepository
) : ViewModel(){

    val selectedFilm: LiveData<FilmData> by lazy { MutableLiveData(filmsRepository.getSelectedFilm()) }

    private val filmId = filmsRepository.getSelectedFilm().let { filmData ->
        when (filmData) {
            is FilmData.Ok -> filmData.film.id
            is FilmData.Error -> 0
        }
    }
    val actorsFlow = actorsRepository.getApiCastForFilmFlow(filmId).cachedIn(viewModelScope)
}