package com.ishevel.filmapp.local

import androidx.paging.PagingSource
import androidx.room.*
import com.ishevel.filmapp.data.model.Film

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(films: List<Film>)

    @Query("SELECT * FROM films ORDER BY page ASC, rating DESC")
    fun getFilms(): PagingSource<Int, Film>

    @Transaction
    @Query("DELETE FROM films WHERE NOT id IN (SELECT DISTINCT(film_id) FROM films_favorites)")
    suspend fun clearAll()
}