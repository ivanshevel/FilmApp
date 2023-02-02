package com.ishevel.filmapp.ui

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.ishevel.filmapp.data.FilmsRepository
import com.ishevel.filmapp.data.model.Film

class FilmsListViewModel(
    private val filmsRepository: FilmsRepository
) : ViewModel() {

    val filmsFlow = filmsRepository.getApiLatestFilmsFlow().cachedIn(viewModelScope)

    private val _filmSelected = MutableLiveData<UiAction.UiEvent<Unit>>()
    val filmSelected: LiveData<UiAction.UiEvent<Unit>> by lazy { _filmSelected }

    fun onFilmClicked(film: Film) {
        filmsRepository.setSelectedFilm(film)
        _filmSelected.value = UiAction.UiEvent(Unit)
    }
}

sealed class UiAction {
    data class UiEvent<out T>(private val content: T) : UiAction() {
        private var hasBeenHandled = false
        fun getContentIfNotHandled(): T? {
            return if (hasBeenHandled) {
                null
            } else {
                hasBeenHandled = true
                content
            }
        }
    }
}
