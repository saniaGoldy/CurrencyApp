package com.example.currencyapp.currencyAPI

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val clientBuilder = OkHttpClient.Builder().addInterceptor(interceptor)

    val currencyAPI: CurrencyAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.apilayer.com/")
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
}