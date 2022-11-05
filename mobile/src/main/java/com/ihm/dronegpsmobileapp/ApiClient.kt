package com.ihm.dronegpsmobileapp

import android.util.Log

import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalArgumentException

object ApiClient {
    private val LOG_TAG = ApiClient::class.java.simpleName
    var TMDBAPI_BASE_URL = "https://api.themoviedb.org/3/"
    @JvmField
    var IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w45"
    private var retrofit: Retrofit? = null
    fun get(): Retrofit? {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        try {
            retrofit = Retrofit.Builder()
                    .baseUrl(TMDBAPI_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
        } catch (e: IllegalArgumentException) {
            Log.e(LOG_TAG, e.message!!)
        }
        return retrofit
    }
}