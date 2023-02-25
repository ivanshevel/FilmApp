package com.ishevel.filmapp.data.remoteKeys

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "films_remote_keys")
data class FilmRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "film_id")
    val filmId: Int,
    val prevKey: Int?,
    val currentPage: Int,
    val nextKey: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)