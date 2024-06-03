package com.mdts.eieapp.data.retrofit

import com.google.gson.Gson
import com.mdts.eieapp.BuildConfig
import com.mdts.eieapp.config.AppConfig
import com.mdts.eieapp.config.BackendEnvironment
import com.mdts.eieapp.data.network.ConnectivityDataSource
import com.mdts.eieapp.data.retrofit.interceptors.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class RetrofitManager (
    private val gson: Gson,
    private val connectivityDataSource: ConnectivityDataSource,
    private val baseUrl: String,
    private val openAiAPIKey: String? = null
) {

    class NetworkLogger : HttpLoggingInterceptor.Logger {
        override fun log(message: String?) {
            Timber.d(message)
        }
    }

    private fun createHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addNetworkInterceptor { chain ->
                // validate network connection
                if (!connectivityDataSource.connected) {
                    throw UnknownHostException()
                }

                val newRequestBuilder = chain.request().newBuilder()
                newRequestBuilder.addHeader("Content-Type", "application/json")
                newRequestBuilder.addHeader("Authorization", "Bearer $openAiAPIKey")
                val newRequest = newRequestBuilder.build()
                return@addNetworkInterceptor chain.proceed(newRequest)
            }
            if (BuildConfig.DEBUG) {
                if (AppConfig.backendEnvironment == BackendEnvironment.Dev || AppConfig.backendEnvironment == BackendEnvironment.Staging) {
                    addNetworkInterceptor(HttpLoggingInterceptor(NetworkLogger()).apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
        }.connectTimeout(90, TimeUnit.SECONDS).callTimeout(90, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS).build()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson)).client(createHttpClient())
            .build()
    }

    operator fun <T> get(apiServiceClazz: Class<T>): T {
        return createRetrofit().create(apiServiceClazz)
    }
}
