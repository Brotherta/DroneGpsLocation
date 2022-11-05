package com.ihm.dronegpsmobileapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITmdbApi {
    // TMDB API call example:
    // https://api.themoviedb.org/3/configuration?api_key=f8c59b73c44d9240c1ded0a07da0d5f5
    // https://api.themoviedb.org/3/person/popular?api_key=f8c59b73c44d9240c1ded0a07da0d5f5&page=1
    @GET("person/popular")
    fun getPersonPopular(
            @Query("api_key") apiKey: String?,
            @Query("page") pageNb: String?
    ): Call<PersonPopularResponse?>?

    companion object {
        const val KEY = "f8c59b73c44d9240c1ded0a07da0d5f5"
    }
}