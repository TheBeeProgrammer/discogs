package com.clara.data.common.repositories

import com.clara.data.common.mapper.ApiArtistDetailResponseMapper
import com.clara.data.remote.DiscogsApiService
import com.clara.domain.model.ArtistDetail
import com.clara.domain.repositories.ArtistDetailRepository
import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ArtistDetailRepositoryImpl @Inject constructor(
    private val apiService: DiscogsApiService,
    private val mapper: ApiArtistDetailResponseMapper,
) : ArtistDetailRepository, BaseRepository() {
    override suspend fun getArtistDetail(
        artistId: Int
    ): UseCaseResult<Flow<ArtistDetail>> = safeCall {
        val response = apiService.getArtistDetails(artistId)
        val artist = mapper.map(response)
        flowOf(artist)
    }
}