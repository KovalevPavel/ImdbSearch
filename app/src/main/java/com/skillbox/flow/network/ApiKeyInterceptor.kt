package com.skillbox.flow.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    companion object {
        private const val API_KEY = "f4e0a452"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalURL = chain.request().url()
        val newURL = originalURL.newBuilder()
            .addQueryParameter("apikey", API_KEY)
            .build()
        val newRequest = chain.request().newBuilder()
            .url(newURL)
            .build()
        return chain.proceed(newRequest)
    }
}
