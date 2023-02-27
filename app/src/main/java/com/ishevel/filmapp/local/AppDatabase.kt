package com.ishevel.filmapp.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ishevel.filmapp.data.model.Film
import com.ishevel.filmapp.data.model.FilmFavorite
import com.ishevel.filmapp.data.remoteKeys.FilmRemoteKeys

@Database(entities = [Film::class, FilmRemoteKeys::class, FilmFavorite::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getFilmsDao(): FilmDao
    abstract fun getFilmsRemoteKeysDao(): FilmRemoteKeysDao
    abstract fun getFilmsFavoritesDao(): FilmFavoritesDao

    companion object {

        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "FilmApp.db"
            )
                .build()
    }
}