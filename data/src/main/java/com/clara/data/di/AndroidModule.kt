package com.clara.data.di

import android.content.Context
import com.clara.data.remote.ConnectionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AndroidModule {
    @Provides
    @Singleton
    fun provideConnectionManager(@ApplicationContext context: Context): ConnectionManager {
        return ConnectionManager(context)
    }
}