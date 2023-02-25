package com.ishevel.filmapp.local

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import com.ishevel.filmapp.data.model.Genre


class Converters {

    @TypeConverter
    fun fromString(value: String): List<Genre> {
        val listType = object : TypeToken<List<Genre>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListToString(list: List<Genre>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}