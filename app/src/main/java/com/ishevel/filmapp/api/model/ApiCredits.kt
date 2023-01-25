package com.ishevel.filmapp.api.model

import com.google.gson.annotations.SerializedName

data class ApiCredits(

    @field:SerializedName("cast") val cast: List<ApiActor>?
)

data class ApiActor(

    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("profile_path") val profilePath: String?,
    @field:SerializedName("order") val order: Int?
)