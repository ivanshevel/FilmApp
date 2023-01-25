package com.ishevel.filmapp.ui

import android.util.Log
import androidx.lifecycle.*
import com.ishevel.filmapp.data.FilmsRepository
import com.ishevel.filmapp.data.SearchResult
import com.ishevel.filmapp.data.model.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FilmsListViewModel(
    private val filmsRepository: FilmsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val state: LiveData<UiState>
    val accept: (UiAction) -> Unit

    private val _filmSelected = MutableLiveData<UiAction.UiEvent>()
    val filmSelected: LiveData<UiAction.UiEvent> by lazy { _filmSelected }

    init {
        val queryLiveData =
            MutableLiveData(savedStateHandle.get(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY)

        state = queryLiveData
            .distinctUntilChanged()
            .switchMap { queryString ->
                liveData {
                    val uiState = filmsRepository.getApiLatestFilmsStream(queryString)
                        .map {
                            UiState(
                                query = queryString,
                                searchResult = it
                            )
                        }
                        .asLiveData(Dispatchers.Main)
                    emitSource(uiState)
                }
            }

        accept = { action ->
            when (action) {
                is UiAction.Search -> queryLiveData.postValue(action.query)
                is UiAction.Scroll -> if (action.shouldFetchMore) {
                    val immutableQuery = queryLiveData.value
                    if (immutableQuery != null) {
                        viewModelScope.launch {
                            filmsRepository.requestMore(immutableQuery)
                        }
                    }
                }
                is UiAction.UiEvent -> {}
            }
        }
    }

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = state.value?.query
        super.onCleared()
    }

    fun onFilmClicked(film: Film) {
        filmsRepository.setSelectedFilm(film)
        _filmSelected.value = UiAction.UiEvent()
        Log.d("1/Selected film is: ", "${filmsRepository.getSelectedFilm().toString()}")
    }
}

private val UiAction.Scroll.shouldFetchMore
    get() = visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount

sealed class UiAction {
    data class Search(val query: String) : UiAction()
    class UiEvent() : UiAction()
    data class Scroll(
        val visibleItemCount: Int,
        val lastVisibleItemPosition: Int,
        val totalItemCount: Int
    ) : UiAction()
}

data class UiState(
    val query: String,
    val searchResult: SearchResult
)

private const val VISIBLE_THRESHOLD = 5
private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val DEFAULT_QUERY = ""