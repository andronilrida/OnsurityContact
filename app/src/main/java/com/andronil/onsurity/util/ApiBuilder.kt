package com.andronil.onsurity.util

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiBuilder {

    companion object{
        private lateinit var retrofit: Retrofit

        private fun buildRetrofit(): Retrofit {
            if (!Companion::retrofit.isInitialized){
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
                retrofit = Retrofit.Builder()
                    .baseUrl("https://7yd7u01nw9.execute-api.ap-south-1.amazonaws.com")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build()

            }
            return retrofit

        }
        fun getApiClient(): ApiClient = buildRetrofit().create(
            ApiClient::class.java)
    }
}