package com.ishevel.filmapp.data.model

data class Configuration (
    val imageBaseUrl: String = "https://image.tmdb.org/t/p/",
    val posterSizeList: String = "w342",
    val posterSizeDetails: String = "w780",
    val profileSize: String = "w185"
)