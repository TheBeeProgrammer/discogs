package com.clara.data.di

import com.clara.data.common.repositories.SearchArtistRepositoryImpl
import com.clara.data.remote.DiscogsApiService
import com.clara.data.common.mapper.ApiArtistSearchResponseMapper
import com.clara.domain.SearchArtistRepository
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
}
