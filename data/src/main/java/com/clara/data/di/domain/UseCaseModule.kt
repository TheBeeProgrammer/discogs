package com.clara.data.di.domain

import androidx.paging.PagingData
import com.clara.domain.model.Artist
import com.clara.domain.model.ArtistDetail
import com.clara.domain.model.Releases
import com.clara.domain.usecase.artist.ArtistDetailUseCase
import com.clara.domain.usecase.artist.ArtistReleasesUseCase
import com.clara.domain.usecase.artist.SearchArtistUseCase
import com.clara.domain.usecase.base.ExecutableUseCase
import com.clara.domain.usecase.base.UseCaseResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideSearchArtistUseCase(
        impl: SearchArtistUseCase
    ): ExecutableUseCase<String, Flow<PagingData<Artist>>> = impl

    @Provides
    fun provideArtistDetailUseCase(
        impl: ArtistDetailUseCase
    ): ExecutableUseCase<Int, UseCaseResult<Flow<ArtistDetail>>> = impl

    @Provides
    fun provideArtisReleasesUseCase(
        impl: ArtistReleasesUseCase
    ): ExecutableUseCase<Int, Flow<PagingData<Releases>>> = impl

}