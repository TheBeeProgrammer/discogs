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
import jakarta.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkStatusInterceptor(
        connectionManager: ConnectionManager
    ): Interceptor = NetworkStatusInterceptor(connectionManager)

    @Provides
    @Singleton
    fun provideAuthInterceptor(): Interceptor = DiscogsAuthInterceptor()

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
        networkStatusInterceptor: Interceptor,
        authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        errorInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkStatusInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(errorInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
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