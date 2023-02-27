package com.ishevel.filmapp

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.ishevel.filmapp.api.ApiService
import com.ishevel.filmapp.data.ActorsRepository
import com.ishevel.filmapp.data.FilmsRepository
import com.ishevel.filmapp.local.AppDatabase
import com.ishevel.filmapp.ui.ViewModelFactory

object Injection {

    private lateinit var filmsRepository: FilmsRepository
    private lateinit var actorsRepository: ActorsRepository

    private fun provideFilmsRepository(context: Context): FilmsRepository {
        if (!Injection::filmsRepository.isInitialized)
            filmsRepository = FilmsRepository(AppDatabase.getInstance(context) ,ApiService.create())
        return filmsRepository
    }

    private fun provideActorsRepository(): ActorsRepository {
        if (!Injection::actorsRepository.isInitialized)
            actorsRepository = ActorsRepository(ApiService.create())
        return actorsRepository
    }

    fun provideViewModelFactory(owner: SavedStateRegistryOwner, context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(owner, provideFilmsRepository(context), provideActorsRepository())
    }
}