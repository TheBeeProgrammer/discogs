package com.clara.clarachallenge.ui.components.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.clara.clarachallenge.R
import com.clara.domain.model.ForbiddenException
import com.clara.domain.model.NetworkUnavailableException
import com.clara.domain.model.UnauthorizedException

@Composable
fun mapErrorToMessage(throwable: Throwable): String {
    return when (throwable) {
        is NetworkUnavailableException -> stringResource(R.string.error_no_internet)
        is UnauthorizedException -> stringResource(R.string.error_unauthorized)
        is ForbiddenException -> stringResource(R.string.error_forbidden)
        else -> throwable.message ?: stringResource(R.string.unknown_error)
    }
}
