package com.ishevel.filmapp.api.model

import com.google.gson.annotations.SerializedName

data class ApiGenres(

    @field:SerializedName("genres") val  genres: List<ApiGenre>? = null
)

data class ApiGenre(

    @field:SerializedName("id") val id: Int? = null,
    @field:SerializedName("name") val name: String? = null
)