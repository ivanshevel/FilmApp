package com.ishevel.filmapp.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ishevel.filmapp.data.remoteKeys.FilmRemoteKeys

@Dao
interface FilmRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<FilmRemoteKeys>)

    @Query("Select * From films_remote_keys Where film_id = :id")
    suspend fun getRemoteKeyByMovieID(id: Int): FilmRemoteKeys?

    @Query("Delete From films_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("Select created_at From films_remote_keys Order By created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?
}