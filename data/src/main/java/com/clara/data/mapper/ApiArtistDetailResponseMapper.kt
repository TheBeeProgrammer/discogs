package com.clara.data.mapper

import com.clara.data.api.entities.ArtistDetailResponse
import com.clara.domain.model.ArtistDetail
import com.clara.domain.model.ArtistMembers
import javax.inject.Inject

/**
 * Mapper class for [ArtistDetailResponse] to [ArtistDetail] mapping.
 */
class ApiArtistDetailResponseMapper @Inject constructor() :
    Mapper<ArtistDetailResponse, ArtistDetail> {

    /**
     * Maps an [ArtistDetailResponse] to an [ArtistDetail].
     *
     * @param from The [ArtistDetailResponse] to map.
     * @return The mapped [ArtistDetail].
     */
    override fun map(from: ArtistDetailResponse): ArtistDetail {
        val artistDetail = ArtistDetail(
            name = from.name,
            profile = stripBBCodeToPlainText(from.profile),
            imageUrl = from.images?.firstOrNull()?.uri,
            members = from.members?.map { ArtistMembers(active = it.isActive, name = it.name) }
                ?: emptyList())
        return artistDetail
    }
}

/**
 * Strips BBCode tags from a string and returns plain text.
 *
 * This function removes common BBCode tags like [b], [i], [url], etc.,
 * as well as more complex tags with attributes. It also normalizes
 * line endings to a single newline character and trims whitespace
 * from the beginning and end of the string.
 *
 * @param input The string containing BBCode to be stripped. Can be null or blank.
 * @return The plain text string with BBCode tags removed. Returns an empty string if the input is null or blank.
 */
private fun stripBBCodeToPlainText(input: String?): String {
    if (input.isNullOrBlank()) return ""

    return input
        .replace(
            Regex(
                "\\[/?(b|i|u|url|img|quote|list|\\*|size|color|font|align|center|left|right|justify)]",
                RegexOption.IGNORE_CASE
            ), ""
        )
        .replace(Regex("\\[url=.*?]"), "")
        .replace(Regex("\\[/?\\w+(=.*?)?]"), "")
        .replace(Regex("\\r?\\n"), "\n")
        .trim()
}

