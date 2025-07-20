package com.clara.data.di.data

import com.clara.data.api.DiscogsApiService
import com.clara.data.mapper.ApiArtistDetailResponseMapper
import com.clara.data.mapper.ApiArtistReleaseResponseMapper
import com.clara.data.mapper.ApiArtistSearchResponseMapper
import com.clara.data.repositories.ArtistDetailRepositoryImpl
import com.clara.data.repositories.ArtistReleasesRepositoryImpl
import com.clara.data.repositories.SearchArtistRepositoryImpl
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