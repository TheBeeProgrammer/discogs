package com.clara.domain.repositories

import androidx.paging.PagingData
import com.clara.domain.model.Album
import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.flow.Flow

interface ArtistReleasesRepository {
    suspend fun getArtistReleases(artistId: Int): UseCaseResult<Flow<PagingData<Album>>>
}