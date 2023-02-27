package com.ishevel.filmapp.local

import androidx.room.*
import com.ishevel.filmapp.data.model.Film
import com.ishevel.filmapp.data.model.FilmFavorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmFavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteFilm(filmFavorite: FilmFavorite): Long

    @Query("DELETE FROM films_favorites WHERE film_id = :id")
    suspend fun deleteFavoriteFilm(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM films_favorites WHERE film_id = :id LIMIT 1)")
    fun isFavorite(id: Int): Flow<Boolean>

    @Transaction
    @Query("SELECT * FROM films WHERE id IN (SELECT DISTINCT(film_id) FROM films_favorites)")
    fun getFavoriteFilms(): Flow<List<Film>>
}