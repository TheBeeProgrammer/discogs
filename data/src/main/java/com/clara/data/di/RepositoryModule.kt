package com.clara.data.di

import com.clara.data.common.mapper.ApiArtistDetailResponseMapper
import com.clara.data.common.mapper.ApiArtistReleaseResponseMapper
import com.clara.data.common.mapper.ApiArtistSearchResponseMapper
import com.clara.data.common.repositories.ArtistDetailRepositoryImpl
import com.clara.data.common.repositories.ArtistReleasesRepositoryImpl
import com.clara.data.common.repositories.SearchArtistRepositoryImpl
import com.clara.data.remote.DiscogsApiService
import com.clara.domain.repositories.ArtistDetailRepository
import com.clara.domain.repositories.ArtistReleasesRepository
import com.clara.domain.repositories.SearchArtistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideSearchArtistRepository(
        apiService: DiscogsApiService,
        artistMapper: ApiArtistSearchResponseMapper
    ): SearchArtistRepository {
        return SearchArtistRepositoryImpl(apiService, artistMapper)
    }

    @Provides
    fun provideArtistMapper(): ApiArtistSearchResponseMapper {
        return ApiArtistSearchResponseMapper()
    }

    @Provides
    fun provideArtistDetailRepository(
        apiService: DiscogsApiService,
        artistDetailMapper: ApiArtistDetailResponseMapper
    ): ArtistDetailRepository {
        return ArtistDetailRepositoryImpl(apiService, artistDetailMapper)
    }

    @Provides
    fun provideArtistReleaseRepository(
        apiService: DiscogsApiService,
        albumMapper: ApiArtistReleaseResponseMapper
    ): ArtistReleasesRepository {
        return ArtistReleasesRepositoryImpl(apiService, albumMapper)
    }
}
