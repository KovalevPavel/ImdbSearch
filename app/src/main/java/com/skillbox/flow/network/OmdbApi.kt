package com.skillbox.flow.network

import com.skillbox.flow.data.ServerItemsWrapper
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface OmdbApi {
    @GET("?")
    fun makeRequest(
        @Query("s") query: String,
        @Query("type") type: String
    ): Call<ServerItemsWrapper>

    @GET
    suspend fun downloadPoster(
        @Url posterUrl: String
    ): ResponseBody
}
