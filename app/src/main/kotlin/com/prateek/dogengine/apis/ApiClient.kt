package com.prateek.dogengine.apis

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.thedogapi.com"

    @Synchronized
    fun getClient(): Retrofit {
        return Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient? {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(getInterceptor()) //httpLogging interceptor for logging network requests
            .build()
    }

    private fun getInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            var request = chain.request()
            request = request.newBuilder()
                .addHeader("x-api-key", "your-api-key-provided-by-thedogapi")
                .build()
            chain.proceed(request)
        }
    }
}