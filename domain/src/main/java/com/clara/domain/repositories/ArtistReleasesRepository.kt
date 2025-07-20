package com.clara.domain.repositories

import androidx.paging.PagingData
import com.clara.domain.model.Releases
import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.flow.Flow

interface ArtistReleasesRepository {
    suspend fun getArtistReleases(artistId: Int): Flow<PagingData<Releases>>
}