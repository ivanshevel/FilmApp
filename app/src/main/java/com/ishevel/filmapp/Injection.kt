package com.ishevel.filmapp

import com.ishevel.filmapp.api.ApiKeyInterceptor
import com.ishevel.filmapp.api.ApiService
import com.ishevel.filmapp.api.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiClient {

    @Provides
    @Singleton
    fun create(): ApiService {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        val keyHolder = ApiKeyInterceptor(BuildConfig.API_KEY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(keyHolder)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}