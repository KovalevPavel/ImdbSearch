package com.skillbox.flow.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object Network {
    private const val API_URI = "http://www.omdbapi.com/"
    const val REQUEST_URI = "https://www.imdb.com/title/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(API_URI)
        .build()

    val api: OmdbApi
        get() = retrofit.create()
}
