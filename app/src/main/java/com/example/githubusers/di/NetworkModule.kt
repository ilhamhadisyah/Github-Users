package com.example.githubusers.di

import com.example.githubusers.data.ApiDataSources
import com.example.githubusers.network.ApiService
import com.example.githubusers.network.HttpInterceptor
import com.example.githubusers.utils.Urls
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.math.log

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideBaseUrl() = Urls.BASE_URL

    @Provides
    fun providesGson() = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun providesRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(Urls.BASE_URL)
        .client(
            OkHttpClient.Builder().also { client ->
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
                client.addInterceptor(HttpInterceptor())
                client.connectTimeout(120, TimeUnit.SECONDS)
                client.readTimeout(120, TimeUnit.SECONDS)
                client.protocols(Collections.singletonList(Protocol.HTTP_1_1))
            }.build()
        )
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiClient(apiService: ApiService) : ApiDataSources {
        return ApiDataSources(apiService)
    }
}