package com.ishevel.filmapp.ui

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.ishevel.filmapp.data.ActorsRepository
import com.ishevel.filmapp.data.FilmData
import com.ishevel.filmapp.data.FilmsRepository
import kotlinx.coroutines.launch

class FilmDetailsViewModel(
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

    val isFavoriteFilm: LiveData<Boolean> = filmsRepository.isFavoriteFilm(filmId).asLiveData()
    val actorsFlow = actorsRepository.getApiCastForFilmFlow(filmId).cachedIn(viewModelScope)

    fun changeFavoriteStatus(status: Boolean) = viewModelScope.launch {
        if (!status) {
            filmsRepository.addFavoriteFilm(filmId)
        } else {
            filmsRepository.deleteFavoriteFilm(filmId)
        }
    }
}