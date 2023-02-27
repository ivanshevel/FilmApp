package com.ishevel.filmapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "films_favorites",
    foreignKeys = arrayOf(
        ForeignKey(entity = Film::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("film_id")
    )
    )
)
data class FilmFavorite(
    @ColumnInfo(name = "film_id")
    val filmId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}