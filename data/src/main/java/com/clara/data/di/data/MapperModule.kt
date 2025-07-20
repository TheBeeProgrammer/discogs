package com.clara.data.di.data

import com.clara.data.api.entities.ApiArtistSearchResponse
import com.clara.data.api.entities.ArtistDetailResponse
import com.clara.data.api.entities.ArtistReleasesResponse
import com.clara.data.mapper.ApiArtistDetailResponseMapper
import com.clara.data.mapper.ApiArtistReleaseResponseMapper
import com.clara.data.mapper.ApiArtistSearchResponseMapper
import com.clara.data.mapper.Mapper
import com.clara.domain.model.Releases
import com.clara.domain.model.Artist
import com.clara.domain.model.ArtistDetail
import com.clara.domain.model.PaginatedResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    fun provideArtistSearchMapper(): Mapper<ApiArtistSearchResponse, PaginatedResult<List<Artist>>> {
        return ApiArtistSearchResponseMapper()
    }

    @Provides
    fun provideArtistDetailMapper(): Mapper<ArtistDetailResponse, ArtistDetail> {
        return ApiArtistDetailResponseMapper()
    }

    @Provides
    fun provideArtistReleasesMapper(): Mapper<ArtistReleasesResponse, PaginatedResult<List<Releases>>> {
        return ApiArtistReleaseResponseMapper()
    }
}