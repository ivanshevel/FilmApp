package com.ishevel.filmapp.api

import com.ishevel.filmapp.api.model.ApiCredits
import com.ishevel.filmapp.api.model.ApiGenres
import com.ishevel.filmapp.api.model.ApiLatestFilms
import okhttp3.Interceptor
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("genre/movie/list")
    suspend fun getGenres(): ApiGenres

    @GET("movie/now_playing")
    suspend fun getLatestFilms(
        @Query("page") page: Int
    ): ApiLatestFilms

    @GET("movie/{movie_id}/credits")
    suspend fun getCredits(@Path("movie_id") filmId: Int): ApiCredits
}

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response = chain.request().let { request ->
        val newUrl = request.url.newBuilder()
            .addQueryParameter("api_key", apiKey).build()
        val newRequest = request.newBuilder().url(newUrl).build()
        chain.proceed(newRequest)
    }
}

const val BASE_URL = "https://api.themoviedb.org/3/"