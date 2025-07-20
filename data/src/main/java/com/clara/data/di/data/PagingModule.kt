package com.clara.data.di.data

import com.clara.data.api.DiscogsApiService
import com.clara.data.mapper.ApiArtistReleaseResponseMapper
import com.clara.data.mapper.ApiArtistSearchResponseMapper
import com.clara.data.repositories.ArtistReleasesPagingSource
import com.clara.data.repositories.ArtistPagingSource
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
    ): (Int) -> ArtistReleasesPagingSource {
        return { artistId ->
            ArtistReleasesPagingSource(
                apiService = apiService,
                mapper = mapper,
                artistId = artistId
            )
        }
    }
}