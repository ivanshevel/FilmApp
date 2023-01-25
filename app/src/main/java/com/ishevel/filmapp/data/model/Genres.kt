package com.ishevel.filmapp.data.model

interface Genres {
    suspend fun get(): List<Genre>
}