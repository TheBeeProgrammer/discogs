package com.clara.data.di

import com.clara.data.common.mapper.ApiArtistReleaseResponseMapper
import com.clara.data.common.mapper.ApiArtistSearchResponseMapper
import com.clara.data.remote.ReleasePagingSource
import com.clara.data.remote.ArtistPagingSource
import com.clara.data.remote.DiscogsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PagingModule {

    @Singleton
    @Provides
    fun provideArtistPagingSource(
        apiService: DiscogsApiService,
        mapper: ApiArtistSearchResponseMapper
    ): (String) -> ArtistPagingSource {
        return { query ->
            ArtistPagingSource(
                apiService = apiService,
                mapper = mapper,
                query = query
            )
        }
    }

    @Singleton
    @Provides
    fun provideAlbumPagingSource(
        apiService: DiscogsApiService,
        mapper: ApiArtistReleaseResponseMapper
    ): (Int) -> ReleasePagingSource {
        return { artistId ->
            ReleasePagingSource(
                apiService = apiService,
                mapper = mapper,
                artistId = artistId
            )
        }
    }
}