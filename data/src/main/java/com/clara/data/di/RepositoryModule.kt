package com.clara.data.di

import com.clara.data.common.mapper.Mapper
import com.clara.data.common.repositories.SearchArtistRepositoryImpl
import com.clara.data.remote.DiscogsApiService
import com.clara.data.remote.entities.ApiArtistSearchResponse
import com.clara.domain.SearchArtistRepository
import com.clara.domain.model.Artist
import com.clara.domain.model.PaginatedResult
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
        mapper: Mapper<ApiArtistSearchResponse, PaginatedResult<List<Artist>>>
    ): SearchArtistRepository {
        return SearchArtistRepositoryImpl(apiService, mapper)
    }
}
