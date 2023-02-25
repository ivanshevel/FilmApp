package com.ishevel.filmapp.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ishevel.filmapp.data.model.Film

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(films: List<Film>)

    @Query("SELECT * FROM films ORDER BY page ASC, rating DESC")
    fun getFilms(): PagingSource<Int, Film>

    @Query("DELETE FROM films")
    suspend fun clearAll()
}