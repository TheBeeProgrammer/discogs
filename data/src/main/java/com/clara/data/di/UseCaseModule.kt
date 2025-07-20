package com.clara.data.di

import com.clara.domain.SearchArtistRepository
import com.clara.domain.usecase.SearchArtistUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideSearchArtistUseCase(
        repository: SearchArtistRepository
    ): SearchArtistUseCase {
        return SearchArtistUseCase(repository)
    }
}

