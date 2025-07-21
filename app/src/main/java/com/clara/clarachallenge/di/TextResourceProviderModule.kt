package com.clara.clarachallenge.di

import com.clara.clarachallenge.ui.common.TextResourceProvider
import com.clara.clarachallenge.ui.common.TextResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TextResourceProviderModule {

    @Provides
    fun provideTextResourceProvider(
        textResourceProviderImpl: TextResourceProviderImpl
    ): TextResourceProvider = textResourceProviderImpl
}