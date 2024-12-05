package com.misterioesf.finance

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiInstance<T> {
    companion object {
        private val retrofitBuilder: Retrofit.Builder =
            Retrofit.Builder()
                .baseUrl("https://api.nbrb.by/")
                .addConverterFactory(GsonConverterFactory.create())

        private val retrofit: Retrofit = retrofitBuilder.build()

        private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        fun <T> createService(serviceClass: Class<T>): T {
            return retrofit.create(serviceClass)
        }
    }
}