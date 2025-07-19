package com.clara.data.di

import com.clara.domain.SearchArtistRepository
import com.clara.domain.model.Artist
import com.clara.domain.model.PaginatedResult
import com.clara.domain.usecase.SearchArtistUseCase
import com.clara.domain.usecase.base.FlowExecutableUseCase
import com.clara.domain.usecase.model.SearchArtistParams
import com.clara.domain.usecase.model.UseCaseResult
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
    ): FlowExecutableUseCase<SearchArtistParams, UseCaseResult<PaginatedResult<List<Artist>>>> {
        return SearchArtistUseCase(repository)
    }
}

