package com.clara.data.di

import com.clara.data.common.mapper.ApiArtistDetailResponseMapper
import com.clara.data.common.mapper.ApiArtistReleaseResponseMapper
import com.clara.data.common.mapper.ApiArtistSearchResponseMapper
import com.clara.data.common.mapper.Mapper
import com.clara.data.remote.entities.ApiArtistSearchResponse
import com.clara.data.remote.entities.ArtistDetailResponse
import com.clara.data.remote.entities.ArtistReleasesResponse
import com.clara.domain.model.Album
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
    fun provideArtistReleasesMapper(): Mapper<ArtistReleasesResponse, PaginatedResult<List<Album>>> {
        return ApiArtistReleaseResponseMapper()
    }
}

