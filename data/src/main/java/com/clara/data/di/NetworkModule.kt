package com.clara.data.di

import com.clara.data.BuildConfig
import com.clara.data.remote.ApiConstants
import com.clara.data.remote.ConnectionManager
import com.clara.data.remote.DiscogsApiService
import com.clara.data.remote.interceptor.DiscogsAuthInterceptor
import com.clara.data.remote.interceptor.LoggingInterceptor
import com.clara.data.remote.interceptor.NetworkStatusInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkStatusInterceptor(
        connectionManager: ConnectionManager
    ): NetworkStatusInterceptor = NetworkStatusInterceptor(connectionManager)

    @Provides
    @Singleton
    fun provideAuthInterceptor(): DiscogsAuthInterceptor = DiscogsAuthInterceptor()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(logger: LoggingInterceptor): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(logger).apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BASIC
            }
        }
    }

    @Provides
    @Singleton
    fun provideCustomLogger(): LoggingInterceptor = LoggingInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        networkStatusInterceptor: NetworkStatusInterceptor,
        authInterceptor: DiscogsAuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkStatusInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDiscogsApiService(retrofit: Retrofit): DiscogsApiService {
        return retrofit.create(DiscogsApiService::class.java)
    }
}