package com.clara.data.di.domain

import com.clara.domain.repositories.ArtistDetailRepository
import com.clara.domain.repositories.ArtistReleasesRepository
import com.clara.domain.repositories.SearchArtistRepository
import com.clara.domain.usecase.ArtistDetailUseCase
import com.clara.domain.usecase.ArtistReleasesUseCase
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

    @Provides
    fun providesArtistDetailUseCase(repository: ArtistDetailRepository): ArtistDetailUseCase {
        return ArtistDetailUseCase(repository)
    }

    @Provides
    fun provideArtistReleaseUseCase(repository: ArtistReleasesRepository): ArtistReleasesUseCase {
        return ArtistReleasesUseCase(repository)
    }
}